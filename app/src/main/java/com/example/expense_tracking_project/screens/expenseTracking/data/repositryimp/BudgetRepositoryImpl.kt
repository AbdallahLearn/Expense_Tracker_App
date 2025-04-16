package com.example.expense_tracking_project.screens.expenseTracking.data.repositryimp

import com.example.expense_tracking_project.core.local.dao.BudgetDao
import com.example.expense_tracking_project.core.local.entities.BudgetEntity
import com.example.expense_tracking_project.screens.expenseTracking.domain.repository.BudgetRepository
import kotlinx.coroutines.flow.Flow

class BudgetRepositoryImpl(private val budgetDao: BudgetDao) :
    BudgetRepository {
    override suspend fun getAllbudgets(): List<BudgetEntity> {
       return budgetDao.getAllBudgets()
    }

    override suspend fun insertbudget(budget: BudgetEntity) {
        return budgetDao.insertBudget(budget)
    }

    override suspend fun updatebudget(budget: BudgetEntity) {
        return budgetDao.updateBudget(budget)
    }
}