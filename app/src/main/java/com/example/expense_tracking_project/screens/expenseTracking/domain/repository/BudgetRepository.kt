package com.example.expense_tracking_project.screens.expenseTracking.domain.repository

import com.example.expense_tracking_project.core.local.entities.BudgetEntity

interface BudgetRepository {
    suspend fun getAllbudgets(): List<BudgetEntity>
    suspend fun insertbudget(budget: BudgetEntity)
    suspend fun updatebudget(budget: BudgetEntity)
}