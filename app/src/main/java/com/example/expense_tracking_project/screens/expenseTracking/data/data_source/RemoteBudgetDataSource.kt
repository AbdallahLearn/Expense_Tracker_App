package com.example.expense_tracking_project.screens.expenseTracking.data.data_source

import com.example.expense_tracking_project.core.local.entities.BudgetEntity

interface RemoteBudgetDataSource {
    suspend fun insertBudget(budget: BudgetEntity)
    suspend fun getBudgets(): List<BudgetEntity>
}
