package com.example.expense_tracking_project.screens.expenseTracking.domain.repository

import androidx.lifecycle.LiveData
import com.example.expense_tracking_project.core.local.entities.Transaction
import com.example.expense_tracking_project.core.local.dao.TransactionDao

class TransactionRepository(private val transactionDao: TransactionDao) {

    val allTransactions: LiveData<List<Transaction>> = transactionDao.getAllTransactions()

    suspend fun insert(transaction: Transaction) {
        transactionDao.insert(transaction)
    }

    suspend fun update(transaction: Transaction) {
        transactionDao.update(transaction)
    }
}
