package com.example.expense_tracking_project.screens.dataSynchronization.data.repositryimp

import android.util.Log
import com.example.expense_tracking_project.core.local.dao.CategoryDao
import com.example.expense_tracking_project.core.local.dao.TransactionDao
import com.example.expense_tracking_project.core.local.entities.Transaction
import com.example.expense_tracking_project.screens.dataSynchronization.domain.repository.SyncTransactionRepository
import com.example.expense_tracking_project.screens.expenseTracking.data.mapper.toDto
import com.example.expense_tracking_project.screens.expenseTracking.data.mapper.toEntity
import com.example.expense_tracking_project.screens.expenseTracking.data.remote.TransactionApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SyncTransactionRepositoryImpl @Inject constructor(
    private val transactionDao: TransactionDao, // local room database
    private val transactionApi: TransactionApi, // remote server
    private val categoryDao: CategoryDao
) : SyncTransactionRepository {

    override suspend fun syncTransactions(): Unit = withContext(Dispatchers.IO) {
        val unSyncedTransactions = transactionDao.getUnSyncedTransactions()

        Log.d("SYNC", "Local unsynced transactions: $unSyncedTransactions")

        unSyncedTransactions.forEach { transaction ->
            try {
                // Check if the transaction already exists in the local DB (even after a sync attempt)
                val existingTransactionCount = transactionDao.checkDuplicateTransaction(transaction.amount, transaction.date)
                if (existingTransactionCount > 0) {
                    Log.d("SYNC", "Duplicate transaction found locally: Skipping sync for ${transaction.amount} on ${transaction.date}")
                    return@forEach // Skip syncing this transaction
                }

                // Sync the transaction to the server
                Log.d("SYNC", "Preparing to sync transaction: ${transaction.amount} with category: ${transaction.categoryId}")

                val category = transaction.categoryId?.let { categoryDao.getCategoryById(it) }
                Log.d("SYNC", "🔗 Linked category = $category")
                val serverId = category?.categoryServerId

                if (serverId.isNullOrEmpty()) {
                    Log.w("SYNC", "Skipping transaction ${transaction.transactionId} — missing categoryServerId")
                    return@forEach
                }

                val dto = transaction.toDto(serverId)
                val response = transactionApi.createTransaction(dto)

                if (response.isSuccessful) {
                    Log.d("SYNC", "Transaction '${transaction.amount}' synced to server")
                    transactionDao.markTransactionAsSynced(transaction.transactionId)
                } else {
                    Log.e("SYNC", "Failed to sync '${transaction.amount}': ${response.code()}")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    override suspend fun getTransactionsFromApi(): List<Transaction> {
        return withContext(Dispatchers.IO) {
            try {
                val response = transactionApi.getTransaction()
                if (response.isSuccessful) {
                    Log.d("SYNC", "API Response: ${response.body()}")
                    val remoteTransactions: List<Transaction> = response.body()?.expenses?.map { it.toEntity() } ?: emptyList()
                    Log.d("SYNC", "Fetched transactions: $remoteTransactions")

                    remoteTransactions.forEach { transaction ->
                        // Check if the transaction already exists in the database (based on amount and date)
                        val existingTransactionCount = transactionDao.checkDuplicateTransaction(transaction.amount, transaction.date)

                        if (existingTransactionCount == 0) {
                            // If not found, insert the transaction
                            transactionDao.insert(transaction)
                            Log.d("SYNC", "Transaction inserted into local database")
                        } else {
                            // If found, log it as a duplicate and skip insertion
                            Log.d("SYNC", "Duplicate transaction found locally: Skipping insertion for ${transaction.amount} on ${transaction.date}")
                        }
                    }
                    Log.d("SYNC", "Fetched ${remoteTransactions.size} transactions from server")
                    remoteTransactions
                } else {
                    Log.e("SYNC", "Failed to fetch transactions: ${response.code()}")
                    emptyList()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                emptyList()
            }
        }
    }


}