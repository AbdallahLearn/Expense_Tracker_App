package com.example.expense_tracking_project.screens.dataSynchronization.data.repositryimp

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
        val unSyncedBudgets =
            budgetDao.getUnsyncedBudgets() // upload unsynced local budgets ( by fetch all budgets where is isSynced = 0 )

        unSyncedBudgets.forEach { budget -> // each entity convert
            try {
                val response = budgetApi.createBudget(
                    budget.toDto()
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

    override suspend fun getBudgetsFromApi(): List<BudgetEntity> {
        return withContext(Dispatchers.IO) {
            try {
                val response = budgetApi.getBudgets()
                if (response.isSuccessful) {
                    val remoteBudgets =
                        response.body()?.budgets?.map { it.toEntity() } ?: emptyList()
                    remoteBudgets.forEach { budget ->
                        val existing = budgetDao.getBudgetByTotalAmount(budget.totalAmount)
                        if (existing != null) {
                            val updated = budget.copy(totalAmount = existing.totalAmount)
                            budgetDao.updateBudget(updated)
                        } else {
                            budgetDao.insertBudget(budget)
                        }
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
