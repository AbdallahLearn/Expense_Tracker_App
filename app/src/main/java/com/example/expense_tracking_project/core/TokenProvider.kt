package com.example.expense_tracking_project.core

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class TokenProvider @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) {
    suspend fun getToken(): String {
        val user = firebaseAuth.currentUser
        return try {
            user?.getIdToken(true)?.await()?.token ?: ""
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }
}
