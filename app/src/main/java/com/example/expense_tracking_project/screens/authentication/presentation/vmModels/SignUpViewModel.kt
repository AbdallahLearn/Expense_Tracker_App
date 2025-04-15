package com.example.expense_tracking_project.screens.authentication.presentation.vmModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.expense_tracking_project.screens.authentication.domain.usecase.SignUpUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SignUpViewModel(
    private val signUpUseCase: SignUpUseCase
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Unauthenticated)
    val authState: StateFlow<AuthState> = _authState

    fun signUp(name: String, email: String, password: String, confirmPassword: String) {
        if (name.isBlank() || email.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
            _authState.value = AuthState.Error("Fields can't be empty")
            return
        }

        if (password != confirmPassword) {
            _authState.value = AuthState.Error("Passwords don't match")
            return
        }

        _authState.value = AuthState.Loading

        viewModelScope.launch {
            val result = signUpUseCase.invoke(name, email, password, confirmPassword)
            _authState.value = result.fold(
                onSuccess = { AuthState.Authenticated },
                onFailure = { AuthState.Error(it.message ?: "Unknown error") }
            )
        }
    }
}

class SignUpViewModelFactory(
    private val signUpUseCase: SignUpUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SignUpViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SignUpViewModel(signUpUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
