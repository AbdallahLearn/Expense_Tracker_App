package com.example.expense_tracking_project.screens.dataSynchronization.data

import android.util.Log
import com.example.expense_tracking_project.core.local.dao.BudgetDao
import com.example.expense_tracking_project.core.local.entities.BudgetEntity
import com.example.expense_tracking_project.screens.dataSynchronization.domain.repository.SyncRepository
import com.example.expense_tracking_project.screens.expenseTracking.data.mapper.toDto
import com.example.expense_tracking_project.screens.expenseTracking.data.mapper.toEntity
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
          "Bearer eyJhbGciOiJSUzI1NiIsImtpZCI6IjkwOTg1NzhjNDg4MWRjMDVlYmYxOWExNWJhMjJkOGZkMWFiMzRjOGEiLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJodHRwczovL3NlY3VyZXRva2VuLmdvb2dsZS5jb20vZXhwZW5zZS10cmFja2VyLTc4YjA3IiwiYXVkIjoiZXhwZW5zZS10cmFja2VyLTc4YjA3IiwiYXV0aF90aW1lIjoxNzQ1MzAyMDE1LCJ1c2VyX2lkIjoiTDZEZkdUUnV5M1FOYW9RcGlzeHlDWWFQU1psMiIsInN1YiI6Ikw2RGZHVFJ1eTNRTmFvUXBpc3h5Q1lhUFNabDIiLCJpYXQiOjE3NDUzMDIwMTUsImV4cCI6MTc0NTMwNTYxNSwiZW1haWwiOiJmYXJlZWhhQGF0b21jYW1wLmNvbSIsImVtYWlsX3ZlcmlmaWVkIjpmYWxzZSwiZmlyZWJhc2UiOnsiaWRlbnRpdGllcyI6eyJlbWFpbCI6WyJmYXJlZWhhQGF0b21jYW1wLmNvbSJdfSwic2lnbl9pbl9wcm92aWRlciI6InBhc3N3b3JkIn19.W1AFjk91VvHQTATSFpIa8YNyc8rX1igMiFoCjB8RlYoxTsH_ey3AzBuGUaA56W-r_-pIo2RAUfIqQdZM-KrjrcItmbgHg596Zfev1uQrHY6J-oF9BMaTOVi70Nr8mAzANMn_tPT_XytJBGMbsUlP1huJkcHJVIIAP1fxh9Z8jgwfDEnvtqIjL1vy_hCt9rJrU3st1QRR8DWvN2bCW68HHslVKg4S_KKWFfmFv11YohF1AmgU417ilKBEQOAW7zpVg2iDuxJzavLnS5niW9b7R2b9bm2QTLuAyAU7VQtFR7D0xLqx-vGD28RkdWgr3agWjxCV7crLi2rORpqI8rbvBQ"
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

                e.printStackTrace()
            }
        }
    }

    override suspend fun getBudgetsFromApi(token: String): List<BudgetEntity> {
        return withContext(Dispatchers.IO) {
            try {
                val response = budgetApi.getBudgets(token)
                if (response.isSuccessful) {
                    val remoteBudgets = response.body()?.budgets?.map { it.toEntity() } ?: emptyList()
                    remoteBudgets.forEach { budget ->
                        budgetDao.insertBudget(budget)
                    }
                    Log.d("SYNC", "Fetched ${remoteBudgets.size} budgets from server")
                    remoteBudgets
                } else {
                    Log.e("SYNC", "Failed to fetch budgets: ${response.code()}")
                    emptyList()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                emptyList()
            }
        }
    }


}
