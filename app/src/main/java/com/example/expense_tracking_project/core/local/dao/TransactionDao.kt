package com.example.expense_tracking_project.core.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update
import com.example.expense_tracking_project.core.local.entities.BudgetEntity
import com.example.expense_tracking_project.core.local.entities.Category
import com.example.expense_tracking_project.core.local.entities.Transaction
import com.example.expense_tracking_project.core.local.entities.TransactionWithCategory
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface TransactionDao {

    @Query("SELECT * FROM transactions WHERE isDeleted = 0 ORDER BY date DESC")
    fun getAllTransactions(): Flow<List<Transaction>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(transaction: Transaction)

    @Update
    suspend fun update(transaction: Transaction)

    @Delete
    suspend fun deleteTransaction(transaction: Transaction)

    @Query("SELECT * FROM transactions WHERE transactionId = :id")
    suspend fun geTransactionById(id: Int): Transaction?

    @Query("SELECT * FROM transactions WHERE isSynced = 0 AND isDeleted = 0")
    suspend fun getUnSyncedTransactions(): List<Transaction>

    @Query("UPDATE transactions SET isSynced = 1 WHERE transactionId = :id") //
    suspend fun markTransactionAsSynced(id: Int)

    @Query("UPDATE transactions SET isDeleted = 1, updatedAt = :updatedAt, isSynced = 0 WHERE transactionId = :transactionId") //so we can sync the delete request to server
    suspend fun softDeleteTransaction(transactionId: Int, updatedAt: Date)

    @Query("SELECT * FROM transactions WHERE transactionId = :transactionId")
    suspend fun getTransactionById(transactionId: Int): Transaction?

    @Query("DELETE FROM transactions WHERE isDeleted = 0")
    suspend fun clearTransactions()

    @Query("SELECT * FROM transactions WHERE amount = :amount AND date = :date")
    suspend fun getTransactionByAmount(amount: Double, date: Date): Transaction?

    
    @Query("SELECT * FROM transactions WHERE isDeleted = 0")
    fun getAllTransactionsWithCategory(): Flow<List<TransactionWithCategory>>

    @Insert
    suspend fun insertCategory(category: Category): Long

}