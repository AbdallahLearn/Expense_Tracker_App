package com.example.expense_tracking_project.screens.authentication.presentation.vmModels

import android.util.Patterns
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel

class ValidationInputViewModel : ViewModel() {

    // Form inputs
    var name by mutableStateOf("")
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var confirmPassword by mutableStateOf("")

    // Error messages
    var emailAndPasswordError by mutableStateOf<String?>(null)
    var fieldsError by mutableStateOf<String?>(null)

    // Regex for password strength: min 8 chars, upper, lower, digit, special char
    private val passwordPattern = Regex(
        "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@\$!%*?&])[A-Za-z\\d@\$!%*?&]{8,}$"
    )

    /**
     * Validate only email and password (used in Sign In)
     */
    fun validateEmailAndPassword() {
        emailAndPasswordError = when {
            email.isBlank() || password.isBlank() ->
                "Email and password can't be empty"
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() ->
                "Invalid email format"
            else -> null
        }
    }

    /**
     * Validate full form (used in Sign Up)
     */
    fun validateFields() {
        fieldsError = when {
            name.isBlank() || email.isBlank() || password.isBlank() || confirmPassword.isBlank() ->
                "All fields are required"
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() ->
                "Invalid email format"
            !passwordPattern.matches(password) ->
                "Password must be 8+ chars with upper, lower, digit & special char"
            password != confirmPassword ->
                "Passwords do not match"
            else -> null
        }
    }

    /**
     * Return true if all fields are valid (Sign Up)
     */
    fun isFormValid(
        name: String,
        email: String,
        password: String,
        confirmPassword: String
    ): Boolean {
        this.name = name
        this.email = email
        this.password = password
        this.confirmPassword = confirmPassword

        validateFields()
        return fieldsError == null
    }
}
