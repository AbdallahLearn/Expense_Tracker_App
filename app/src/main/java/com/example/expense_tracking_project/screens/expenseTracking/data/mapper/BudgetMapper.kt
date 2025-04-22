package com.example.expense_tracking_project.screens.expenseTracking.data.mapper

import com.example.expense_tracking_project.core.local.entities.BudgetEntity
import com.example.expense_tracking_project.screens.expenseTracking.data.remote.BudgetDto
import java.text.SimpleDateFormat
import java.util.*

// Mapping from Room Entity -> DTO for API
fun BudgetEntity.toDto(): BudgetDto {
    return BudgetDto(
        name = name,
        amount = totalAmount,
        category_id = null, // not used now
        start_date = startDate.toSimpleDate(),
        end_date = endDate.toSimpleDate()
    )
}

// Helper: Date → yyyy-MM-dd
fun Date.toSimpleDate(): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    sdf.timeZone = TimeZone.getTimeZone("UTC")
    return sdf.format(this)
}
// Mapping from API to Entity
fun BudgetDto.toEntity(): BudgetEntity{
    val now = Date()
    return BudgetEntity(
        name = name,
        totalAmount = amount,
        startDate = start_date.toDate(),
        endDate = end_date.toDate(),
        createdAt = now,
        updatedAt = now,
        isSynced = true
    )
}
// String -> Date
fun String.toDate(): Date {
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    sdf.timeZone = TimeZone.getTimeZone("UTC")
    return sdf.parse(this) ?: Date()
}

