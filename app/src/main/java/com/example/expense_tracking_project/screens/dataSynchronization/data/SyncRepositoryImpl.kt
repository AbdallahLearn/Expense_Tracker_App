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
            "Bearer eyJhbGciOiJSUzI1NiIsImtpZCI6IjkwOTg1NzhjNDg4MWRjMDVlYmYxOWExNWJhMjJkOGZkMWFiMzRjOGEiLCJ0eXAiOiJKV1QifQ.eyJuYW1lIjoiYWJlZXIiLCJpc3MiOiJodHRwczovL3NlY3VyZXRva2VuLmdvb2dsZS5jb20vZXhwZW5zZS10cmFja2VyLTc4YjA3IiwiYXVkIjoiZXhwZW5zZS10cmFja2VyLTc4YjA3IiwiYXV0aF90aW1lIjoxNzQ1MzEyODYxLCJ1c2VyX2lkIjoiaDRCYmNoWm5NMVpqaVFCWHNBTXRyd3l1dk52MiIsInN1YiI6Img0QmJjaFpuTTFaamlRQlhzQU10cnd5dXZOdjIiLCJpYXQiOjE3NDUzMTI4NjEsImV4cCI6MTc0NTMxNjQ2MSwiZW1haWwiOiJhYnJhQGdtYWlsLmNvbSIsImVtYWlsX3ZlcmlmaWVkIjpmYWxzZSwiZmlyZWJhc2UiOnsiaWRlbnRpdGllcyI6eyJlbWFpbCI6WyJhYnJhQGdtYWlsLmNvbSJdfSwic2lnbl9pbl9wcm92aWRlciI6InBhc3N3b3JkIn19.EofkPu7vKUkuGlwT9rzBAIzSDjBPUlhJ9BxEKrP0tzmZYBVi7kuXLrTajyhVGPrX_nCF-VRS86YXpNM0PP6AVpClKrEEBr3gXPa1fvSuyicHWY2voDRFTTgdURbqSOpgcQzyNcppzsKNec1d3TH1r_ujiz48KCN7oCNDiBRKe21kHh5YhwLN7AMxPpVKSGwsem9Fw3FCbYU8dWP6tyI7R3vckJaoLyVoMid1T6Hov5ZU9u_Vbp2TL26vRpjAH1XgYU633ZxeR3Lnn1Oq2jMYsX7PH-3eG8ocGabQZRr4WBYc6mhC3m1y-q5CPl-UhRkhjHR_ZTd3I7Ku_DVNQOLE9Q"

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
