package com.example.expense_tracking_project.screens.expenseTracking.data.data_source

import com.example.expense_tracking_project.core.local.entities.Category

interface DataSource {
    suspend fun insertCategory(category: Category)
    suspend fun getAllCategories(): List<Category>
    suspend fun getCategoryById(id: Int): Category?
    suspend fun updateCategory(category: Category)
    suspend fun clearAndInsert(categories: List<Category>)
}