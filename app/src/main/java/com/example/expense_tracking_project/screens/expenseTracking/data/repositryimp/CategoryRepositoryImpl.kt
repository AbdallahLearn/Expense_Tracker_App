package com.example.expense_tracking_project.screens.expenseTracking.data.repositryimp

import com.example.expense_tracking_project.core.connectivity.NetworkConnectivityObserver
import com.example.expense_tracking_project.core.local.entities.Category
import com.example.expense_tracking_project.screens.dataSynchronization.domain.repository.SyncCategoryRepository
import com.example.expense_tracking_project.screens.expenseTracking.data.data_source.DataSource
import com.example.expense_tracking_project.screens.expenseTracking.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.first
import java.util.Date
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
    private val networkConnectivityObserver: NetworkConnectivityObserver,
    private val localDataSource: DataSource,
    private val remoteDataSource: DataSource,
    private val syncCategoryRepository: SyncCategoryRepository
) : CategoryRepository {

    override suspend fun insertCategory(category: Category) {
        val status = networkConnectivityObserver.observe().first()
        if (status == "Available") {
            remoteDataSource.insertCategory(category)

            syncCategoryRepository.syncCategory() // to get the server ID

            val updatedList = remoteDataSource.getAllCategories()
            localDataSource.clearAndInsert(updatedList)
        } else {
            localDataSource.insertCategory(category)
        }

    }

    override suspend fun getAllCategories(): List<Category> {
        return localDataSource.getAllCategories()
    }

    override suspend fun softDeleteCategory(categoryId: Int) {
        val category = localDataSource.getCategoryById(categoryId)
        if (category != null) {
            val updatedCategory = category.copy(
                isDeleted = true,
                updatedAt = Date()
            )
            localDataSource.updateCategory(updatedCategory)
        }
    }
}