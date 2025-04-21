package com.example.expense_tracking_project.screens.expenseTracking.domain.repository

import com.example.expense_tracking_project.core.local.entities.Transaction
import kotlinx.coroutines.flow.Flow

interface TransactionRepository {
    suspend fun allTransactions(): Flow<List<Transaction>>
    suspend fun insert(transaction: Transaction)
    suspend fun update(transaction: Transaction)
    suspend fun softDeleteTransaction(transactionId: Int)
}
