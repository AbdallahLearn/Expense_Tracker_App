//package com.example.expense_tracking_project.screens.expenseTracking.data.data_source.local
//
//import com.example.expense_tracking_project.core.local.dao.TransactionDao
//import com.example.expense_tracking_project.core.local.entities.Transaction
//import com.example.expense_tracking_project.screens.expenseTracking.data.data_source.TransactionDataSource
//import javax.inject.Inject
//import kotlin.collections.forEach
//
//class TransactionLocalDataSource @Inject constructor(
//    private val transactionDao: TransactionDao
//) : TransactionDataSource {
//
//    override suspend fun insertTransaction(transaction: Transaction) =
//        transactionDao.insert(transaction)
//
//    override suspend fun getAllTransactions() = transactionDao.getAllTransactions()
//
//    override suspend fun getTransactionById(id: Int): Transaction? {
//        return transactionDao.getTransactionById(id)
//    }
//
//    override suspend fun updateTransaction(transaction: Transaction) =
//        transactionDao.update(transaction)
//
//    override suspend fun clearAndInsert(transaction: List<Transaction>) {
//        transactionDao.clearTransactions()
//        transaction.forEach { transactionDao.insert(it) }
//    }
//
//
//}