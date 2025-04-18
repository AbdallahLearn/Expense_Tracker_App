package com.example.expense_tracking_project.screens.expenseTracking.data.repositryimp

import com.example.expense_tracking_project.core.local.dao.CategoryDao
import com.example.expense_tracking_project.core.local.entities.Category
import com.example.expense_tracking_project.screens.expenseTracking.domain.repository.CategoryRepository
import java.util.Date
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
    private val categoryDao: CategoryDao
) : CategoryRepository {

    override suspend fun insertCategory(category: Category) {
        categoryDao.insertCategory(category)
    }
    override suspend fun getAllCategories(): List<Category> {
        return categoryDao.getAllCategories()
    }
    override suspend fun softDeleteCategory(categoryId: Int) {
        val category = categoryDao.getCategoryById(categoryId)
        if (category != null) {
            val updatedCategory = category.copy(
                isDeleted = true,
                updatedAt = Date()
            )
            categoryDao.updateCategory(updatedCategory)
        }
    }
}