package com.example.expense_tracking_project.presentation.vm


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.expense_tracking_project.data.model.AuthState
import com.google.firebase.auth.FirebaseAuth
import java.util.regex.Pattern

class SignUpViewModel : ViewModel() {

    // Initialize FirebaseAuth instance to manage authentication
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    // LiveData to observe authentication state (Authenticated, Unauthenticated, Loading, or Error)
    private val _authState = MutableLiveData<AuthState>()
    val authState: LiveData<AuthState> = _authState

    // Regular expression for validating an email format
    private val emailPattern: Pattern =
        Pattern.compile("^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}$")

    // Regular expression for validating password format
    private val passwordPattern: Pattern =
        Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$")

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

    // Function to validate the email format using a regular expression
    fun isValidEmail(email: String): Boolean {
        return emailPattern.matcher(email).matches()
    }

    // Function to validate the password format using a regular expression
    fun isValidPassword(password: String): Boolean {
        return passwordPattern.matcher(password).matches()
    }


    // Function to handle the signup process
    fun signup(name: String, email: String, password: String, confirmPassword: String) {

        // Check if any field is empty
        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            _authState.value = AuthState.Error("Fields can't be empty")
            return
        }

        // Check if the password and confirm password match
        if (password != confirmPassword) {
            _authState.value = AuthState.Error("Passwords don't match")
            return
        }

        // Check if the email format is valid
        if (!isValidEmail(email)) {
            _authState.value = AuthState.Error("Please, Enter a valid email")
            return
        }

        // Check if the password format is valid
        if (!isValidPassword(password)) {
            _authState.value =
                AuthState.Error("Password must be at least 8 characters, include one uppercase letter, one lowercase letter, one number, and one symbol")
            return
        }

        // Set the state to Loading while the signup is being processed
        _authState.value = AuthState.Loading

        // create a new user with email and password using Firebase Authentication
        auth.createUserWithEmailAndPassword(email, password)
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

