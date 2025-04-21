package com.example.expense_tracking_project.screens.dataSynchronization.data

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
        val token =
            "Bearer eyJhbGciOiJSUzI1NiIsImtpZCI6Ijg1NzA4MWNhOWNiYjM3YzIzNDk4ZGQzOTQzYmYzNzFhMDU4ODNkMjgiLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJodHRwczovL3NlY3VyZXRva2VuLmdvb2dsZS5jb20vZXhwZW5zZS10cmFja2VyLTc4YjA3IiwiYXVkIjoiZXhwZW5zZS10cmFja2VyLTc4YjA3IiwiYXV0aF90aW1lIjoxNzQ1MjA3ODkxLCJ1c2VyX2lkIjoiTDZEZkdUUnV5M1FOYW9RcGlzeHlDWWFQU1psMiIsInN1YiI6Ikw2RGZHVFJ1eTNRTmFvUXBpc3h5Q1lhUFNabDIiLCJpYXQiOjE3NDUyMDc4OTEsImV4cCI6MTc0NTIxMTQ5MSwiZW1haWwiOiJmYXJlZWhhQGF0b21jYW1wLmNvbSIsImVtYWlsX3ZlcmlmaWVkIjpmYWxzZSwiZmlyZWJhc2UiOnsiaWRlbnRpdGllcyI6eyJlbWFpbCI6WyJmYXJlZWhhQGF0b21jYW1wLmNvbSJdfSwic2lnbl9pbl9wcm92aWRlciI6InBhc3N3b3JkIn19.bAhtuwFOQTlHJGQmrO_S256qx6DrElCCadKk2jGyuCvRDbNzoDGsNycwNwZaXfpTTehDRQ8h-jXF9ZLKEiaX6aKgCq_tLP4cw8oiO4kjfXUZFfcnoCdb7w6RWAhGjDnmQb34vN8aUYYuGrf4TnzviFREifgNt_zGGxB-6kSjdgANXBZgJBBCtGEKwnkUjV29J4DM5AbljC7ftTIl1HAKsMiVGN5Q0kVHYRZ3iQGUNae9potYDUnoZDb-xqTwhCUct1lr08aFbrWkqTlehbNcY_vFjUQzMhYJ9mGe6L4dqpnyuosDBUGN6VD-qxm8mUAWCQbwspkAV2_nCyyK3c5BnA"
        val unSyncedTransactions = transactionDao.getUnSyncedTransactions()
        unSyncedTransactions.forEach { transaction ->
            try {
                val response = transactionApi.createTransaction(token, transaction.toDto())
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
