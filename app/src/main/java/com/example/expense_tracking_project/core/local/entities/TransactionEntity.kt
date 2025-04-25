package com.example.expense_tracking_project.core.local.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Relation
import java.util.Date

@Entity(
    tableName = "transactions",
    foreignKeys = [
        ForeignKey(
            entity = Category::class,
            parentColumns = ["categoryId"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("categoryId"),
        Index(value = ["amount", "date"], unique = true)
    ]
)
data class Transaction(
    @PrimaryKey(autoGenerate = true)
    val transactionId: Int = 0,

    val categoryId: Int?,

    val amount: Double,
    val date: Date,
    val note: String,

    val isDeleted: Boolean = false,
    val isSynced: Boolean = false, // add flag
    val createdAt: Date,
    val updatedAt: Date
)

data class TransactionWithCategory(
    @Embedded val transaction: Transaction,

    @Relation(
        parentColumn = "categoryId",
        entityColumn = "categoryId"
    )
    val category: Category?
)