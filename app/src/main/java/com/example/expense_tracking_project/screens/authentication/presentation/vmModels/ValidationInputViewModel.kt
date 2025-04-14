package com.example.expense_tracking_project.screens.authentication.presentation.vmModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import android.util.Patterns

class ValidationInputViewModel : ViewModel() {

    // For Login
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var emailAndPasswordError by mutableStateOf<String?>(null)

    // For Sign Up
    var name by mutableStateOf("")
    var confirmPassword by mutableStateOf("")
    var fieldsError by mutableStateOf<String?>(null)

    // Validate Email and Password (combined for login)
    fun validateEmailAndPassword() {
        emailAndPasswordError = if (email.isBlank() || password.isBlank()) {
            "Email and password can't be empty"
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            "Invalid email format"
        } else {
            null
        }
    }

    // Validate Fields (for sign up)
    fun validateFields() {
        fieldsError =
            if (name.isBlank() || email.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
                "Fields can't be empty"
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                "Invalid email format"
            } else if (!Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@\$!%*?&])[A-Za-z\\d@\$!%*?&]{8,}\$").matches(
                    password
                )
            ) {
                "Password must be at least 8 characters long, contain at least one uppercase letter, one lowercase letter, one number, and one special character"
            } else if (confirmPassword != password) {
                "Passwords do not match"
            } else {
                null
            }
    }

    // Form validation (for sign up)
    fun isFormValid(
        name: String,
        email: String,
        password: String,
        confirmPassword: String
    ): Boolean {
        validateFields()
        validateEmailAndPassword()

        return fieldsError == null && emailAndPasswordError == null
    }
}
