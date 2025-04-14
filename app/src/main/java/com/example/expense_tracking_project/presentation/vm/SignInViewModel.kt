package com.example.expense_tracking_project.presentation.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.expense_tracking_project.data.model.AuthState
import com.google.firebase.auth.FirebaseAuth

class SignInViewModel : ViewModel() {

    // Initialize FirebaseAuth instance to manage authentication
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    // LiveData to observe authentication state (Authenticated, Unauthenticated, Loading, or Error)
    private val _authState = MutableLiveData<AuthState>()
    val authState: LiveData<AuthState> = _authState

    private var passwordResetCompleted = false // Flag to track password reset

    init {
        // Check the authentication status as soon as the ViewModel is initialized
        checkAuthStatus()
    }

    // Function to check if the user is already authenticated or not
    fun checkAuthStatus() {
        if (auth.currentUser == null) {
            // If the user is not authenticated, set the state to Unauthenticated
            _authState.value = AuthState.Unauthenticated
        } else {
            // If the user is authenticated, set the state to Authenticated
            _authState.value = AuthState.Authenticated
        }
    }

    fun setPasswordResetCompleted(completed: Boolean) {
        passwordResetCompleted = completed
    }

    fun isPasswordResetCompleted(): Boolean {
        return passwordResetCompleted
    }

    fun authenticate(isAuthenticated: Boolean) {
        if (isAuthenticated) {
            _authState.value = AuthState.Authenticated
            passwordResetCompleted = false // Reset the flag after successful login
        } else {
            _authState.value = AuthState.Unauthenticated
            passwordResetCompleted = false // Reset the flag when logging out or unauthenticated
        }
    }

    // Function to handle the login process
    fun login(email: String, password: String) {

        // Check if the email or password is empty
        if (email.isEmpty() || password.isEmpty()) {
            _authState.value = AuthState.Error("Email or password can't be empty")
            return
        }

        // Set the state to Loading while the login is being processed
        _authState.value = AuthState.Loading

        // sign in with email and password using Firebase Authentication
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // If successful, set the state to Authenticated
                    _authState.value = AuthState.Authenticated
                } else {
                    // If there's an error, show the error message
                    _authState.value =
                        AuthState.Error(task.exception?.message ?: "Something went wrong")
                }
            }
    }
}
