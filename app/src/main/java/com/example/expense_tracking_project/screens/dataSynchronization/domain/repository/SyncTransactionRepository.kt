package com.example.expense_tracking_project.screens.dataSynchronization.domain.repository

import com.example.expense_tracking_project.core.local.entities.Transaction

interface SyncTransactionRepository {
    suspend fun syncTransactions(): Unit
    suspend fun getTransactionsFromApi(): List<Transaction>
}