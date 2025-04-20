package com.example.expense_tracking_project.screens.dataSynchronization.domain.repository

interface SyncRepository {
    suspend fun syncBudgets() : Unit
}
