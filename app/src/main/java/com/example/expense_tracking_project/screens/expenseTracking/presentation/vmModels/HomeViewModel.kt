package com.example.expense_tracking_project.screens.expenseTracking.presentation.vmModels

import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expense_tracking_project.core.local.entities.Transaction
import com.example.expense_tracking_project.screens.expenseTracking.domain.usecase.transactionsusecase.GetAllTransactionsUseCase
import com.example.expense_tracking_project.screens.expenseTracking.domain.usecase.transactionsusecase.UpdateTransactionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import kotlinx.coroutines.flow.combine

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getAllTransactionsUseCase: GetAllTransactionsUseCase,
    private val updateTransactionUseCase: UpdateTransactionUseCase
) : ViewModel() {

    // State to hold the list of transactions
//    val transactions = mutableStateOf<List<Transaction>>(emptyList())
    private val allTransactions = MutableStateFlow<List<Transaction>>(emptyList())

    val income = mutableDoubleStateOf(0.0)
    val expenses = mutableDoubleStateOf(0.0)

    private val _searchText = MutableStateFlow("")
    val searchText: StateFlow<String> = _searchText

    val transactions: Flow<List<Transaction>> = combine(
        allTransactions,
        _searchText
    ) { transactions, search ->
        if (search.isBlank()) {
            transactions
        } else {
            val lowerSearch = search.lowercase()
            transactions.filter { txn ->
                txn.note.lowercase().contains(lowerSearch) ||
                        txn.amount.toString().contains(lowerSearch)
            }
        }
    }

    init {
        loadTransactions()
    }

    private fun loadTransactions() {
        viewModelScope.launch {
            getAllTransactionsUseCase().collect { all ->
                val filtered = all.filter { !it.isDeleted }
                allTransactions.value = filtered

                // Calculate income & expenses
                income.doubleValue = filtered
                    .filter { it.amount >= 0 }
                    .sumOf { it.amount }

                expenses.doubleValue = filtered
                    .filter { it.amount < 0 }
                    .sumOf { -it.amount }
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

    fun updateSearch(text: String) {
        _searchText.value = text
    }
}