package com.example.expense_tracking_project.screens.authentication.presentation.vmModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.expense_tracking_project.screens.authentication.domain.usecase.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class AuthState {
    object Unauthenticated : AuthState()
    object Authenticated : AuthState()
    object Loading : AuthState()
    data class Error(val message: String) : AuthState()
}
@HiltViewModel
class SignInViewModel @Inject constructor (private val loginUseCase: LoginUseCase) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Unauthenticated)
    val authState: StateFlow<AuthState> = _authState

    private val _isPasswordResetCompleted = MutableStateFlow(false)
    val isPasswordResetCompleted: StateFlow<Boolean> = _isPasswordResetCompleted

    fun setPasswordResetCompleted(isCompleted: Boolean) {
        _isPasswordResetCompleted.value = isCompleted
    }

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
            try {
                loginUseCase(email, password)
                _authState.value = AuthState.Authenticated
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Login failed.")
            }
        }
    }
}


//// Factory class to create ViewModel with required dependencies
//class SignInViewModelFactory(
//    private val loginUseCase: LoginUseCase
//) : ViewModelProvider.Factory {
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        if (modelClass.isAssignableFrom(SignInViewModel::class.java)) {
//            return SignInViewModel(loginUseCase) as T
//        }
//        throw IllegalArgumentException("Unknown ViewModel class")
//    }
//}





