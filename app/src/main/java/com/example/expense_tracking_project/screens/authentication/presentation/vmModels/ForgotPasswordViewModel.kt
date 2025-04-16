package com.example.expense_tracking_project.screens.authentication.presentation.vmModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expense_tracking_project.screens.authentication.domain.usecase.ForgotPasswordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor (private val forgotPasswordUseCase: ForgotPasswordUseCase) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Unauthenticated)
    var email by mutableStateOf("")

    fun forgotPassword() {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                forgotPasswordUseCase(email.trim())
                _authState.value = AuthState.Authenticated
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Reset Password Failed.")
            }
        }
    }
}
