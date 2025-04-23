package com.example.expense_tracking_project.screens.expenseTracking.domain.usecase.transactionsusecase

import com.example.expense_tracking_project.core.local.entities.Transaction
import com.example.expense_tracking_project.screens.expenseTracking.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllTransactionsUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {
    suspend operator fun invoke(): Flow<List<Transaction>> {
        return transactionRepository.allTransactions()
    }

}

class GetTransactionByIdUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {
    suspend operator fun invoke(transactionId: Int): Transaction? {
        return transactionRepository.getTransactionById(transactionId)
    }
}