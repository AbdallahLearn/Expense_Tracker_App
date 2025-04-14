package com.example.expense_tracking_project.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.Image
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.LightMode

import androidx.compose.material.icons.filled.Delete

import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.livedata.observeAsState

import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.expense_tracking_project.R
import com.example.expense_tracking_project.data.dataSource.Transaction.Transaction
import com.example.expense_tracking_project.navigation.Screen
import com.example.expense_tracking_project.presentation.ui.dialogs.ConfirmationDialog
import com.example.expense_tracking_project.presentation.vm.ThemeViewModel
import com.example.expense_tracking_project.presentation.vm.transaction_list.TransactionViewModel

@Composable
fun HomeScreen(navController: NavController, themeViewModel: ThemeViewModel, isDarkTheme: Boolean) {
    val transactionViewModel: TransactionViewModel = viewModel()
    val transactions by transactionViewModel.allTransactions.observeAsState(emptyList())

    Scaffold(
        bottomBar = {
            CustomBottomBar(
                selectedIndex = 0,
                onItemSelected = { index ->
                    when (index) {
                        0 -> navController.navigate("home")
                        1 -> navController.navigate("statistics")
                        2 -> navController.navigate("edit")
                        3 -> navController.navigate("profile")
                    }
                },
                navController = navController
            )
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize()) {
            // Curved Top Background
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .background(
                        color = Color(0xFF5C4DB7),
                        shape = RoundedCornerShape(bottomStart = 35.dp, bottomEnd = 35.dp)
                    )
            )

            // Foreground content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 16.dp)
            ) {
                Spacer(modifier = Modifier.height(24.dp)) // Top spacing
                TopSection(
                    name = "Abdullah",
                    isDarkTheme = isDarkTheme,
                    onToggleTheme = { themeViewModel.toggleTheme() }
                )
                BudgetCard(income = transactionViewModel.income, expenses = transactionViewModel.expenses)
                TimeTabSection()
                RecentTransactions(navController, transactions, transactionViewModel = transactionViewModel)
            }
        }
    }
}

@Composable
fun TopSection(
    name: String,
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = stringResource(id = R.string.welcome),
                style = MaterialTheme.typography.labelMedium,
                color = Color.White
            )
            Text(
                text = name,
                style = MaterialTheme.typography.titleMedium,
                color = Color.White
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { /* Search action */ }) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = stringResource(id = R.string.search),
                    tint = Color.White
                )
            }
            IconButton(onClick = onToggleTheme) {
                Icon(
                    imageVector = if (isDarkTheme) Icons.Default.LightMode else Icons.Default.DarkMode,
                    contentDescription = stringResource(id = R.string.theme),
                    tint = Color.White
                )
            }
        }
    }
}

@Composable
fun BudgetCard(income: Double, expenses: Double) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(210.dp)
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF5C4DB7))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = stringResource(R.string.budget),
                color = Color.White,
                modifier = Modifier.padding(bottom = 6.dp)
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.saudi_riyal_symbol),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    contentScale = ContentScale.Fit,
                    colorFilter = ColorFilter.tint(Color.White)
                )
                Text(
                    text = "$${income - expenses}",
                    color = Color.White,
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = R.drawable.saudi_riyal_symbol),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        contentScale = ContentScale.Fit,
                        colorFilter = ColorFilter.tint(Color.White)
                    )
                    Text(
                        text = "${stringResource(R.string.income)}\n $${income}",
                        color = Color.White,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = R.drawable.saudi_riyal_symbol),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        contentScale = ContentScale.Fit,
                        colorFilter = ColorFilter.tint(Color.White)
                    )
                    Text(
                        text = "${stringResource(R.string.expenses)}\n $${expenses}",
                        color = Color.White,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun TimeTabSection() {
    var selectedTab by remember { mutableStateOf("Today") }
    val tabs = listOf(
        stringResource(id = R.string.today),
        stringResource(id = R.string.week),
        stringResource(id = R.string.month),
        stringResource(id = R.string.year)
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        tabs.forEach { tab ->
            TextButton(
                onClick = { selectedTab = tab },
                colors = ButtonDefaults.textButtonColors(
                    containerColor = if (tab == selectedTab) Color(0xFFFFEBCD) else Color.Transparent
                )
            ) {
                Text(text = tab, color = if (tab == selectedTab) Color.Black else Color.Gray)
            }
        }
    }
}

@Composable
fun RecentTransactions(
    navController: NavController,
    transactions: List<Transaction>,
    transactionViewModel: TransactionViewModel
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                stringResource(R.string.recent_transactions),
                style = MaterialTheme.typography.titleSmall
            )
            Text(
                stringResource(R.string.see_all),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable {
                    navController.navigate(Screen.AddTransaction.route)
                }
            )
        }

        Button(
            onClick = { navController.navigate(Screen.AddTransaction.route) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text("Add Transaction")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (transactions.isEmpty()) {
            Text(
                stringResource(R.string.no_data_available),
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
        } else {
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(transactions) { transaction ->
                    TransactionItem(transaction = transaction , transactionViewModel = transactionViewModel )
                }
            }
        }
    }
}

@Composable
fun TransactionItem(transaction: Transaction, transactionViewModel: TransactionViewModel) {
    var showDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { /* Handle click action */ },
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text("Amount: ${transaction.amount}", style = MaterialTheme.typography.bodyMedium)
                Text("Note: ${transaction.note}", style = MaterialTheme.typography.bodySmall)
                Text(
                    "Date: ${transaction.date}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }

            if (showDialog) {
                ConfirmationDialog(
                    title = "Delete Transaction",
                    message = "Are you sure you want to delete this transaction?",
                    confirmButtonText = "Delete",
                    confirmButtonColor = Color.Red,
                    onConfirm = {
                        transactionViewModel.hideTransaction(transaction)
                        showDialog = false
                    },
                    onDismiss = {
                        showDialog = false
                    }
                )
            }

            IconButton(onClick = {
                showDialog = true
            }) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete Transaction",
                    tint = Color.Red
                )
            }
        }
    }
}



