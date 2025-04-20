package com.example.expense_tracking_project.screens.dataSynchronization.domain.repository

interface SyncCategoryRepository {
    suspend fun syncCategory(): Unit
}