//package com.example.expense_tracking_project.screens.expenseTracking.data.data_source.remote
//
//import com.example.expense_tracking_project.core.local.entities.Transaction
//import com.example.expense_tracking_project.screens.expenseTracking.data.data_source.RemoteTransactionDataSource
//import com.example.expense_tracking_project.screens.expenseTracking.data.mapper.toDto
//import com.example.expense_tracking_project.screens.expenseTracking.data.mapper.toEntity
//import com.example.expense_tracking_project.screens.expenseTracking.data.remote.TransactionApi
//import javax.inject.Inject
//
//class RemoteTransactionDataSourceImpl @Inject constructor(
//    private val transactionApi: TransactionApi
//) : RemoteTransactionDataSource {
//    override suspend fun insertTransaction(transaction: Transaction) {
//        transactionApi.createTransaction(transaction.toDto())
//    }
//
//    override suspend fun getAllTransactions(): List<Transaction> {
//        val response = transactionApi.getTransaction()
//        return response.body()?.map { it.toEntity() } ?: emptyList()
//    }
//}