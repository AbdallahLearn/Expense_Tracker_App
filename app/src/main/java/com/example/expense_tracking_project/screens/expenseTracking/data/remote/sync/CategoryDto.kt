package com.example.expense_tracking_project.screens.expenseTracking.data.remote.sync

data class CategoryDto(
    val id: String,
    val name: String,
    val color: String,
    val userId: String,
    val icon: String?,
    val budget_limit: Double?
)