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
        val token =
        "Bearer eyJhbGciOiJSUzI1NiIsImtpZCI6IjkwOTg1NzhjNDg4MWRjMDVlYmYxOWExNWJhMjJkOGZkMWFiMzRjOGEiLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJodHRwczovL3NlY3VyZXRva2VuLmdvb2dsZS5jb20vZXhwZW5zZS10cmFja2VyLTc4YjA3IiwiYXVkIjoiZXhwZW5zZS10cmFja2VyLTc4YjA3IiwiYXV0aF90aW1lIjoxNzQ1MzAyMDE1LCJ1c2VyX2lkIjoiTDZEZkdUUnV5M1FOYW9RcGlzeHlDWWFQU1psMiIsInN1YiI6Ikw2RGZHVFJ1eTNRTmFvUXBpc3h5Q1lhUFNabDIiLCJpYXQiOjE3NDUzMDIwMTUsImV4cCI6MTc0NTMwNTYxNSwiZW1haWwiOiJmYXJlZWhhQGF0b21jYW1wLmNvbSIsImVtYWlsX3ZlcmlmaWVkIjpmYWxzZSwiZmlyZWJhc2UiOnsiaWRlbnRpdGllcyI6eyJlbWFpbCI6WyJmYXJlZWhhQGF0b21jYW1wLmNvbSJdfSwic2lnbl9pbl9wcm92aWRlciI6InBhc3N3b3JkIn19.W1AFjk91VvHQTATSFpIa8YNyc8rX1igMiFoCjB8RlYoxTsH_ey3AzBuGUaA56W-r_-pIo2RAUfIqQdZM-KrjrcItmbgHg596Zfev1uQrHY6J-oF9BMaTOVi70Nr8mAzANMn_tPT_XytJBGMbsUlP1huJkcHJVIIAP1fxh9Z8jgwfDEnvtqIjL1vy_hCt9rJrU3st1QRR8DWvN2bCW68HHslVKg4S_KKWFfmFv11YohF1AmgU417ilKBEQOAW7zpVg2iDuxJzavLnS5niW9b7R2b9bm2QTLuAyAU7VQtFR7D0xLqx-vGD28RkdWgr3agWjxCV7crLi2rORpqI8rbvBQ"
        val unSyncedCategory = categoryDao.getUnsyncedCategories()
        unSyncedCategory.forEach { category ->
            try {
                val response = categoryApi.createCategory(token, category.toDto())
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