package com.example.expense_tracking_project.screens.expenseTracking.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.expense_tracking_project.core.local.db.AppDatabase
import com.example.expense_tracking_project.core.local.entities.Transaction
import com.example.expense_tracking_project.navigation.Screen
import com.example.expense_tracking_project.presentation.vm.transaction_list.TransactionViewModel
import com.example.expense_tracking_project.screens.expenseTracking.data.repositryimp.TransactionRepositoryImpl
import com.example.expense_tracking_project.screens.expenseTracking.domain.usecase.transactionsusecase.GetAllTransactionsUseCase
import com.example.expense_tracking_project.screens.expenseTracking.domain.usecase.transactionsusecase.InsertTransactionUseCase
import com.example.expense_tracking_project.screens.expenseTracking.domain.usecase.transactionsusecase.UpdateTransactionUseCase
import java.util.Date


@Composable
fun TransactionScreen(navController: NavHostController, isDarkTheme: Boolean) {
    val context = LocalContext.current

    val transactionDao = AppDatabase.getDatabase(context).transactionDao()
    val repository = TransactionRepositoryImpl(transactionDao)
    val insertUseCase = InsertTransactionUseCase(repository)
    val updateUseCase = UpdateTransactionUseCase(repository)
    val getAllUseCase = GetAllTransactionsUseCase(repository)

    val viewModel = remember {
        TransactionViewModel(insertUseCase, updateUseCase, getAllUseCase)
    }

    val transactions by viewModel.allTransactions.collectAsState(emptyList())

    val amountText = viewModel.amountText
    val noteText = viewModel.noteText

    val backgroundColor = if (isDarkTheme) Color(0xFF121212) else Color.White
    val textColor = if (isDarkTheme) Color.White else Color.Black

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(16.dp)
    ) {
        Text(
            text = "Add Transaction", style = MaterialTheme.typography.titleLarge, color = textColor
        )

        OutlinedTextField(
            value = amountText,
            onValueChange = { viewModel.onAmountChange(it) },
            label = { Text("Amount", color = textColor) },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            textStyle = MaterialTheme.typography.bodyLarge.copy(color = textColor)
        )

        OutlinedTextField(
            value = noteText,
            onValueChange = { viewModel.onNoteChange(it) },
            label = { Text("Note", color = textColor) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            textStyle = MaterialTheme.typography.bodyLarge.copy(color = textColor)
        )

        Button(
            onClick = {
                val amount = amountText.toDoubleOrNull() ?: 0.0
                if (amount > 0) {
                    val category = if (noteText.contains("income", ignoreCase = true)) {
                        "income"
                    } else {
                        "expense"
                    }

                    viewModel.insert(
                        Transaction(
                            categoryId = 1,
                            amount = amount,
                            date = Date(),
                            note = noteText,
                            createdAt = Date(),
                            updatedAt = Date()
                        )
                    )

                    viewModel.onAmountChange("")
                    viewModel.onNoteChange("")

                    navController.navigate(Screen.Home)
                }
            }, modifier = Modifier.align(Alignment.End)
        ) {
            Text("Save")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "All Transactions",
            style = MaterialTheme.typography.titleMedium,
            color = textColor
        )

        LazyColumn {
            items(transactions) { txn ->
                Text(
                    text = "Amount: ${txn.amount} - Note: ${txn.note}",
                    color = textColor,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }
    }
}