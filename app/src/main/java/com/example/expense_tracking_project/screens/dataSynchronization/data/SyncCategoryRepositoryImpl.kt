package com.example.expense_tracking_project.screens.dataSynchronization.data

import android.util.Log
import com.example.expense_tracking_project.core.local.dao.CategoryDao
import com.example.expense_tracking_project.core.local.entities.Category
import com.example.expense_tracking_project.screens.dataSynchronization.domain.repository.SyncCategoryRepository
import com.example.expense_tracking_project.screens.expenseTracking.data.mapper.toDto
import com.example.expense_tracking_project.screens.expenseTracking.data.mapper.toEntity
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
"Bearer eyJhbGciOiJSUzI1NiIsImtpZCI6IjkwOTg1NzhjNDg4MWRjMDVlYmYxOWExNWJhMjJkOGZkMWFiMzRjOGEiLCJ0eXAiOiJKV1QifQ.eyJuYW1lIjoiYWJlZXIiLCJpc3MiOiJodHRwczovL3NlY3VyZXRva2VuLmdvb2dsZS5jb20vZXhwZW5zZS10cmFja2VyLTc4YjA3IiwiYXVkIjoiZXhwZW5zZS10cmFja2VyLTc4YjA3IiwiYXV0aF90aW1lIjoxNzQ1MzEyODYxLCJ1c2VyX2lkIjoiaDRCYmNoWm5NMVpqaVFCWHNBTXRyd3l1dk52MiIsInN1YiI6Img0QmJjaFpuTTFaamlRQlhzQU10cnd5dXZOdjIiLCJpYXQiOjE3NDUzMTI4NjEsImV4cCI6MTc0NTMxNjQ2MSwiZW1haWwiOiJhYnJhQGdtYWlsLmNvbSIsImVtYWlsX3ZlcmlmaWVkIjpmYWxzZSwiZmlyZWJhc2UiOnsiaWRlbnRpdGllcyI6eyJlbWFpbCI6WyJhYnJhQGdtYWlsLmNvbSJdfSwic2lnbl9pbl9wcm92aWRlciI6InBhc3N3b3JkIn19.EofkPu7vKUkuGlwT9rzBAIzSDjBPUlhJ9BxEKrP0tzmZYBVi7kuXLrTajyhVGPrX_nCF-VRS86YXpNM0PP6AVpClKrEEBr3gXPa1fvSuyicHWY2voDRFTTgdURbqSOpgcQzyNcppzsKNec1d3TH1r_ujiz48KCN7oCNDiBRKe21kHh5YhwLN7AMxPpVKSGwsem9Fw3FCbYU8dWP6tyI7R3vckJaoLyVoMid1T6Hov5ZU9u_Vbp2TL26vRpjAH1XgYU633ZxeR3Lnn1Oq2jMYsX7PH-3eG8ocGabQZRr4WBYc6mhC3m1y-q5CPl-UhRkhjHR_ZTd3I7Ku_DVNQOLE9Q"
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

    override suspend fun getCategoryFromApi(token: String): List<Category> {
        val response = categoryApi.getCategory(token)
        val remoteCategory = response.body()?.categories?.map { it.toEntity() } ?: emptyList()

        return withContext(Dispatchers.IO) {
            try {
                if (response.isSuccessful) {
                    remoteCategory.forEach { category ->
                        categoryDao.insertCategory(category)
                    }
                    Log.d("SYNC", "Fetched ${remoteCategory.size} categories from server")
                    remoteCategory
                } else {
                    Log.e("SYNC", "Failed to fetch category: ${response.code()}")
                    emptyList()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                emptyList()
            }
        }
    }
}
