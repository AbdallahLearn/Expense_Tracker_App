package com.example.expense_tracking_project.screens.dataSynchronization.data.repositryimp

import android.util.Log
import com.example.expense_tracking_project.core.local.dao.TransactionDao
import com.example.expense_tracking_project.screens.dataSynchronization.domain.repository.SyncTransactionRepository
import com.example.expense_tracking_project.screens.expenseTracking.data.mapper.toDto
import com.example.expense_tracking_project.screens.expenseTracking.data.remote.TransactionApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SyncTransactionRepositoryImpl @Inject constructor(
    private val transactionDao: TransactionDao, // local room database
    private val transactionApi: TransactionApi // remote server
) : SyncTransactionRepository {

    override suspend fun syncTransactions(): Unit = withContext(Dispatchers.IO) {
        val unSyncedTransactions = transactionDao.getUnSyncedTransactions()
        unSyncedTransactions.forEach { transaction ->
            try {
                val response = transactionApi.createTransaction(transaction.toDto())
                if (response.isSuccessful) {
                    Log.d("SYNC", "Transaction '${transaction.amount}' synced to server")
                    Log.d("SYNC", "Fetched Transaction: ${response.body()}")
                    transactionDao.markTransactionAsSynced(transaction.transactionId)
                } else {
                    Log.e("SYNC", "Failed to sync '${transaction.amount}': ${response.code()}")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
