package com.example.expense_tracking_project.screens.authentication.presentation.vmModels

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.expense_tracking_project.navigation.Screen
import com.example.expense_tracking_project.screens.authentication.domain.usecase.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class AuthState {
    data object Unauthenticated : AuthState()
    data object Authenticated : AuthState()
    data object Loading : AuthState()
    data class Error(val message: String) : AuthState()
}
@HiltViewModel
class LoginViewModel @Inject constructor (private val loginUseCase: LoginUseCase) : ViewModel() {

    var email by mutableStateOf("")
    var password by mutableStateOf("")

    private val _authState = MutableStateFlow<AuthState>(AuthState.Unauthenticated)
    val authState: StateFlow<AuthState> = _authState

    private val _isPasswordResetCompleted = MutableStateFlow(false)
    val isPasswordResetCompleted: StateFlow<Boolean> = _isPasswordResetCompleted

    fun authenticate(isAuthenticated: Boolean) {
        _authState.value = if (isAuthenticated) {
            AuthState.Authenticated
        } else {
            AuthState.Unauthenticated
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val result = loginUseCase(email, password)

            _authState.value = result.fold(
                onSuccess = { AuthState.Authenticated },
                onFailure = { AuthState.Error(it.message ?: "Login failed.") }
            )
        }
    }
}

fun handleAuthStateLogin(
    authState: AuthState,
    context: Context,
    navController: NavController
) {
    when (authState) {
        is AuthState.Authenticated -> {
            Toast.makeText(context, "Logged in successfully", Toast.LENGTH_SHORT).show()
            navController.navigate(Screen.Home) {
                popUpTo(Screen.Login) { inclusive = true }
            }
        }

        is AuthState.Error -> {
            Toast.makeText(context, authState.message, Toast.LENGTH_SHORT).show()
        }

        else -> Unit
    }
}