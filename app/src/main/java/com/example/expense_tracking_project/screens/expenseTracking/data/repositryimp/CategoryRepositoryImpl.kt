package com.example.expense_tracking_project.screens.expenseTracking.data.repositryimp

import com.example.expense_tracking_project.core.connectivity.NetworkConnectivityObserver
import com.example.expense_tracking_project.core.local.dao.CategoryDao
import com.example.expense_tracking_project.core.local.entities.Category
import com.example.expense_tracking_project.screens.expenseTracking.data.data_source.DataSource
import com.example.expense_tracking_project.screens.expenseTracking.domain.repository.CategoryRepository
import java.util.Date
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
    private val networkConnectivityObserver: NetworkConnectivityObserver,
    private val categoryDao: CategoryDao,
    private val localDataSource: DataSource,
    private val remoteDataSource: DataSource,
) : CategoryRepository {

    override suspend fun insertCategory(category: Category) {
        networkConnectivityObserver.observe().collect { status ->
            if (status == "Available") {
                categoryDao.insertCategory(category)
            } else {
                categoryDao.insertCategory(category)
            }
        }
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