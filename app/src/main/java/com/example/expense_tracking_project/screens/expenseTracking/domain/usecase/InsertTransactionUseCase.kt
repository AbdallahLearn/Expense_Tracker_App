package com.example.expense_tracking_project.screens.expenseTracking.domain.usecase

import com.example.expense_tracking_project.core.local.entities.Transaction
import com.example.expense_tracking_project.screens.expenseTracking.domain.repository.TransactionRepository


class InsertTransactionUseCase(private val repository: TransactionRepository) {
    suspend operator fun invoke(transaction: Transaction) {
        repository.insert(transaction)
    }
}