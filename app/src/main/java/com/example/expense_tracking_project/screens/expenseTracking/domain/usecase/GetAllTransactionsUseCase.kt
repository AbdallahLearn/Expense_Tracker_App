package com.example.expense_tracking_project.screens.expenseTracking.domain.usecase



import com.example.expense_tracking_project.core.local.entities.Transaction
import com.example.expense_tracking_project.screens.expenseTracking.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow

class GetAllTransactionsUseCase(private val repository: TransactionRepository) {
    suspend operator fun invoke(): Flow<List<Transaction>> {
        return repository.allTransactions()
    }
}