package com.example.expense_tracking_project.data.model

// Sealed class to represent the authentication states
sealed class AuthState {
    object Authenticated : AuthState()  // User is authenticated
    object Unauthenticated : AuthState()  // User is not authenticated
    object Loading : AuthState()  // The process is loading (signup/login)
    data class Error(val message: String) : AuthState()  // An error occurred with the message
}