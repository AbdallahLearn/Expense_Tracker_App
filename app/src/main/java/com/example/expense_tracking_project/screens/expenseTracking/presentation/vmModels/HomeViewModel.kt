package com.example.expense_tracking_project.screens.expenseTracking.presentation.vmModels

import androidx.compose.runtime.mutableDoubleStateOf
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

enum class TransactionTypeFilter {
    ALL, INCOME, EXPENSES
}

enum class TimeFilter {
    TODAY, WEEK, MONTH, YEAR
}

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getAllTransactionsUseCase: GetAllTransactionsUseCase,
    private val updateTransactionUseCase: UpdateTransactionUseCase,
) : ViewModel() {

    // State to hold the list of transactions
    private val allTransactions = MutableStateFlow<List<Transaction>>(emptyList())

    val income = mutableDoubleStateOf(0.0)
    val expenses = mutableDoubleStateOf(0.0)

    private val _searchText = MutableStateFlow("")
    val searchText: StateFlow<String> = _searchText

    private val _typeFilter = MutableStateFlow(TransactionTypeFilter.ALL)
    val typeFilter: StateFlow<TransactionTypeFilter> = _typeFilter

    private val _timeFilter = MutableStateFlow(TimeFilter.TODAY)
    val timeFilter: StateFlow<TimeFilter> = _timeFilter

    fun updateTypeFilter(type: TransactionTypeFilter) {
        _typeFilter.value = type
    }

    fun updateTimeFilter(filter: TimeFilter) {
        _timeFilter.value = filter
    }

    val transactions: Flow<List<Transaction>> = combine(
        allTransactions,
        _searchText,
        _typeFilter,
        _timeFilter
    ) { transactions, search, filter, timeFilter ->

        val now = Date()
        val calendar = java.util.Calendar.getInstance()

        val filteredByTime = transactions.filter { txn ->
            val txnDate = txn.date

            when (timeFilter) {
                TimeFilter.TODAY -> {
                    calendar.time = now
                    val todayYear = calendar.get(java.util.Calendar.YEAR)
                    val todayDay = calendar.get(java.util.Calendar.DAY_OF_YEAR)

                    calendar.time = txnDate
                    todayYear == calendar.get(java.util.Calendar.YEAR) &&
                            todayDay == calendar.get(java.util.Calendar.DAY_OF_YEAR)
                }

                TimeFilter.WEEK -> {
                    calendar.time = now
                    val currentWeek = calendar.get(java.util.Calendar.WEEK_OF_YEAR)
                    val currentYear = calendar.get(java.util.Calendar.YEAR)

                    calendar.time = txnDate
                    currentWeek == calendar.get(java.util.Calendar.WEEK_OF_YEAR) &&
                            currentYear == calendar.get(java.util.Calendar.YEAR)
                }

                TimeFilter.MONTH -> {
                    calendar.time = now
                    val currentMonth = calendar.get(java.util.Calendar.MONTH)
                    val currentYear = calendar.get(java.util.Calendar.YEAR)

                    calendar.time = txnDate
                    currentMonth == calendar.get(java.util.Calendar.MONTH) &&
                            currentYear == calendar.get(java.util.Calendar.YEAR)
                }

                TimeFilter.YEAR -> {
                    calendar.time = now
                    val currentYear = calendar.get(java.util.Calendar.YEAR)

                    calendar.time = txnDate
                    currentYear == calendar.get(java.util.Calendar.YEAR)
                }
            }
        }

        val filteredByType = filteredByTime.filter { txn ->
            when (filter) {
                TransactionTypeFilter.ALL -> true
                TransactionTypeFilter.INCOME -> txn.amount >= 0
                TransactionTypeFilter.EXPENSES -> txn.amount < 0
            }
        }

        val filteredBySearch = filteredByType.filter { txn ->
            search.isBlank() || txn.note.lowercase().contains(search.lowercase()) ||
                    txn.amount.toString().contains(search)
        }

        filteredBySearch.sortedByDescending { it.createdAt }
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