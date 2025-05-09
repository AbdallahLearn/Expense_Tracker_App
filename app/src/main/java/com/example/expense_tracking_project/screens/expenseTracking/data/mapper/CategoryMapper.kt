package com.example.expense_tracking_project.screens.expenseTracking.data.mapper

import com.example.expense_tracking_project.core.local.entities.Category
import com.example.expense_tracking_project.screens.expenseTracking.data.remote.sync.CategoryDto
import java.util.Date

fun Category.toDto(): CategoryDto {
    return CategoryDto(
        id = categoryServerId ?: "",
        name = categoryName,
        color = color,
        userId = 0.toString(),
        icon = null,
        budget_limit = null
    )
}

fun CategoryDto.toEntity(): Category {
    val now = Date()
    return Category(
        categoryId = 0,
        categoryName = name,
        color = color,
        createdAt = now,
        updatedAt = now,
        isDeleted = false,
        isSynced = true,
        type = "Expense",
        budgetId = null,
        categoryServerId = id
    )
}