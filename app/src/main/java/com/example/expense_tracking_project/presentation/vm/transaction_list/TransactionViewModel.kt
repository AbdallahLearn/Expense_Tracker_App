package com.example.expense_tracking_project.presentation.vm.transaction_list

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expense_tracking_project.data.dataSource.Transaction.Transaction
import com.example.expense_tracking_project.domain.usecase.transaction.GetAllTransactionsUseCase
import com.example.expense_tracking_project.domain.usecase.transaction.InsertTransactionUseCase
import com.example.expense_tracking_project.domain.usecase.transaction.UpdateTransactionUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Date

class TransactionViewModel(
    private val insertTransactionUseCase: InsertTransactionUseCase,
    private val updateTransactionUseCase: UpdateTransactionUseCase,
    private val getAllTransactionsUseCase: GetAllTransactionsUseCase
) : ViewModel() {

    private val _allTransactions = MutableStateFlow<List<Transaction>>(emptyList())
    val allTransactions: StateFlow<List<Transaction>> = _allTransactions

    var income by mutableDoubleStateOf(0.0)
    var expenses by mutableDoubleStateOf(0.0)

    var amountText by mutableStateOf("")
        private set

    var noteText by mutableStateOf("")
        private set

    fun onAmountChange(newAmount: String) {
        amountText = newAmount
    }

    fun onNoteChange(newNote: String) {
        noteText = newNote
    }

    init {

        viewModelScope.launch {
            getAllTransactionsUseCase().collect { transactions ->
                _allTransactions.value = transactions // Emit the transactions to StateFlow
                calculateIncomeAndExpenses(transactions)
            }
        }
    }

    private fun calculateIncomeAndExpenses(transactions: List<Transaction>) {
        income = transactions.filter { it.note.contains("income", ignoreCase = true) }
            .sumOf { it.amount }

        expenses = transactions.filter { it.note.contains("expense", ignoreCase = true) }
            .sumOf { it.amount }
    }

    fun insert(transaction: Transaction) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                insertTransactionUseCase(transaction)
                Log.d("TransactionInsert", "Transaction successfully inserted.")
            } catch (e: Exception) {
                Log.e("TransactionInsert", "Error inserting transaction: ${e.message}", e)
            }
        }
    }

    fun hideTransaction(transaction: Transaction) {
        viewModelScope.launch(Dispatchers.IO) {
            if (!transaction.isDeleted) {
                try {
                    val deletedTransaction = transaction.copy(
                        isDeleted = true, updated_at = Date()
                    )
                    updateTransactionUseCase(deletedTransaction)
                    Log.d("TransactionDelete", "Soft delete applied to transaction.")
                } catch (e: Exception) {
                    Log.e("TransactionDelete", "Error applying soft delete: ${e.message}", e)
                }
            }
        }
    }
}



