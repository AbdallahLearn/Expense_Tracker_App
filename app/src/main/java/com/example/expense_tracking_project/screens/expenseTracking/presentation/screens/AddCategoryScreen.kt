package com.example.expense_tracking_project.screens.expenseTracking.presentation.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.expense_tracking_project.screens.authentication.presentation.component.BackgroundLayout
import com.example.expense_tracking_project.screens.authentication.presentation.component.ColorPickerDialog
import com.example.expense_tracking_project.screens.authentication.presentation.component.CustomDropdownMenu
import com.example.expense_tracking_project.screens.authentication.presentation.component.SimpleButton
import com.example.expense_tracking_project.screens.authentication.presentation.component.SimpleTextField
import com.example.expense_tracking_project.screens.expenseTracking.presentation.vmModels.EditCategoryViewModel
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AddCategoryScreen(
    navController: NavController,
    categoryId: Int? = null,
    viewModel: EditCategoryViewModel = hiltViewModel(),
) {
    BackgroundLayout("Edit Category")

    val errorMessage = remember { mutableStateOf("") }
    val budgets = viewModel.budgetList.value
    val selectedBudgetLabel = remember { mutableStateOf("") }
    var showColorPicker by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.loadBudgets()
    }

    LaunchedEffect(categoryId) {
        if (categoryId != null) {
            viewModel.loadCategoryById(categoryId)
        } else {
            viewModel.loadBudgets()
            viewModel.categoryName.value = ""
            viewModel.categoryType.value = ""
            viewModel.budget.value = ""
            viewModel.note.value = ""
            viewModel.selectedBudgetId.value = null
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
                .border(
                    width = 1.dp,
                    color = Color.White,
                    shape = RoundedCornerShape(30.dp)
                )
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

                SimpleTextField(
                    title = "Category Color",
                    value = viewModel.categoryColor.value,
                    onValueChange = {}, // Read-only
                    onIconClick = { showColorPicker = true }
                )

                if (showColorPicker) {
                    ColorPickerDialog(
                        selectedColor = viewModel.categoryColor.value,
                        onColorSelected = {
                            viewModel.categoryColor.value = it
                            showColorPicker = false
                        },
                        onDismiss = { showColorPicker = false }
                    )
                }

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
                    categoryOptions = budgets.map { "ID: ${it.budgetId} • ${it.totalAmount}" },
                    selectedOption = viewModel.budget.value,
                    onOptionSelected = { selectedLabel ->
                        viewModel.budget.value = selectedLabel
                        val selectedBudget = budgets.find {
                            "ID: ${it.budgetId} • ${it.totalAmount}" == selectedLabel
                        }
                        viewModel.selectedBudgetId.value = selectedBudget?.budgetId
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

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
