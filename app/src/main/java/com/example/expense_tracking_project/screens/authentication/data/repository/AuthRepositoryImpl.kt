package com.example.expense_tracking_project.screens.authentication.data.repository

import com.example.expense_tracking_project.screens.authentication.data.remote.FirebaseAuthDataSource
import com.example.expense_tracking_project.screens.authentication.domain.repository.AuthRepository
import javax.inject.Inject


class AuthRepositoryImpl @Inject constructor(private val firebaseAuthDataSource: FirebaseAuthDataSource) : AuthRepository {

    override suspend fun signUp(name: String, email: String, password: String): Result<Unit> {
        return try {
            firebaseAuthDataSource.signUp(name, email, password)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    override suspend fun login(email: String, password: String): Result<Unit> {
        return try {
            firebaseAuthDataSource.login(email, password)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun sendPasswordResetEmail(email: String) {
        firebaseAuthDataSource.sendPasswordResetEmail(email)
    }
}




