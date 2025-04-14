package com.example.expense_tracking_project.data.dataSource

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
    tableName = "transactions",
    indices = [Index("category_id")]
)
data class Transaction(
    @PrimaryKey(autoGenerate = true)
    val transaction_id: Int = 0,

    @ColumnInfo(name = "category_id")
    val categoryId: Int,

    val amount: Double,
    val date: Date,
    val note: String,
    val isDeleted: Boolean = false,
    val created_at: Date,
    val updated_at: Date
)

