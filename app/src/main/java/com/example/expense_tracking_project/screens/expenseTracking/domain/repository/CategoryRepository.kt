package com.example.expense_tracking_project.screens.expenseTracking.domain.repository

import com.example.expense_tracking_project.core.local.entities.Category

interface CategoryRepository {
    suspend fun insertCategory(category: Category)
    suspend fun getAllCategories(): List<Category>
    suspend fun softDeleteCategory(categoryId: Int)

}