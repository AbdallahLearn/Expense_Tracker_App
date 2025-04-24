package com.example.expense_tracking_project.screens.expenseTracking.domain.repository

interface AuthRepository {
    suspend fun saveToken(token: String)
    fun getToken(): String?
    fun logout()
}

