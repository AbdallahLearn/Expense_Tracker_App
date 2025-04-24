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
                val category = transaction.categoryId?.let { categoryDao.getCategoryById(it) }
                Log.d("SYNC", "🔗 Linked category = $category")
                val serverId = category?.categoryServerId

                if (serverId.isNullOrEmpty()) {
                    Log.w(
                        "SYNC",
                        "Skipping transaction ${transaction.transactionId} — missing categoryServerId"
                    )
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
                    val remoteTransactions: List<Transaction> =
                        response.body()?.expenses?.map { it.toEntity() } ?: emptyList()
                    Log.d("SYNC", "Fetched transactions: $remoteTransactions")

                    remoteTransactions.forEach { transaction ->

                        val existing = transactionDao.getTransactionByAmount(transaction.amount, transaction.date)
                        if (existing != null) {
                            val updated = transaction.copy(amount = existing.amount)
                            transactionDao.update(updated)
                        } else {
                            transactionDao.insert(transaction)
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