package com.example.expense_tracking_project.core
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

class TokenProvider {
    suspend fun getToken(): String? {
        val user = FirebaseAuth.getInstance().currentUser
        return try {
            user?.getIdToken(true)?.await()?.token
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}