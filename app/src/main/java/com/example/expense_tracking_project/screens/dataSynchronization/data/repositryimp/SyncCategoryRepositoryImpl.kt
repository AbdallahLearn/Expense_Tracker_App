package com.example.expense_tracking_project.screens.dataSynchronization.data.repositryimp

import android.util.Log
import com.example.expense_tracking_project.core.local.dao.CategoryDao
import com.example.expense_tracking_project.core.local.entities.Category
import com.example.expense_tracking_project.screens.dataSynchronization.domain.repository.SyncCategoryRepository
import com.example.expense_tracking_project.screens.expenseTracking.data.mapper.toDto
import com.example.expense_tracking_project.screens.expenseTracking.data.mapper.toEntity
import com.example.expense_tracking_project.screens.expenseTracking.data.remote.sync.CategoryApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Date
import javax.inject.Inject

class SyncCategoryRepositoryImpl @Inject constructor(
    private val categoryDao: CategoryDao,
    private val categoryApi: CategoryApi
) : SyncCategoryRepository {

    override suspend fun syncCategory(): Unit = withContext(Dispatchers.IO) {
        val unSyncedCategory = categoryDao.getUnsyncedCategories()

        unSyncedCategory.forEach { category ->
            try {
                val response = categoryApi.createCategory(category.toDto())

                if (response.isSuccessful) {
                    val returnedCategory = response.body()
                    val serverId = returnedCategory?.id

                    if (!serverId.isNullOrBlank()) {
                        val updatedCategory = category.copy(
                            isSynced = true,
                            categoryServerId = serverId,
                            updatedAt = Date()
                        )
                        categoryDao.insertOrReplaceCategory(updatedCategory)

                        Log.d("SYNC", "Category synced: $updatedCategory")
                    } else {
                        Log.w("SYNC", "⚠Server did not return ID for category '${category.categoryName}'")
                    }
                } else {
                    Log.e("SYNC", "Sync failed for '${category.categoryName}': ${response.code()}")
                }

            } catch (e: Exception) {
                Log.e("SYNC", "Exception syncing category '${category.categoryName}': ${e.message}")
            }
        }
    }

    override suspend fun getCategoryFromApi(): List<Category> = withContext(Dispatchers.IO) {
        try {
            val response = categoryApi.getCategory()

            if (response.isSuccessful) {
                val remoteCategories = response.body()?.categories?.map { it.toEntity() } ?: emptyList()

                remoteCategories.forEach { remoteCategory ->
                    val existing = remoteCategory.categoryServerId?.let {
                        categoryDao.getCategoryByServerId(it)
                    }

                    if (existing == null) {
                        categoryDao.insertCategory(remoteCategory)
                        Log.d("SYNC", "Inserted remote category '${remoteCategory.categoryName}'")
                    } else {
                        val updated = remoteCategory.copy(categoryId = existing.categoryId)
                        categoryDao.updateCategory(updated)
                        Log.d("SYNC", "Updated category '${remoteCategory.categoryName}'")
                    }
                }

                Log.d("SYNC", "✅ Synced ${remoteCategories.size} categories from server")
                return@withContext remoteCategories

            } else {
                Log.e("SYNC", "Failed to fetch categories: ${response.code()}")
                emptyList()
            }
        } catch (e: Exception) {
            Log.e("SYNC", "Exception fetching categories: ${e.message}")
            emptyList()
        }
    }
}