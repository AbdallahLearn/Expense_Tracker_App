package com.example.expense_tracking_project.data.repositoryImp

import com.example.expense_tracking_project.data.dataSource.Transaction.Transaction
import com.example.expense_tracking_project.data.dataSource.Transaction.TransactionDao
import com.example.expense_tracking_project.domain.repository.transaction.TransactionRepository
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

