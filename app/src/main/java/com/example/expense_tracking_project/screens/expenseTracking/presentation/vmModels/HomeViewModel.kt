package com.example.expense_tracking_project.screens.expenseTracking.presentation.vmModels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expense_tracking_project.core.local.entities.Transaction
import com.example.expense_tracking_project.screens.expenseTracking.domain.usecase.transactionsusecase.GetAllTransactionsUseCase
import com.example.expense_tracking_project.screens.expenseTracking.domain.usecase.transactionsusecase.UpdateTransactionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getAllTransactionsUseCase: GetAllTransactionsUseCase,
    private val updateTransactionUseCase: UpdateTransactionUseCase
) : ViewModel() {

    // State to hold the list of transactions
    val transactions = mutableStateOf<List<Transaction>>(emptyList())

    val income = mutableStateOf(0.0)
    val expenses = mutableStateOf(0.0)

    init {
        loadTransactions()
    }

    fun loadTransactions() {
        viewModelScope.launch {
            getAllTransactionsUseCase().collect { allTransactions ->
                val filteredTransactions = allTransactions.filter { !it.isDeleted }
                transactions.value = filteredTransactions

                // Calculate income & expenses
                income.value = filteredTransactions
                    .filter { it.amount >= 0 }
                    .sumOf { it.amount }

                expenses.value = filteredTransactions
                    .filter { it.amount < 0 }
                    .sumOf { it.amount * -1 } // negate since expenses are negative
            }
        }
    }

    fun softDeleteTransaction(transaction: Transaction) {
        viewModelScope.launch {
            val updated = transaction.copy(
                isDeleted = true,
                updatedAt = Date()
            )
            updateTransactionUseCase(updated)
            loadTransactions() // Refresh list
        }
    }

    fun formatDate(date: Date): String {
        val sdf = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        return sdf.format(date)
    }
}