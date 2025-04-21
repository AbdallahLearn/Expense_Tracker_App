package com.example.expense_tracking_project.screens.dataSynchronization.domain.repository

interface SyncTransactionRepository {
    suspend fun syncTransactions(): Unit
}