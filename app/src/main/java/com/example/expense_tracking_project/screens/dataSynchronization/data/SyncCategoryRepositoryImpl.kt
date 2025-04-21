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
          "Bearer eyJhbGciOiJSUzI1NiIsImtpZCI6IjkwOTg1NzhjNDg4MWRjMDVlYmYxOWExNWJhMjJkOGZkMWFiMzRjOGEiLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJodHRwczovL3NlY3VyZXRva2VuLmdvb2dsZS5jb20vZXhwZW5zZS10cmFja2VyLTc4YjA3IiwiYXVkIjoiZXhwZW5zZS10cmFja2VyLTc4YjA3IiwiYXV0aF90aW1lIjoxNzQ1MjMyMTI0LCJ1c2VyX2lkIjoiTDZEZkdUUnV5M1FOYW9RcGlzeHlDWWFQU1psMiIsInN1YiI6Ikw2RGZHVFJ1eTNRTmFvUXBpc3h5Q1lhUFNabDIiLCJpYXQiOjE3NDUyMzIxMjQsImV4cCI6MTc0NTIzNTcyNCwiZW1haWwiOiJmYXJlZWhhQGF0b21jYW1wLmNvbSIsImVtYWlsX3ZlcmlmaWVkIjpmYWxzZSwiZmlyZWJhc2UiOnsiaWRlbnRpdGllcyI6eyJlbWFpbCI6WyJmYXJlZWhhQGF0b21jYW1wLmNvbSJdfSwic2lnbl9pbl9wcm92aWRlciI6InBhc3N3b3JkIn19.R00ty9JDZgblG1zSlekQ3jk5Y_yAcGNnkKjKIgPzn0SHBNzh7zgqhizbdF25NOIvDApwx6ZczSIQh_aAPHqMtONjQs6oitdYpjwM2iqIIULDEJuLRytRZZkXlcFP99je9mos5WqRIUXoigCUFWE9WCU6yrXr3EAotPlJeAfgMIo8Towd9RU1Vri6nSPMkdZy0WxH6Eya4VnwUejYczJ7xLcNMyYLsdoHpH3_b6r2gv41Ci732ltHxuSK0v5YSrcDTCqTaihyQbFJ7oTbqEGm0vml0rF0-wdh-myL5m_Qly46TBdHO9V8gyVkiuit9pfjdbmFMY0yIkHnkCKpGcA4jQ"
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