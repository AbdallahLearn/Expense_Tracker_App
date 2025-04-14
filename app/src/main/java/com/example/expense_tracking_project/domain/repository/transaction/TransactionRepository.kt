package com.example.expense_tracking_project.domain.repository.transaction

import com.example.expense_tracking_project.data.dataSource.Transaction.Transaction
import kotlinx.coroutines.flow.Flow

interface TransactionRepository {
    suspend fun allTransactions(): Flow<List<Transaction>>
    suspend fun insert(transaction: Transaction)
    suspend fun update(transaction: Transaction)
}
