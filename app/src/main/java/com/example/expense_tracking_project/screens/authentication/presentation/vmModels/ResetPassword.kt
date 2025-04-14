package com.example.expense_tracking_project.screens.authentication.presentation.vmModels

import com.google.firebase.auth.FirebaseAuth

fun resetPassword(email: String, onResult: (Boolean) -> Unit) {
    FirebaseAuth.getInstance().sendPasswordResetEmail(email)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                FirebaseAuth.getInstance().signOut()
            }
            onResult(task.isSuccessful)
        }
}