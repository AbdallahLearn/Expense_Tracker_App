package com.example.expense_tracking_project.presentation.vm.transaction_list

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.expense_tracking_project.data.dataSource.AppDatabase
import com.example.expense_tracking_project.data.dataSource.Transaction
import com.example.expense_tracking_project.domain.repository.Transaction.TransactionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TransactionViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: TransactionRepository
    val allTransactions: LiveData<List<Transaction>>

    init {
        // Initialize the DAO and repository
        val dao = AppDatabase.getDatabase(application).transactionDao()
        repository = TransactionRepository(dao)
        allTransactions = repository.allTransactions
    }

    // Insert a transaction and handle possible errors
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
}


