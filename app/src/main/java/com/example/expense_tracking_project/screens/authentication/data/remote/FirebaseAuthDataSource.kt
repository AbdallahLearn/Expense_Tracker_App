package com.example.expense_tracking_project.screens.authentication.data.remote

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import com.example.expense_tracking_project.core.TokenProvider


class FirebaseAuthDataSource @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) {

    suspend fun signUp(name: String, email: String, password: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, password).await()
        val user = firebaseAuth.currentUser
        user?.updateProfile(
            UserProfileChangeRequest.Builder().setDisplayName(name).build()
        )?.await()
    }

    suspend fun login(email: String, password: String): Boolean {
        firebaseAuth.signInWithEmailAndPassword(email, password).await()
        val user = firebaseAuth.currentUser
        if (user == null) {
            return false
        } else {

            val tokenProvider = TokenProvider()
            val token = tokenProvider.getToken()

            if (token != null) {
                println("Token: $token")
                return true
            } else {
                println("Token not found")
                return false
            }
        }
    }

    suspend fun sendPasswordResetEmail(email: String) {
        firebaseAuth.sendPasswordResetEmail(email).await()
    }
}
