package com.example.expense_tracking_project.screens.expenseTracking.presentation.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.expense_tracking_project.core.local.data.PredefinedBudgetProvider
import com.example.expense_tracking_project.navigation.Screen
import com.example.expense_tracking_project.screens.authentication.presentation.component.BackgroundLayout
import com.example.expense_tracking_project.screens.authentication.presentation.component.CustomDropdownMenu
import com.example.expense_tracking_project.screens.authentication.presentation.component.CustomDropdownMenuBudget
import com.example.expense_tracking_project.screens.authentication.presentation.component.SelectEditingTab
import com.example.expense_tracking_project.screens.authentication.presentation.component.SimpleButton
import com.example.expense_tracking_project.screens.authentication.presentation.component.SimpleTextField
import com.example.expense_tracking_project.screens.expenseTracking.presentation.vmModels.EditBudgetCategoryViewModel
import com.example.expense_tracking_project.screens.expenseTracking.presentation.vmModels.EditScreenViewModel


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EditScreen(
    navController: NavController,
    viewModel: EditScreenViewModel = viewModel()
) {
    val selectedTab by viewModel.selectedTab.collectAsState()
    val searchText by viewModel.searchText.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        // Background + Tabs
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

            // Empty Data Placeholder
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
        }

        // Floating Action Button
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 300.dp ,bottom = 10.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            FloatingActionButton(
                onClick = { if (selectedTab == "Budget") {
                    navController.navigate(Screen.AddBudget)
                } else if (selectedTab == "Category"){
                    navController.navigate(Screen.AddCategory)
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
    viewModel: EditBudgetCategoryViewModel = viewModel()
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
    viewModel: EditBudgetCategoryViewModel = viewModel()
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
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        SimpleButton(
            title = "Save",
            onButtonClick = {
                viewModel.saveCategory(
                    onSuccess = { navController.popBackStack() },
                    onFailure = {  }
                )
            }
        )
    }
}
