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

// Helper: yyyy-MM-dd → Date
fun String.toDate(): Date {
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    sdf.timeZone = TimeZone.getTimeZone("UTC")
    return sdf.parse(this)!!
}
