package com.example.expense_tracking_project.screens.expenseTracking.presentation.screens


import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.expense_tracking_project.R
import com.example.expense_tracking_project.core.local.entities.Transaction
import com.example.expense_tracking_project.screens.dataSynchronization.presentation.SyncViewModel
import com.example.expense_tracking_project.screens.authentication.presentation.component.DropdownFilter
import com.example.expense_tracking_project.screens.expenseTracking.presentation.component.ConfirmationDialog
import com.example.expense_tracking_project.screens.expenseTracking.presentation.component.DataCard
import com.example.expense_tracking_project.screens.expenseTracking.presentation.vmModels.HomeViewModel
import com.example.expense_tracking_project.screens.expenseTracking.presentation.vmModels.TimeFilter
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.util.Calendar
import java.util.Locale

@Composable
fun HomeScreen(
    navController: NavController,
    changeAppTheme: () -> Unit,
    isDarkTheme: Boolean,
    viewModel: HomeViewModel = hiltViewModel(),
    syncViewModel: SyncViewModel = hiltViewModel()
) {
    // Add user name state
    var userName by remember { mutableStateOf("") }
    val currentUser = Firebase.auth.currentUser

    // Fetch user name when composable launches
    LaunchedEffect(Unit) {
        userName = currentUser?.displayName ?: ""
    }

    val transactions by viewModel.transactions.collectAsState(initial = emptyList())
    Log.d("DEBUG", "UI transactions: $transactions")
    val syncStatus by syncViewModel.syncStatus.collectAsState()

    var showSearchField by remember { mutableStateOf(false) }
    val context = LocalContext.current

    syncStatus?.let {
        Text(
            text = it,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(vertical = 8.dp)
        )
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 80.dp)
    ) {
        item {
            Box(modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .background(
                            color = Color(0xFF5C4DB7),
                            shape = RoundedCornerShape(bottomStart = 35.dp, bottomEnd = 35.dp)
                        )
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 24.dp)
                ) {
                    TopSection(
                        name = userName, // Pass the fetched user name here
                        isDarkTheme = isDarkTheme,
                        onToggleTheme = changeAppTheme,
                        onSearchClicked = { showSearchField = !showSearchField },
                        onToggleLanguage = { changeLanguage(context) }
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    BudgetCard(
                        income = viewModel.income.value,
                        expenses = viewModel.expenses.value
                    )
                }
            }
        }
        item {
            Spacer(modifier = Modifier.height(16.dp))
            TimeTabSection()
            Spacer(modifier = Modifier.height(16.dp))
        }
        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                TransactionsSection(
                    navController = navController,
                    transactions = transactions,
                    viewModel = viewModel,
                    showSearchField = showSearchField // send the state of search
                )
            }
        }
    }
}

fun changeLanguage(context: Context) {
    val currentLocale = context.resources.configuration.locales[0]
    val newLocale = if (currentLocale.language == "en") Locale("ar") else Locale("en")

    val config = context.resources.configuration
    config.setLocale(newLocale)
    context.resources.updateConfiguration(config, context.resources.displayMetrics)

    if (context is Activity) {
        context.recreate() // Restart the activity to apply the new locale
    }
}



