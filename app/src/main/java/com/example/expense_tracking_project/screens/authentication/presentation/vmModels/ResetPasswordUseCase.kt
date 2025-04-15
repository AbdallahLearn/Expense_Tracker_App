package com.example.expense_tracking_project.screens.authentication.presentation.vmModels

import com.google.firebase.auth.FirebaseAuth

class ResetPasswordUseCase(private val auth: FirebaseAuth) {
    fun resetPassword(email: String, onResult: (Boolean, String?) -> Unit) {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onResult(true, null)
                } else {
                    onResult(false, task.exception?.localizedMessage ?: "Unknown error")
                }
            }
    }
} // we have to check that
