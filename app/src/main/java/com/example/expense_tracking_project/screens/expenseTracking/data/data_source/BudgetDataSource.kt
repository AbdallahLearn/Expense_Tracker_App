package com.example.expense_tracking_project.screens.expenseTracking.data.data_source

import com.example.expense_tracking_project.core.local.entities.BudgetEntity

interface BudgetDataSource {
    suspend fun getBudgets(): List<BudgetEntity>
    suspend fun insertBudget(budget: BudgetEntity)
    suspend fun updateBudget(budget: BudgetEntity)
    suspend fun clearAndInsert(budgets: List<BudgetEntity>)
    suspend fun getBudgetById(id: Int): BudgetEntity?

}
