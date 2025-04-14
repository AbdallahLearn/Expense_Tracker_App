package com.example.expense_tracking_project.domain.usecase.transaction

import com.example.expense_tracking_project.data.dataSource.Transaction.Transaction
import com.example.expense_tracking_project.domain.repository.transaction.TransactionRepository

class InsertTransactionUseCase(private val repository: TransactionRepository) {
    suspend operator fun invoke(transaction: Transaction) {
        repository.insert(transaction)
    }
}

