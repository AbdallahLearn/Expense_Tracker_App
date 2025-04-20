package com.example.expense_tracking_project.core.local.entities


import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
    tableName = "categories",
    foreignKeys = [
        ForeignKey(
            entity = BudgetEntity::class,
            parentColumns = ["budgetId"],
            childColumns = ["budgetId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("budgetId")]
)
data class Category(
    @PrimaryKey(autoGenerate = true)
    val categoryId: Int,
    val budgetId: Int?,
    val categoryName: String,
    val type: String, // "income" or "expense"
    val color: String,
    val isDeleted: Boolean,
    val isSynced: Boolean = false , // add flag
    val createdAt: Date,
    val updatedAt: Date
)



