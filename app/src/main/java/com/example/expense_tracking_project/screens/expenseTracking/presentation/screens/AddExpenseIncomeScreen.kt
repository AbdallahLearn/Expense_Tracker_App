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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.expense_tracking_project.R
import com.example.expense_tracking_project.screens.authentication.presentation.component.BackgroundLayout
import com.example.expense_tracking_project.screens.authentication.presentation.component.CustomDropdownMenu
import com.example.expense_tracking_project.screens.authentication.presentation.component.SelectTransaction
import com.example.expense_tracking_project.screens.authentication.presentation.component.SimpleButton
import com.example.expense_tracking_project.screens.authentication.presentation.component.SimpleTextField
import androidx.navigation.NavController
import com.example.expense_tracking_project.navigation.Screen
import com.example.expense_tracking_project.screens.expenseTracking.presentation.vmModels.AddTransactionViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AddExpenseScreen(
    navController: NavController,
    viewModel: AddTransactionViewModel = viewModel()
) {

    // retrieve current context
    val context = LocalContext.current

    // handle navigation to the home screen after adding transaction
    var navigateToHome by remember { mutableStateOf(false) }

    // initialize the date picker dialog for the Calendar
    val datePickerDialog = remember { viewModel.updateDate(context) }

    // Navigate to home screen
    LaunchedEffect(navigateToHome) {
        if (navigateToHome) {
            navController.navigate(Screen.Home)
        }
    }

    // Observe State from the ViewModel
    val amountState = viewModel.getAmountState()
    val categoryState = viewModel.getCategoryState()
    val dateState = viewModel.getDateState()
    val noteState = viewModel.getNoteState()

    // for the category options
    val categoryOptions by remember(viewModel.selectedTab.value) {
        mutableStateOf(viewModel.getCategoryOptions())
    }

    // text fields based on the selected transaction type (Income / Expenses)
    val amountLabel = stringResource(
        if (viewModel.selectedTab.value == "Income") R.string.incomeAmount else R.string.expenseAmount
    )
    val categoryLabel = stringResource(
        if (viewModel.selectedTab.value == "Income") R.string.incomeCategory else R.string.expenseCategory
    )
    val dateLabel = stringResource(
        if (viewModel.selectedTab.value == "Income") R.string.incomeDate else R.string.expenseDate
    )
    val noteLabel = stringResource(
        if (viewModel.selectedTab.value == "Income") R.string.incomeNote else R.string.expenseNote
    )

//    // Apply background layout color based on the theme
//    BackgroundLayout()
//
    SelectTransaction(
        showTabs = true,
        tabOptions = listOf("Expenses", "Income"),
        onTabSelected = { viewModel.selectedTab.value = it }
    )

    Spacer(modifier = Modifier.height(20.dp))

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
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface), // Dynamic color
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surface) // Dynamic background color
                    .padding(18.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {

                Spacer(modifier = Modifier.height(20.dp))

                SimpleTextField(
                    title = amountLabel,
                    value = amountState.value,
// <<<<<<< fixing-issue-icons-dialogs
                    onValueChange = { amountState.value = it },
                )
// =======
                    onValueChange = { amountState.value = it })
// >>>>>>> dev

                Spacer(modifier = Modifier.height(20.dp))

                CustomDropdownMenu(
                    categoryLabel,
                    categoryOptions = categoryOptions,
                    selectedOption = categoryState.value,
                    onOptionSelected = { categoryState.value = it }
                )

                Spacer(modifier = Modifier.height(20.dp))

                SimpleTextField(
                    title = dateLabel,
                    value = dateState.value,
                    onValueChange = { dateState.value = it },
                    onIconClick = { datePickerDialog.show() },
                )

                Spacer(modifier = Modifier.height(20.dp))

                SimpleTextField(
                    title = noteLabel,
                    value = noteState.value,
                    onValueChange = { noteState.value = it },
                )

                Spacer(modifier = Modifier.height(20.dp))

                SimpleButton("Save") {
                    if (viewModel.isTransactionValid()) {
                        Toast.makeText(context, "Added successfully!", Toast.LENGTH_SHORT).show()
                        navigateToHome = true
                    } else {
                        val amount = viewModel.getAmountState().value
                        if (amount.isBlank() || amount.toDoubleOrNull() == null) {
                            Toast.makeText(
                                context,
                                "Please enter a valid amount",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            Toast.makeText(
                                context,
                                "Please fill in all required fields",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }
    }
}
