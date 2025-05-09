@file:Suppress("NAME_SHADOWING")

package com.example.expense_tracking_project.screens.expenseTracking.presentation.screens

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.expense_tracking_project.screens.authentication.presentation.component.BackgroundLayout
import com.example.expense_tracking_project.screens.authentication.presentation.component.CustomDropdownMenuBudget
import com.example.expense_tracking_project.screens.authentication.presentation.component.SimpleButton
import com.example.expense_tracking_project.screens.authentication.presentation.component.SimpleTextField
import com.example.expense_tracking_project.screens.expenseTracking.presentation.vmModels.EditBudgetViewModel
import androidx.compose.ui.res.stringResource
import com.example.expense_tracking_project.R

@RequiresApi(Build.VERSION_CODES.O)
@Composable

fun AddBudgetScreen(
    navController: NavController,
    budgetId: Int? = null,
    viewModel: EditBudgetViewModel = hiltViewModel(),
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

    BackgroundLayout(title = stringResource(R.string.editBudget))

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
                .border(1.dp, Color.White, RoundedCornerShape(30.dp))
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
                    title = stringResource(R.string.amountBudget),
                    value = viewModel.budgetAmount.value,
                    onValueChange = { viewModel.budgetAmount.value = it }
                )

                Spacer(modifier = Modifier.height(16.dp))

                SimpleTextField(
                    title = stringResource(R.string.startDate),
                    value = viewModel.startDate.value,
                    onValueChange = {},
                    onIconClick = {
                        viewModel.getStartDatePicker(context).show()
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                CustomDropdownMenuBudget(
                    label = stringResource(R.string.Interval),
                    BudgetOptions = listOf(
                        stringResource(R.string.one_month),
                        stringResource(R.string.two_months),
                        stringResource(R.string.three_months)
                    ),
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
        val successMsg = stringResource(R.string.budget_saved_successfully)
        val context = LocalContext.current
        SimpleButton(
            title = stringResource(R.string.add),
            onButtonClick = {
                viewModel.saveBudget(
                    context = context,
                    onSuccess = {
                        Toast.makeText(context, successMsg, Toast.LENGTH_SHORT).show()
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
