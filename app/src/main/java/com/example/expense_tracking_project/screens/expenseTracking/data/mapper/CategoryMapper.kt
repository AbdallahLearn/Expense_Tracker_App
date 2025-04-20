package com.example.expense_tracking_project.screens.expenseTracking.data.mapper

import com.example.expense_tracking_project.core.local.entities.Category
import com.example.expense_tracking_project.screens.expenseTracking.data.remote.sync.CategoryDto




fun Category.toDto(): CategoryDto {
    return CategoryDto(
        name = categoryName,
        color = color
    )
}

