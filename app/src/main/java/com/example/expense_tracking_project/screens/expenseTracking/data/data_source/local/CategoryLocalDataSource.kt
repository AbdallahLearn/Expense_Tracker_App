package com.example.expense_tracking_project.screens.expenseTracking.data.data_source.local

import com.example.expense_tracking_project.core.local.dao.CategoryDao
import com.example.expense_tracking_project.core.local.entities.Category
import com.example.expense_tracking_project.screens.expenseTracking.data.data_source.DataSource
import javax.inject.Inject

class CategoryLocalDataSource @Inject constructor(
    private val categoryDao: CategoryDao,
) : DataSource {

    override suspend fun insertCategory(category: Category) = categoryDao.insertCategory(category)
    override suspend fun getAllCategories() = categoryDao.getAllCategories()
    override suspend fun getCategoryById(id: Int) = categoryDao.getCategoryById(id)
    override suspend fun updateCategory(category: Category) = categoryDao.updateCategory(category)
    override suspend fun clearAndInsert(categories: List<Category>) {
        categoryDao.clearCategories()
        categories.forEach { categoryDao.insertCategory(it) }
    }
}