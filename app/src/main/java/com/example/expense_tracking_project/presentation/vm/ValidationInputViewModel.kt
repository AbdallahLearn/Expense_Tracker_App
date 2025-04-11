package com.example.expense_tracking_project.presentation.vm

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class ValidationInputViewModel : ViewModel() {

    // For Login
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var emailError by mutableStateOf<String?>(null)
    var passwordError by mutableStateOf<String?>(null)


    // For Sign Up
    var name by mutableStateOf("")
    var confirmPassword by mutableStateOf("")
    var nameError by  mutableStateOf<String?>(null)
    var confirmPasswordError by mutableStateOf<String?>(null)

    // Login validation for real-time feedback
    fun validateEmail() {
        emailError = if (email.isBlank()) {
            "Email is required"
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            "Invalid email format"
        } else {
            null
        }
    }

    fun validatePassword() {
        passwordError = if (password.isBlank()) {
            "Password is required"
        } else if (password.length < 8) {
            "Password must be at least 8 characters"
        } else {
            null
        }
    }

    fun validateName() {
        nameError = if (name.isBlank()) {
            "Name is required"
        } else {
            null
        }
    }
    fun validateConfirmPassword() {
        confirmPasswordError = when {
            confirmPassword.isBlank() -> "Confirm Password is required"
            confirmPassword != password -> "Passwords do not match"
            else -> null
        }
    }
}
