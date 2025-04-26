package com.example.expense_tracking_project.screens.expenseTracking.data.mapper

import com.example.expense_tracking_project.core.local.entities.Transaction
import com.example.expense_tracking_project.screens.expenseTracking.data.remote.TransactionDto
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

// Mapping from Room Entity -> DTO for API
fun Transaction.toDto(): TransactionDto {
    return TransactionDto(
        amount = amount,
        category_id = null,
        date = date.TransactiontoSimpleDate()
    )
}

fun Transaction.toDto(categoryServerId: String?): TransactionDto {
    return TransactionDto(
        amount = amount,
        category_id = categoryServerId,
        date = date.TransactiontoSimpleDate()
    )
}

// Helper: yyyy-MM-dd → Date
fun Date.TransactiontoSimpleDate(): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    sdf.timeZone = TimeZone.getTimeZone("UTC")
    return sdf.format(this)
}

// Mapping from API to Entity
fun TransactionDto.toEntity(categoryId: Int?): Transaction {
    val now = Date()
    return Transaction(
        transactionId = 0,
        amount = amount,
        date = date.toDate(),
        note = "",
        createdAt = now,
        updatedAt = now,
        isDeleted = false,
        isSynced = true,
        categoryId = categoryId
    )
}


// Helper: yyyy-MM-dd → Date
fun String.TransactiontoDate(): Date {
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    sdf.timeZone = TimeZone.getTimeZone("UTC")
    return requireNotNull(sdf.parse(this)) { "Invalid date format: $this" }
}