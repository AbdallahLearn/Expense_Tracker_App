package com.example.expense_tracking_project.core.local.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
    tableName = "BudgetEntity",
    indices = [Index("budgetId"),
        Index(value = ["totalAmount"], unique = true)]
)
data class BudgetEntity(
    @PrimaryKey(autoGenerate = true)
    val budgetId: Int = 0,

    val name: String,
    val totalAmount: Double,

    val startDate: Date,
    val endDate: Date,

    val isDeleted: Boolean = false,
    val isSynced: Boolean = false, // add flag
    val createdAt: Date,
    val updatedAt: Date
)