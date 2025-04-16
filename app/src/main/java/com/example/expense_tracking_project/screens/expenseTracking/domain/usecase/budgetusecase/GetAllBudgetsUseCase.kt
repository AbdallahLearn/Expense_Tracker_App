package com.example.expense_tracking_project.screens.expenseTracking.domain.usecase.budgetusecase

import com.example.expense_tracking_project.core.local.entities.BudgetEntity
import com.example.expense_tracking_project.screens.expenseTracking.domain.repository.BudgetRepository
import kotlinx.coroutines.flow.Flow

class GetAllbudgetsUseCase(private val repository: BudgetRepository) {
    suspend operator fun invoke(): List<BudgetEntity> {
        return repository.getAllbudgets()
    }
}
