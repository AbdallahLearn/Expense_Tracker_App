package com.example.expense_tracking_project.screens.authentication.data.model

sealed class AuthState {
    object Authenticated : AuthState()  // User is authenticated
    object Unauthenticated : AuthState()  // User is not authenticated
    object Loading : AuthState()  // The process is loading (signup/login)
    data class Error(val message: String) : AuthState()  // An error occurred with the message
}