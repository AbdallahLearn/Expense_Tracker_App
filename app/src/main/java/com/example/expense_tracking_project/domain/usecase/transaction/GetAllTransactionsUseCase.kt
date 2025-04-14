package com.example.expense_tracking_project.domain.usecase.transaction

import com.example.expense_tracking_project.data.dataSource.Transaction.Transaction
import com.example.expense_tracking_project.domain.repository.transaction.TransactionRepository
import kotlinx.coroutines.flow.Flow

class GetAllTransactionsUseCase(private val repository: TransactionRepository) {
    suspend operator fun invoke(): Flow<List<Transaction>> {
        return repository.allTransactions()
    }
}

