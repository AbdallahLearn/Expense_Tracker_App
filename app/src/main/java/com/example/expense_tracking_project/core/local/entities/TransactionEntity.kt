package com.example.expense_tracking_project.core.local.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
    tableName = "transactions",
    indices = [Index("categoryId")]
)
data class Transaction(
    @PrimaryKey(autoGenerate = true)
    val transactionId: Int = 0,

    //   val categoryId: Int,
    val categoryId: Int? = null, // make FK after screen add category
    val amount: Double,
    val date: Date,
    val note: String,
    val isDeleted: Boolean = false,
    val createdAt: Date,
    val updatedAt: Date
)