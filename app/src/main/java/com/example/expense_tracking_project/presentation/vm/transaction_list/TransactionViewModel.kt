package com.example.expense_tracking_project.presentation.vm.transaction_list

import android.app.Application
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.expense_tracking_project.data.dataSource.AppDatabase
import com.example.expense_tracking_project.data.dataSource.Transaction
import com.example.expense_tracking_project.domain.repository.Transaction.TransactionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date

class TransactionViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = AppDatabase.getDatabase(application).transactionDao()
    private val repository: TransactionRepository = TransactionRepository(dao)

    val allTransactions: LiveData<List<Transaction>> = repository.allTransactions

    var income by mutableStateOf(0.0)
    var expenses by mutableStateOf(0.0)

    init {
        // Observe the LiveData and calculate income/expenses when data changes
        allTransactions.observeForever { transactions ->
            calculateIncomeAndExpenses(transactions)
        }
    }

    private fun calculateIncomeAndExpenses(transactions: List<Transaction>) {
        income = transactions
            .filter { it.note.contains("income", ignoreCase = true) }
            .sumOf { it.amount }

        expenses = transactions
            .filter { it.note.contains("expense", ignoreCase = true) }
            .sumOf { it.amount }
    }

    fun insert(transaction: Transaction) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.insert(transaction)
                Log.d("TransactionInsert", "Transaction successfully inserted.")
            } catch (e: Exception) {
                Log.e("TransactionInsert", "Error inserting transaction: ${e.message}", e)
            }
        }
    }

    fun hideTransaction(transaction: Transaction) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val deletedTransaction = transaction.copy(
                    isDeleted = true,
                    updated_at = Date()
                )
                repository.update(deletedTransaction)
                Log.d("TransactionDelete", "Soft delete applied to transaction.")
            } catch (e: Exception) {
                Log.e("TransactionDelete", "Error applying soft delete: ${e.message}", e)
            }
        }
    }

}



