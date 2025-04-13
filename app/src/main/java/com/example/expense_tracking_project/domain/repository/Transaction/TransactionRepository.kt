package com.example.expense_tracking_project.domain.repository.Transaction

import androidx.lifecycle.LiveData
import com.example.expense_tracking_project.data.dataSource.Transaction
import com.example.expense_tracking_project.data.dataSource.TransactionDao

class TransactionRepository(private val transactionDao: TransactionDao) {

    val allTransactions: LiveData<List<Transaction>> = transactionDao.getAllTransactions()

    suspend fun insert(transaction: Transaction) {
        transactionDao.insert(transaction)
    }

    suspend fun update(transaction: Transaction) {
        transactionDao.update(transaction)
    }
}
