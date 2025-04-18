package com.example.expense_tracking_project.screens.expenseTracking.presentation.screens

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.expense_tracking_project.core.local.data.PredefinedBudgetProvider
import com.example.expense_tracking_project.navigation.Screen
import com.example.expense_tracking_project.screens.authentication.presentation.component.BackgroundLayout
import com.example.expense_tracking_project.screens.authentication.presentation.component.CustomDropdownMenu
import com.example.expense_tracking_project.screens.authentication.presentation.component.CustomDropdownMenuBudget
import com.example.expense_tracking_project.screens.authentication.presentation.component.SelectEditingTab
import com.example.expense_tracking_project.screens.authentication.presentation.component.SimpleButton
import com.example.expense_tracking_project.screens.authentication.presentation.component.SimpleTextField
import com.example.expense_tracking_project.screens.expenseTracking.presentation.component.ConfirmationDialog
import com.example.expense_tracking_project.screens.expenseTracking.presentation.component.DataCard
import com.example.expense_tracking_project.screens.expenseTracking.presentation.vmModels.EditBudgetCategoryViewModel
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
) {
    val selectedTab by viewModel.selectedTab.collectAsState()
    val searchText by viewModel.searchText.collectAsState()
    val categories by categoryViewModel.categories

    // Load categories when the tab is "Category"
    LaunchedEffect(selectedTab) {
        if (selectedTab == "Category") {
            categoryViewModel.loadCategories()
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
                        imageVector = Icons.Default.FilterList,
                        contentDescription = "Filter"
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
                                    "Budget ID: ${category.categoryId}", //it should budget ID
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
            if (selectedTab == "Budget") {
                Text(
                    text = "you are in budget screen"
                )
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
                        "Budget" -> navController.navigate(Screen.AddBudget)
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
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AddBudgetScreen(
    navController: NavController,
    viewModel: EditBudgetCategoryViewModel = hiltViewModel(),
) {
    val context = LocalContext.current

    BackgroundLayout("Edit Budget")

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
                    title = "Amount Budget",
                    value = viewModel.budgetAmount.value,
                    onValueChange = { viewModel.budgetAmount.value = it }
                )

                Spacer(modifier = Modifier.height(16.dp))

                SimpleTextField(
                    title = "Start Date",
                    value = viewModel.startDate.value,
                    onValueChange = {},
                    onIconClick = {
                        viewModel.getStartDatePicker(context).show()
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                CustomDropdownMenuBudget(
                    label = "Interval",
                    BudgetOptions = listOf("1 Month", "2 Month", "3 Month"),
                    selectedOption = viewModel.selectedInterval.value,
                    onOptionSelected = { viewModel.selectedInterval.value = it }
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        SimpleButton(
            title = "Save",
            onButtonClick = {

            }
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AddCategoryScreen(
    navController: NavController,
    categoryId: Int? = null,
    viewModel: EditCategoryViewModel = hiltViewModel(),
) {
    BackgroundLayout("Edit Category")

    val errorMessage = remember { mutableStateOf("") }

    LaunchedEffect(categoryId) {
        if (categoryId != null) {
            viewModel.loadCategoryById(categoryId)
        } else {
            // Reset fields for adding new category
            viewModel.categoryName.value = ""
            viewModel.categoryType.value = ""
            viewModel.budget.value = ""
            viewModel.note.value = ""
            viewModel.editingCategoryId.value = null
        }
    }

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
                    onValueChange = { viewModel.categoryName.value = it }
                )

                Spacer(modifier = Modifier.height(16.dp))

                CustomDropdownMenu(
                    label = "Category Type",
                    categoryOptions = listOf("Income", "Expense"),
                    selectedOption = viewModel.categoryType.value,
                    onOptionSelected = { viewModel.categoryType.value = it }
                )

                Spacer(modifier = Modifier.height(16.dp))

                CustomDropdownMenu(
                    label = "Budget (Optional)",
                    categoryOptions = PredefinedBudgetProvider.getAllBudgets(),
                    selectedOption = viewModel.budget.value,
                    onOptionSelected = { viewModel.budget.value = it }
                )

                Spacer(modifier = Modifier.height(16.dp))

                SimpleTextField(
                    title = "Note",
                    value = viewModel.note.value,
                    onValueChange = { viewModel.note.value = it }
                )

                // Display error message if any
                if (errorMessage.value.isNotEmpty()) {
                    Text(
                        text = errorMessage.value,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        SimpleButton(
            title = "Save",
            onButtonClick = {
                viewModel.saveCategory(
                    onSuccess = {
                        navController.popBackStack()
                    },
                    onFailure = { errorMessage.value = it }
                )
            }
        )
    }
}
