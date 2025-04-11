package com.example.expense_tracking_project.presentation.vm

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import android.util.Patterns

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

    // Validate Email (for login and sign up)
    fun validateEmail() {
        emailError = if (email.isBlank()) {
            "Email is required"
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            "Invalid email format"
        } else {
            null
        }
    }

    // Validate Password (for login)
    fun validatePassword() {
        passwordError = if (password.isBlank()) {
            "Password is required"
        } else if (password.length < 8) {
            "Password must be at least 8 characters"
        } else {
            null
        }
    }

    // Validate Name (for sign up)
    fun validateName() {
        nameError = if (name.isBlank()) {
            "Name is required"
        } else {
            null
        }
    }

    // Validate Confirm Password (for sign up)
    fun validateConfirmPassword() {
        confirmPasswordError = when {
            confirmPassword.isBlank() -> "Confirm Password is required"
            confirmPassword != password -> "Passwords do not match"
            else -> null
        }
    }

    // Form validation (for sign up)
    fun isFormValid(
        name: String,
        email: String,
        password: String,
        confirmPassword: String
    ): Boolean {
        validateName()
        validateEmail()
        validatePassword()
        validateConfirmPassword()

        return nameError == null && emailError == null && passwordError == null && confirmPasswordError == null
    }
}
