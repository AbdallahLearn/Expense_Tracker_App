package com.example.expense_tracking_project.screens.expenseTracking.presentation.screens

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
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
import com.example.expense_tracking_project.navigation.Screen
import com.example.expense_tracking_project.screens.authentication.presentation.component.SelectEditingTab
import com.example.expense_tracking_project.screens.dataSynchronization.presentation.SyncViewModel
import com.example.expense_tracking_project.screens.expenseTracking.presentation.component.ConfirmationDialog
import com.example.expense_tracking_project.screens.expenseTracking.presentation.component.DataCard
import com.example.expense_tracking_project.screens.expenseTracking.presentation.vmModels.EditBudgetViewModel
import com.example.expense_tracking_project.screens.expenseTracking.presentation.vmModels.EditCategoryViewModel
import com.example.expense_tracking_project.screens.expenseTracking.presentation.vmModels.EditScreenViewModel
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@SuppressLint("WrongNavigateRouteType")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EditScreen(
    navController: NavController,
    viewModel: EditScreenViewModel = hiltViewModel(),
    categoryViewModel: EditCategoryViewModel = hiltViewModel(),
    budgetViewModel: EditBudgetViewModel = hiltViewModel()
) {
    val selectedTab by viewModel.selectedTab.collectAsState()
    val searchText by viewModel.searchText.collectAsState()
    val categories by categoryViewModel.categories
    val budgetList by budgetViewModel.budgetList

    // Load categories when the tab is "Category"
    LaunchedEffect(selectedTab) {
        if (selectedTab == "Category") {
            categoryViewModel.loadCategories()
        } else if (selectedTab == "Budget"){
            budgetViewModel.loadBudgets()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
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

            // Display category list or empty message
            if (selectedTab == "Category") {
                if (categories.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No data available",
                            color = Color.Gray
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f)
                    ) {
                        items(categories) { category ->
                            var showDeleteDialog by remember { mutableStateOf(false) }


                            DataCard(
                                title = " ${category.categoryName}",
                                subtitleItems = listOf(
                                    "Type: ${category.type}",
                                    "Budget ID: ${category.budgetId}", //it should budget ID
                                ),
                                trailingContent = {
                                    Row {
                                        IconButton(onClick = {
                                            navController.navigate(Screen.AddCategory(categoryId = category.categoryId))
                                        }) {
                                            Icon(
                                                imageVector = Icons.Outlined.Edit,
                                                contentDescription = "Edit Category",
                                                tint = Color.Gray
                                            )
                                        }

                                        IconButton(onClick = {
                                            showDeleteDialog = true
                                        }) {
                                            Icon(
                                                imageVector = Icons.Outlined.Delete,
                                                contentDescription = "Delete Category",
                                                tint = Color.Red
                                            )
                                        }
                                    }
                                }
                            )

                            if (showDeleteDialog) {
                                ConfirmationDialog(
                                    title = "Confirm Deletion",
                                    message = "Are you sure you want to delete this category?",
                                    onConfirm = {
                                        categoryViewModel.softDeleteCategory(category.categoryId!!) {
                                            showDeleteDialog = false
                                        }
                                    },
                                    onDismiss = {
                                        showDeleteDialog = false
                                    }
                                )
                            }
                        }
                    }


                }
            }
            if (selectedTab == "Budget") {
                if (budgetList.isNotEmpty()) {
                    DisplaySavedBudgets(budgetList, navController = navController)
                } else {
                    Text("No budgets available", color = Color.Gray)
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

                    when (selectedTab) {
                        "Budget" -> {
                            val budget = Screen.AddBudget(budgetId = 2)
                            val budgetData = Json.encodeToString(budget)
                            navController.navigate("add_budget?budgetData=$budgetData")
                        }

                        "Category" -> {
                            val category = Screen.AddCategory(categoryId = 1)
                            val categoryData = Json.encodeToString(category)
                            navController.navigate("add_category?categoryData=$categoryData")
                        }
                    }
                },
                containerColor = Color(0xFF5C4DB7)
            ) {
                Text("Add", color = Color.White, fontSize = 15.sp)
            }

        }

        LaunchedEffect(selectedTab) {
            if (selectedTab == "Budget") {
                budgetViewModel.loadBudgets()
            }
        }

    }
}

