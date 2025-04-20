package com.example.expense_tracking_project.screens.expenseTracking.data.remote

data class BudgetDto(
    val name: String,
    val amount: Double,
    val category_id: String? = null,
    val start_date: String,
    val end_date: String
)