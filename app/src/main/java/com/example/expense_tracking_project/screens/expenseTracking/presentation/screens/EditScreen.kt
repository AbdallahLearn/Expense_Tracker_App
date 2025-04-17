package com.example.expense_tracking_project.screens.expenseTracking.presentation.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.expense_tracking_project.core.local.data.PredefinedBudgetProvider
import com.example.expense_tracking_project.core.local.entities.BudgetEntity
import com.example.expense_tracking_project.navigation.Screen
import com.example.expense_tracking_project.screens.authentication.presentation.component.BackgroundLayout
import com.example.expense_tracking_project.screens.authentication.presentation.component.CustomDropdownMenu
import com.example.expense_tracking_project.screens.authentication.presentation.component.SelectEditingTab
import com.example.expense_tracking_project.screens.authentication.presentation.component.SimpleButton
import com.example.expense_tracking_project.screens.authentication.presentation.component.SimpleTextField
import com.example.expense_tracking_project.screens.expenseTracking.presentation.component.ConfirmationDialog
import com.example.expense_tracking_project.screens.expenseTracking.presentation.vmModels.EditBudgetCategoryViewModel
import com.example.expense_tracking_project.screens.expenseTracking.presentation.vmModels.EditScreenViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EditScreen(
    navController: NavController, viewModel: EditScreenViewModel = hiltViewModel()
) {
    val selectedTab by viewModel.selectedTab.collectAsState()
    val searchText by viewModel.searchText.collectAsState()
    val viewModeleditbudget: EditBudgetCategoryViewModel = hiltViewModel()
    val budgetList by viewModeleditbudget.budgetList
    Box(modifier = Modifier.fillMaxSize()) {
        // Background + Tabs
        SelectEditingTab(
            showTabs = true,
            tabOptions = listOf("Category", "Budget"),
            onTabSelected = { viewModel.updateTab(it) })

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 200.dp, start = 16.dp, end = 16.dp)
        ) {
            Text(
                text = "Recent $selectedTab(s)",
                fontSize = 18.sp,
                color = Color(0xFF5C4DB7),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Search bar
            OutlinedTextField(
                value = searchText,
                onValueChange = { viewModel.updateSearch(it) },
                placeholder = { Text("Search") },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.FilterList, contentDescription = "Filter"
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 30.dp)
                    .clip(RoundedCornerShape(16.dp))
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Empty Data Placeholder
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f), contentAlignment = Alignment.Center
            ) {
                if (selectedTab == "Budget") {
                    if (budgetList.isNotEmpty()) {
                        DisplaySavedBudgets(budgetList)
                    } else {
                        Text("No budgets available", color = Color.Gray)
                    }
                } else {
                    Text("No categories available", color = Color.Gray)
                }

            }
        }

        // Floating Action Button
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 300.dp, bottom = 10.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            FloatingActionButton(
                onClick = {
                    if (selectedTab == "Budget") {
                        navController.navigate(Screen.AddBudget)
                    } else if (selectedTab == "Category") {
                        navController.navigate(Screen.AddCategory)
                    }
                }, containerColor = Color(0xFF5C4DB7)
            ) {
                Text("Add", color = Color.White, fontSize = 15.sp)
            }
        }

        LaunchedEffect(selectedTab) {
            if (selectedTab == "Budget") {
                viewModeleditbudget.loadBudgets()
            }
        }

    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DisplaySavedBudgets(budgetList: List<BudgetEntity>) {
    //val  budget: BudgetEntity
    val viewModel: EditBudgetCategoryViewModel = hiltViewModel()
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
    ) {
        items(budgetList) { budget ->
            var showDeleteDialog by remember { mutableStateOf(false) }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 4.dp),
                elevation = CardDefaults.cardElevation(4.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Amount: ${budget.totalAmount}",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Start: ${budget.startDate}",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                        Text(
                            text = "End: ${budget.endDate}",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                    }

                    IconButton(
                        onClick = { showDeleteDialog = true }, modifier = Modifier
                            .padding(top = 20.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete Budget",
                            tint = Color.Red
                        )
                    }
                }

                if (showDeleteDialog) {
                    ConfirmationDialog(
                        title = "Confirm Deletion",
                        message = "Are you sure you want to delete this budget?",
                        onConfirm = {
                            viewModel.softDeleteBudget(budget)
                            showDeleteDialog = false
                        },
                        onDismiss = {
                            showDeleteDialog = false
                        })
                }
            }
        }

    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AddCategoryScreen(
    navController: NavController, viewModel: EditBudgetCategoryViewModel = hiltViewModel()
) {
    BackgroundLayout("Edit Category")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 120.dp, start = 24.dp, end = 24.dp, bottom = 100.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            shape = RoundedCornerShape(32.dp),
            elevation = CardDefaults.cardElevation(8.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(18.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Spacer(modifier = Modifier.height(20.dp))

                SimpleTextField(
                    title = "Category Name",
                    value = viewModel.categoryName.value,
                    onValueChange = { viewModel.categoryName.value = it })

                Spacer(modifier = Modifier.height(16.dp))

                CustomDropdownMenu(
                    label = "Category Type",
                    categoryOptions = listOf("Income", "Expense"),
                    selectedOption = viewModel.categoryType.value,
                    onOptionSelected = { viewModel.categoryType.value = it })

                Spacer(modifier = Modifier.height(16.dp))

                CustomDropdownMenu(
                    label = "Budget (Optional)",
                    categoryOptions = PredefinedBudgetProvider.getAllBudgets(),
                    selectedOption = viewModel.budget.value,
                    onOptionSelected = { viewModel.budget.value = it })

                Spacer(modifier = Modifier.height(16.dp))

                SimpleTextField(
                    title = "Note",
                    value = viewModel.note.value,
                    onValueChange = { viewModel.note.value = it })
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        SimpleButton(
            title = "Save", onButtonClick = {
                viewModel.saveCategory(
                    onSuccess = { navController.popBackStack() },
                    onFailure = { })
            })
    }
}