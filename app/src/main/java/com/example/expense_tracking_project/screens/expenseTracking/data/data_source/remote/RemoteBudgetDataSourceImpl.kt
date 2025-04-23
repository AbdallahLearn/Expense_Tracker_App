package com.example.expense_tracking_project.screens.expenseTracking.data.data_source.remote

import com.example.expense_tracking_project.core.local.entities.BudgetEntity
import com.example.expense_tracking_project.screens.expenseTracking.data.data_source.RemoteBudgetDataSource
import com.example.expense_tracking_project.screens.expenseTracking.data.mapper.toDto
import com.example.expense_tracking_project.screens.expenseTracking.data.mapper.toEntity
import com.example.expense_tracking_project.screens.expenseTracking.data.remote.BudgetApi
import javax.inject.Inject

class RemoteBudgetDataSourceImpl @Inject constructor(
    private val budgetApi: BudgetApi
) : RemoteBudgetDataSource {
    override suspend fun insertBudget(budget: BudgetEntity) {
        budgetApi.createBudget(budget.toDto())
    }

    override suspend fun getBudgets(): List<BudgetEntity> {
        val response = budgetApi.getBudgets()
        return response.body()?.budgets?.map { it.toEntity() } ?: emptyList()
    }
}
