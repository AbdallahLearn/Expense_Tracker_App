package com.example.expense_tracking_project.screens.expenseTracking.data.remote

data class TransactionDto(
    val amount: Double,
    val category_id: String? = null,
    val date: String,
)