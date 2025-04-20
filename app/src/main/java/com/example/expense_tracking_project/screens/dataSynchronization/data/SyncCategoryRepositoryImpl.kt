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
            "Bearer eyJhbGciOiJSUzI1NiIsImtpZCI6IjkwOTg1NzhjNDg4MWRjMDVlYmYxOWExNWJhMjJkOGZkMWFiMzRjOGEiLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJodHRwczovL3NlY3VyZXRva2VuLmdvb2dsZS5jb20vZXhwZW5zZS10cmFja2VyLTc4YjA3IiwiYXVkIjoiZXhwZW5zZS10cmFja2VyLTc4YjA3IiwiYXV0aF90aW1lIjoxNzQ1MTcyMDY0LCJ1c2VyX2lkIjoiTDZEZkdUUnV5M1FOYW9RcGlzeHlDWWFQU1psMiIsInN1YiI6Ikw2RGZHVFJ1eTNRTmFvUXBpc3h5Q1lhUFNabDIiLCJpYXQiOjE3NDUxNzIwNjQsImV4cCI6MTc0NTE3NTY2NCwiZW1haWwiOiJmYXJlZWhhQGF0b21jYW1wLmNvbSIsImVtYWlsX3ZlcmlmaWVkIjpmYWxzZSwiZmlyZWJhc2UiOnsiaWRlbnRpdGllcyI6eyJlbWFpbCI6WyJmYXJlZWhhQGF0b21jYW1wLmNvbSJdfSwic2lnbl9pbl9wcm92aWRlciI6InBhc3N3b3JkIn19.fUJ0FjcM429TC8eV9qngrxSwXPB78ol9GOjeQJkQmLwpZyAGqsIdIlbD3Bo3-hJT3XISUHf76e3akPciE9pM1q2UYptZUxgt7PIueH-0pYbmPSQStwd3iQOBQxrmH8SEmgO8Z7xFksB6kgLW_mDWCt0aIYJ9CiYa2ETkWhS-NwwN-8_94hBonlyljyOSlyY5UeiNHi_nXuI_M52bpXbdG6S7CgZw8ZdGtz6ggXmucAbvJeFvwVuqtO0Jmsel6lIAjk7CKdKv-KCn-hEQbqDWfhCCms3gVIF_QAtLkuoV_p9n8C5aXXoMU2qBI8xEYxE2JPWygnSrPkOaS7dlXjickQ"
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