package com.example.expense_tracking_project.screens.expenseTracking.domain.usecase.transactionsusecase

import com.example.expense_tracking_project.screens.expenseTracking.domain.repository.TransactionRepository
import javax.inject.Inject

class SoftDeleteTransactionUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {
    suspend operator fun invoke(transactionId: Int) {
        transactionRepository.softDeleteTransaction(transactionId)
    }
}