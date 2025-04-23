//package com.example.expense_tracking_project.screens.expenseTracking.data.data_source
//
//import com.example.expense_tracking_project.core.local.entities.Transaction
//import kotlinx.coroutines.flow.Flow
//
//interface TransactionDataSource {
//    suspend fun insertTransaction(transaction: Transaction)
//    suspend fun getAllTransactions(): Flow<List<Transaction>>
//    suspend fun getTransactionById(id: Int): Transaction?
//    suspend fun updateTransaction(transaction: Transaction)
//    suspend fun clearAndInsert(transactions: List<Transaction>)
//}