@Composable
fun TransactionsSection(
    transactions: List<Transaction>,
    showSearchField: Boolean,
    viewModel: HomeViewModel,
    navController: NavController
) {
    val searchText by viewModel.searchText.collectAsState()
    val typeFilter by viewModel.typeFilter.collectAsState()
    var showDropdown by remember { mutableStateOf(false) }

    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.recent_transactions),
                style = MaterialTheme.typography.titleSmall
            )
        }

        if (showSearchField) {
            OutlinedTextField(
                value = searchText,
                onValueChange = { viewModel.updateSearch(it) },
                placeholder = { Text(stringResource(R.string.search)) },
                trailingIcon = {
                    IconButton(onClick = { showDropdown = !showDropdown }) {
                        Icon(Icons.Default.FilterList, contentDescription = "Filter")
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
                    .clip(RoundedCornerShape(16.dp))
            )
        }

        if (showDropdown) {
            DropdownFilter(typeFilter = typeFilter) {
                viewModel.updateTypeFilter(it)
                showDropdown = false
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (transactions.isEmpty()) {
            Text(
                stringResource(R.string.no_data_available),
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
        } else {
            transactions.forEach { transaction ->
                Log.d("DEBUG", "Rendering transaction: $transaction")
                TransactionItem(
                    transaction = transaction,
                    navController = navController,
                    viewModel = viewModel
                )
            }
        }
    }
}

@Composable
fun TopSection(
    name: String,
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit,
    onSearchClicked: () -> Unit,
    onToggleLanguage: () -> Unit
) {
    val themeIcon = if (isDarkTheme) Icons.Default.LightMode else Icons.Default.DarkMode

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
            IconButton(onClick = onSearchClicked) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = stringResource(id = R.string.search),
                    tint = Color.White
                )
            }
            IconButton(onClick = onToggleTheme) {
                Icon(
                    imageVector = themeIcon,
                    contentDescription = stringResource(id = R.string.theme),
                    tint = Color.White
                )
            }
            IconButton(onClick = onToggleLanguage) { // This calls your changeLanguage function
                Icon(
                    imageVector = Icons.Default.Language,
                    contentDescription = stringResource(id = R.string.change_language),
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
            .padding(8.dp),
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
                    text = "${income - expenses}",
                    color = Color.White,
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
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
                        text = "${stringResource(R.string.income)}\n ${income}",
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
                        text = "${stringResource(R.string.expenses)}\n ${expenses}",
                        color = Color.White,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun TimeTabSection(viewModel: HomeViewModel = hiltViewModel()) {
    val selectedTab by viewModel.timeFilter.collectAsState()

    val tabs = listOf(
        TimeFilter.TODAY to stringResource(id = R.string.today),
        TimeFilter.WEEK to stringResource(id = R.string.week),
        TimeFilter.MONTH to stringResource(id = R.string.month),
        TimeFilter.YEAR to stringResource(id = R.string.year)
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        tabs.forEach { (filter, label) ->
            TextButton(
                onClick = {
                    viewModel.updateTimeFilter(filter)
                },
                colors = ButtonDefaults.textButtonColors(
                    containerColor = if (filter == selectedTab) Color(0xFFFFEBCD) else Color.Transparent
                )
            ) {
                Text(text = label, color = if (filter == selectedTab) Color.Black else Color.Gray)
            }
        }
    }
}


@Composable
fun TransactionItem(transaction: Transaction,
                    navController: NavController,
                    viewModel: HomeViewModel = hiltViewModel()) {
    var showDeleteDialog by remember { mutableStateOf(false) }

    val transactionType = if (transaction.amount >= 0) "Income" else "Expenses"
    val typeColor = if (transaction.amount >= 0) Color(0xFF4CAF50) else Color(0xFFF44336)

    DataCard(
        title = "${transaction.amount}",
        titleColor = typeColor,
        subtitleItems = buildList {
            add(stringResource(R.string.transaction) + ": $transactionType")
            add(stringResource(R.string.category) + ": ${transaction.categoryId}")
            if (!transaction.note.isNullOrBlank()) {
                add(stringResource(R.string.note) + ": ${transaction.note}")
            }
            add(stringResource(R.string.date) + ": ${viewModel.formatDate(transaction.date)}")
        },
        trailingContent = {
            Row {
                IconButton(onClick = {
                    navController.navigate("add_expense?transactionId=${transaction.transactionId}")
                }) {
                    Icon(
                        imageVector = Icons.Outlined.Edit,
                        contentDescription = "Edit Transaction",
                        tint = Color.Gray
                    )
                }

                IconButton(onClick = {
                    showDeleteDialog = true
                }) {
                    Icon(
                        imageVector = Icons.Outlined.Delete,
                        contentDescription = "Delete Transaction",
                        tint = Color.Red
                    )
                }
            }
        },
        // 🔹 Add this for Riyal symbol beside amount
        titleLeadingContent = {
            Image(
                painter = painterResource(id = R.drawable.saudi_riyal_symbol),
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                contentScale = ContentScale.Fit,
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground)
            )
        }
    )


    if (showDeleteDialog) {
        ConfirmationDialog(
            title = stringResource(R.string.confirm_deletion),
            message = stringResource(R.string.confirm_delete_transaction),
            onConfirm = {
                viewModel.softDeleteTransaction(transaction)
                showDeleteDialog = false
            },
            onDismiss = {
                showDeleteDialog = false
            }
        )
    }
}