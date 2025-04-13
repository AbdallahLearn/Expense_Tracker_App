package com.example.expense_tracking_project.data.dataSource.Transaction

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Insert
import androidx.room.Update


@Dao
interface TransactionDao {

    @Query("SELECT * FROM transactions WHERE isDeleted = 0 ORDER BY date DESC")
    fun getAllTransactions(): LiveData<List<Transaction>>

    @Insert
    suspend fun insert(transaction: Transaction)

    @Update
    suspend fun update(transaction: Transaction)
}

