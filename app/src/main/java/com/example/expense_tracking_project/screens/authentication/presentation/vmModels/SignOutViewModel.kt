package com.example.expense_tracking_project.screens.authentication.presentation.vmModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.expense_tracking_project.screens.authentication.data.model.AuthState
//import com.example.expense_tracking_project.data.model.AuthState
import com.google.firebase.auth.FirebaseAuth

class SignOutViewModel : ViewModel() {

    // Initialize FirebaseAuth instance to manage authentication
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    // LiveData to observe authentication state (Authenticated, Unauthenticated, Loading, or Error)
    private val _authState = MutableLiveData<AuthState>()

    init {
        // Check the authentication status as soon as the ViewModel is initialized
        checkAuthStatus()
    }

    // Function to  check if the user is already authenticated or not
    fun checkAuthStatus() {
        if (auth.currentUser == null) {
            // If the user is not authenticated, set the state to Unauthenticated
            _authState.value = AuthState.Unauthenticated
        } else {
            // If the user is authenticated, set the state to Authenticated
            _authState.value = AuthState.Authenticated
        }
    }

    // Function to sign out the user and set the state to Unauthenticated
    fun signout() {
        auth.signOut()
        _authState.value = AuthState.Unauthenticated
    }

}