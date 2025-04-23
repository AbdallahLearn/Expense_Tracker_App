package com.example.expense_tracking_project.screens.expenseTracking.data.data_source.local

import com.example.expense_tracking_project.core.local.dao.BudgetDao
import com.example.expense_tracking_project.core.local.entities.BudgetEntity
import com.example.expense_tracking_project.screens.expenseTracking.data.data_source.BudgetDataSource
import javax.inject.Inject

class LocalBudgetDataSource @Inject constructor(
    private val budgetDao: BudgetDao
) : BudgetDataSource {
    override suspend fun getBudgets() = budgetDao.getAllBudgets()
    override suspend fun insertBudget(budget: BudgetEntity) = budgetDao.insertBudget(budget)
    override suspend fun updateBudget(budget: BudgetEntity) = budgetDao.updateBudget(budget)

    override suspend fun clearAndInsert(budgets: List<BudgetEntity>) {
        budgetDao.clearBudgets()
        budgets.forEach { budgetDao.insertBudget(it) }
    }

    override suspend fun getBudgetById(id: Int): BudgetEntity? {
        return budgetDao.getBudgetById(id)
    }

}