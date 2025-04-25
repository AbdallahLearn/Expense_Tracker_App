package com.example.expense_tracking_project.screens.expenseTracking.presentation.screens

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.expense_tracking_project.R
import com.example.expense_tracking_project.navigation.Screen
import com.example.expense_tracking_project.screens.authentication.presentation.component.SelectEditingTab
import com.example.expense_tracking_project.screens.expenseTracking.presentation.component.ConfirmationDialog
import com.example.expense_tracking_project.screens.expenseTracking.presentation.component.DataCard
import com.example.expense_tracking_project.screens.expenseTracking.presentation.vmModels.EditBudgetViewModel
import com.example.expense_tracking_project.screens.expenseTracking.presentation.vmModels.EditCategoryViewModel
import com.example.expense_tracking_project.screens.expenseTracking.presentation.vmModels.EditScreenViewModel
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import androidx.core.graphics.toColorInt

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

    LaunchedEffect(selectedTab) {
        if (selectedTab == "Category") {
            categoryViewModel.loadCategories()
        } else if (selectedTab == "Budget") {
            budgetViewModel.loadBudgets()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        SelectEditingTab(
            showTabs = true,
            tabOptions = listOf("Category", "Budget"),
            onTabSelected = { viewModel.updateTab(it) }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 200.dp, start = 16.dp, end = 16.dp)
        ) {
            Text(
                text = stringResource(R.string.recent_items, selectedTab),
                fontSize = 18.sp,
                color = Color(0xFF5C4DB7),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Search bar for Category only
            if (selectedTab == "Category") {
                OutlinedTextField(
                    value = searchText,
                    onValueChange = { viewModel.updateSearch(it) },
                    placeholder = { Text(stringResource(R.string.search)) },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.FilterList,
                            contentDescription = stringResource(R.string.filter)
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 30.dp)
                        .clip(RoundedCornerShape(16.dp))
                )

                Spacer(modifier = Modifier.height(24.dp))
            }

            // CATEGORY TAB
            if (selectedTab == "Category") {
                val filteredCategories = categories
                    .filter { it.categoryName.contains(searchText, ignoreCase = true) }
                    .sortedByDescending { it.createdAt }

                if (filteredCategories.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(stringResource(R.string.no_data), color = Color.Gray)
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f)
                    ) {
                        items(filteredCategories) { category ->
                            var showDeleteDialog by remember { mutableStateOf(false) }
                            val typeLocalized = when (category.type) {
                                "Income" -> stringResource(R.string.Income)
                                "Expense" -> stringResource(R.string.expense)
                                else -> category.type
                            }
                            val typeLabel = stringResource(R.string.type_label, typeLocalized)

                            DataCard(
                                title = category.categoryName,
                                subtitleItems = listOf(typeLabel),
                                titleLeadingContent = {
                                    Box(
                                        modifier = Modifier
                                            .size(12.dp)
                                            .clip(CircleShape)
                                            .background(
                                                try {
                                                    Color(category.color.toColorInt())
                                                } catch (e: Exception) {
                                                    Color.Gray
                                                }
                                            )
                                    )
                                },
                                trailingContent = {
                                    Row {
                                        IconButton(onClick = {
                                            navController.navigate(Screen.AddCategory(categoryId = category.categoryId))
                                        }) {
                                            Icon(
                                                imageVector = Icons.Outlined.Edit,
                                                contentDescription = stringResource(R.string.edit_category),
                                                tint = Color.Gray
                                            )
                                        }

                                        IconButton(onClick = {
                                            showDeleteDialog = true
                                        }) {
                                            Icon(
                                                imageVector = Icons.Outlined.Delete,
                                                contentDescription = stringResource(R.string.delete_category),
                                                tint = Color.Red
                                            )
                                        }
                                    }
                                }
                            )

                            if (showDeleteDialog) {
                                ConfirmationDialog(
                                    title = stringResource(R.string.confirm_deletion),
                                    message = stringResource(R.string.delete_message),
                                    onConfirm = {
                                        categoryViewModel.softDeleteCategory(category.categoryId) {
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

            // BUDGET TAB
            if (selectedTab == "Budget") {
                if (budgetList.isNotEmpty()) {
                    DisplaySavedBudgets(budgetList, navController = navController)
                } else {
                    Text(stringResource(R.string.no_budgets), color = Color.Gray)
                }
            }
        }

        // FAB
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
                            val budget = Screen.AddBudget()
                            val budgetData = Json.encodeToString(budget)
                            navController.navigate("add_budget?budgetData=$budgetData")
                        }

                        "Category" -> {
                            val category = Screen.AddCategory()
                            val categoryData = Json.encodeToString(category)
                            navController.navigate("add_category?categoryData=$categoryData")
                        }
                    }
                },
                containerColor = Color(0xFF5C4DB7)
            ) {
                Text(stringResource(R.string.add), color = Color.White, fontSize = 15.sp)
            }
        }

        LaunchedEffect(selectedTab) {
            if (selectedTab == "Budget") {
                budgetViewModel.loadBudgets()
            }
            }
        }
}
