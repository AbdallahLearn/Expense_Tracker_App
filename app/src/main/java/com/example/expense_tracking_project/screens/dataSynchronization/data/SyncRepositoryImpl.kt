package com.example.expense_tracking_project.screens.dataSynchronization.data

import android.util.Log
import com.example.expense_tracking_project.core.local.dao.BudgetDao
import com.example.expense_tracking_project.screens.dataSynchronization.domain.repository.SyncRepository
import com.example.expense_tracking_project.screens.expenseTracking.data.mapper.toDto
import com.example.expense_tracking_project.screens.expenseTracking.data.remote.BudgetApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SyncRepositoryImpl @Inject constructor(
    private val budgetDao: BudgetDao, // local room database
    private val budgetApi: BudgetApi // remote server
) : SyncRepository {

    override suspend fun syncBudgets(): Unit = withContext(Dispatchers.IO) {
        val token =
            "Bearer eyJhbGciOiJSUzI1NiIsImtpZCI6Ijg1NzA4MWNhOWNiYjM3YzIzNDk4ZGQzOTQzYmYzNzFhMDU4ODNkMjgiLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJodHRwczovL3NlY3VyZXRva2VuLmdvb2dsZS5jb20vZXhwZW5zZS10cmFja2VyLTc4YjA3IiwiYXVkIjoiZXhwZW5zZS10cmFja2VyLTc4YjA3IiwiYXV0aF90aW1lIjoxNzQ1MTQxMzk2LCJ1c2VyX2lkIjoiTDZEZkdUUnV5M1FOYW9RcGlzeHlDWWFQU1psMiIsInN1YiI6Ikw2RGZHVFJ1eTNRTmFvUXBpc3h5Q1lhUFNabDIiLCJpYXQiOjE3NDUxNDEzOTYsImV4cCI6MTc0NTE0NDk5NiwiZW1haWwiOiJmYXJlZWhhQGF0b21jYW1wLmNvbSIsImVtYWlsX3ZlcmlmaWVkIjpmYWxzZSwiZmlyZWJhc2UiOnsiaWRlbnRpdGllcyI6eyJlbWFpbCI6WyJmYXJlZWhhQGF0b21jYW1wLmNvbSJdfSwic2lnbl9pbl9wcm92aWRlciI6InBhc3N3b3JkIn19.WDDJAKDyuY6XZAVnHE0Z645GEWrgOeg8F_aQ4JwTgp79kd1AXa7dHYMNG-j2INip7e_9d8CCIT71cMM_MFArHvDfn7tsg1d-gp-nyzp3CnYGieWKKpRBbSRW5uhatX6gD2vToEJXeefeVwJ23RlpSbNmr4lw8_Xj8UaxpZTVC8I9L8K14stJ3_I9cQT3AK4E19CjbJqS-ulsTv-HQ1n_E_TASuhxGuljmdG28ZQtIqBIm6cJHQWQQrGRayHJ0Gbx07-bJXA8aIu62NDqYlQKl7FY3TFxtvUC9ZmPvjFk3MYoPa6TbWsiGLhCFD3sC5dipSvWWL_XmJgwgUVO271uvg"
        val unSyncedBudgets =
            budgetDao.getUnsyncedBudgets() // upload unsynced local budgets ( by fetch all budgets where is isSynced = 0 )
        unSyncedBudgets.forEach { budget -> // each entity convert
            try {
                val response = budgetApi.createBudget(
                    token, budget.toDto()
                )  // It converts BudgetEntity to BudgetDto
                if (response.isSuccessful) {
                    Log.d("SYNC", "Budget '${budget.name}' synced to server")
                    Log.d("SYNC", "Fetched budgets: ${response.body()}")
                    budgetDao.markBudgetAsSynced(budget.budgetId) // if it is sync marks synced
                } else {
                    Log.e("SYNC", "Failed to sync '${budget.name}': ${response.code()}")
                }
            } catch (e: Exception) {
                // Optionally log error or handle retry
                e.printStackTrace()
            }
        }
    }
}
