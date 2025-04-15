package com.example.expense_tracking_project.core.local.entities


import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "BudgetEntity")
data class BudgetEntity(
    @PrimaryKey(autoGenerate = true)
    val budgetId: Int = 0,

    val name: String,
    val totalAmount: Double,

    val startDate: Date,
    val endDate: Date,

    val isDeleted: Boolean = false,
    val createdAt: Date,
    val updatedAt: Date
)

