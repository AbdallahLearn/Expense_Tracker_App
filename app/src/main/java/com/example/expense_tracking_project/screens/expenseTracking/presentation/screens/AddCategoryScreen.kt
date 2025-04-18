package com.example.expense_tracking_project.screens.expenseTracking.presentation.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.expense_tracking_project.core.local.data.PredefinedBudgetProvider
import com.example.expense_tracking_project.screens.authentication.presentation.component.BackgroundLayout
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
            title = "Save", onButtonClick = {
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