package com.example.expense_tracking_project.screens.expenseTracking.domain.usecase.transactionsusecase

import com.example.expense_tracking_project.core.local.entities.Transaction
import com.example.expense_tracking_project.screens.expenseTracking.domain.repository.TransactionRepository
import javax.inject.Inject


class UpdateTransactionUseCase @Inject constructor(private val repository: TransactionRepository) {
    suspend operator fun invoke(transaction: Transaction) {
        repository.update(transaction)
    }
}