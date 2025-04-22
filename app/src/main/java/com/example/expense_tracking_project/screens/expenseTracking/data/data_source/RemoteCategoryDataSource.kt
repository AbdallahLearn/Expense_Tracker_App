package com.example.expense_tracking_project.screens.expenseTracking.data.data_source

import com.example.expense_tracking_project.core.local.entities.Category


interface RemoteCategoryDataSource {
    suspend fun insertCategory(category: Category)
}