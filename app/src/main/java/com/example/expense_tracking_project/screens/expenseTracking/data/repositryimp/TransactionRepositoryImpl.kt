package com.example.expense_tracking_project.screens.expenseTracking.data.repositryimp



import com.example.expense_tracking_project.core.local.dao.TransactionDao
import com.example.expense_tracking_project.core.local.entities.Transaction
import com.example.expense_tracking_project.screens.expenseTracking.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow


class TransactionRepositoryImpl(private val transactionDao: TransactionDao) :
    TransactionRepository {

    override suspend fun allTransactions(): Flow<List<Transaction>> {
        return transactionDao.getAllTransactions()
    }

    override suspend fun insert(transaction: Transaction) {
        transactionDao.insert(transaction)
    }

    override suspend fun update(transaction: Transaction) {
        transactionDao.update(transaction)
    }
}