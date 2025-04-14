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
            parentColumns = ["budget_id"],
            childColumns = ["budget_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("budget_id")]
)
data class Category(
    @PrimaryKey(autoGenerate = true)
    val categoryId: Int,
    val budget_id: Int,
    val category_name: String,
    val type: String, // "income" or "expense"
    val isDeleted: Boolean,
    val created_at: Date,
    val updated_at: Date
)



