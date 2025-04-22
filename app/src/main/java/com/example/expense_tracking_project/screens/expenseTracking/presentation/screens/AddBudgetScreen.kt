package com.example.expense_tracking_project.screens.expenseTracking.presentation.screens

import android.os.Build
import android.widget.Toast
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.expense_tracking_project.screens.authentication.presentation.component.BackgroundLayout
import com.example.expense_tracking_project.screens.authentication.presentation.component.CustomDropdownMenuBudget
import com.example.expense_tracking_project.screens.authentication.presentation.component.SimpleButton
import com.example.expense_tracking_project.screens.authentication.presentation.component.SimpleTextField
import com.example.expense_tracking_project.screens.dataSynchronization.presentation.SyncViewModel
import com.example.expense_tracking_project.screens.expenseTracking.presentation.vmModels.EditBudgetViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AddBudgetScreen(
    navController: NavController,
    budgetId: Int? = null,
    viewModel: EditBudgetViewModel = hiltViewModel(),
    syncViewModel: SyncViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val errorMessage = remember { mutableStateOf("") }


    LaunchedEffect(budgetId) {
        if (budgetId != null) {
            viewModel.loadBudgetById(budgetId)
        } else {
            viewModel.budgetAmount.value = ""
            viewModel.startDate.value = ""
            viewModel.selectedInterval.value = ""
        }
    }

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
                    onValueChange = { viewModel.budgetAmount.value = it })

                Spacer(modifier = Modifier.height(16.dp))

                SimpleTextField(
                    title = "Start Date",
                    value = viewModel.startDate.value,
                    onValueChange = {},
                    onIconClick = {
                        viewModel.getStartDatePicker(context).show()
                    })

                Spacer(modifier = Modifier.height(16.dp))

                CustomDropdownMenuBudget(
                    label = "Interval",
                    BudgetOptions = listOf("1 Month", "2 Month", "3 Month"),
                    selectedOption = viewModel.selectedInterval.value,
                    onOptionSelected = { viewModel.selectedInterval.value = it }
                )

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
                viewModel.saveBudget(
                    onSuccess = {
                        Toast.makeText(context, "Budget saved successfully", Toast.LENGTH_SHORT).show()
                        viewModel.loadBudgets()
                        navController.popBackStack()
                    },
                    onFailure = {
                        errorMessage.value = it
                    }
                )
            }
        )

        }
    }
