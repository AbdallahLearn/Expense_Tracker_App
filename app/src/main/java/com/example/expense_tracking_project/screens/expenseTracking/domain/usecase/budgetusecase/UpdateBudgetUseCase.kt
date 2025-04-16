package com.example.expense_tracking_project.screens.expenseTracking.domain.usecase.budgetusecase

import com.example.expense_tracking_project.core.local.entities.BudgetEntity
import com.example.expense_tracking_project.screens.expenseTracking.domain.repository.BudgetRepository
import javax.inject.Inject


class UpdateBudgetUseCase @Inject constructor(private val repository: BudgetRepository) {
    suspend operator fun invoke(budget: BudgetEntity){
        return repository.updatebudget(budget)
    }
}