package com.example.expense_tracking_project.screens.dataSynchronization.data

import android.util.Log
import com.example.expense_tracking_project.core.local.dao.CategoryDao
import com.example.expense_tracking_project.screens.dataSynchronization.domain.repository.SyncCategoryRepository
import com.example.expense_tracking_project.screens.expenseTracking.data.mapper.toDto
import com.example.expense_tracking_project.screens.expenseTracking.data.remote.sync.CategoryApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject


class SyncCategoryRepositoryImpl @Inject constructor(
    private val categoryDao: CategoryDao, // local room database
    private val categoryApi: CategoryApi // remote server
) : SyncCategoryRepository {

    override suspend fun syncCategory(): Unit = withContext(Dispatchers.IO) {
         val unSyncedCategory = categoryDao.getUnsyncedCategories()
        unSyncedCategory.forEach { category ->
            try {
                val response = categoryApi.createCategory(category.toDto())
                if (response.isSuccessful) {
                    Log.d("SYNC", "Category '${category.categoryName}' synced to server")
                    Log.d("SYNC", "Fetched Category: ${response.body()}")
                    categoryDao.markCategoriesAsSynced(category.categoryId)
                } else {
                    Log.e("SYNC", "Failed to sync '${category.categoryName}': ${response.code()}")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}