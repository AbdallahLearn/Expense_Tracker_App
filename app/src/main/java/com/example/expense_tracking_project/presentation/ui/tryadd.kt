package com.example.expense_tracking_project.presentation.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.expense_tracking_project.data.dataSource.Transaction
import com.example.expense_tracking_project.navigation.Screen
import com.example.expense_tracking_project.presentation.vm.transaction_list.TransactionViewModel
import java.util.Date

@Composable
fun TransactionScreen(navController: NavHostController) {
    val viewModel: TransactionViewModel = viewModel()
    val transactions by viewModel.allTransactions.observeAsState(emptyList())

    var amountText by remember { mutableStateOf("") }
    var noteText by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Add Transaction", style = MaterialTheme.typography.titleLarge)

        OutlinedTextField(
            value = amountText,
            onValueChange = { amountText = it },
            label = { Text("Amount") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
        )

        OutlinedTextField(
            value = noteText,
            onValueChange = { noteText = it },
            label = { Text("Note") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
        )

        Button(
            onClick = {
                val amount = amountText.toDoubleOrNull() ?: 0.0
                if (amount > 0) {
                    // Update income or expenses based on user selection
                    val category = if (noteText.contains("income", ignoreCase = true)) {
                        "expense"
                    } else {
                        "income"
                    }

                    viewModel.insert(
                        Transaction(
                            categoryId = 1, // just a sample category
                            amount = amount,
                            date = Date(),
                            note = noteText,
                            created_at = Date(),
                            updated_at = Date()
                        )
                    )

                    amountText = ""
                    noteText = ""
                }
                navController.navigate(Screen.Home.route)
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Save")
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text("All Transactions", style = MaterialTheme.typography.titleMedium)

        // LazyColumn to display the list of transactions with dynamic amount
        LazyColumn {
            items(transactions) { txn ->
                Text(
                    text = "Amount: ${txn.amount} - Note: ${txn.note}",
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }

    }
}
