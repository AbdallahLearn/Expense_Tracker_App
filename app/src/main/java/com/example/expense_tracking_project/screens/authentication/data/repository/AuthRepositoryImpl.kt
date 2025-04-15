package com.example.expense_tracking_project.screens.authentication.data.repository

import com.example.expense_tracking_project.screens.authentication.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class AuthRepositoryImpl @Inject constructor(private val firebaseAuth: FirebaseAuth) : AuthRepository {

    override suspend fun signUp(name: String, email: String, password: String): Result<Unit> {
        return try {
            FirebaseAuth.getInstance()
                .createUserWithEmailAndPassword(email, password)
                .await()

            // Optionally update display name
            val user = FirebaseAuth.getInstance().currentUser
            user?.updateProfile(
                UserProfileChangeRequest.Builder().setDisplayName(name).build()
            )?.await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun login(email: String, password: String): Result<Unit> {
        return try {
            firebaseAuth.signInWithEmailAndPassword(email, password).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun sendPasswordResetEmail(email: String) {
        firebaseAuth.sendPasswordResetEmail(email).await()
    }
}


