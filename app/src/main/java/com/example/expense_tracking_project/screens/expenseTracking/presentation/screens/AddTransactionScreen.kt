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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.expense_tracking_project.R
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
    transactionId: Int? = null,
    viewModel: AddTransactionViewModel = hiltViewModel(),
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
    LaunchedEffect(transactionId) {
        if (transactionId != null) {
                viewModel.loadTransactionById(transactionId)
        } else {
            viewModel.resetForm()
        }
    }

    // Observe State from the ViewModel
    val amount = viewModel.amount
    val categoryName = viewModel.categoryName
    val date = viewModel.date
    val note = viewModel.note
    val selectedTab = viewModel.selectedTab

    // for the category options
    val categoryOptions by remember(
        viewModel.categoryList.value,
        viewModel.selectedTab.value
    ) {
        mutableStateOf(
            viewModel.getCategoryOptions() + viewModel.categoryList.value.map { it.categoryName }
        )
    }

    // text fields based on the selected transaction type (Income / Expenses)
    val amountLabel = stringResource(
        if (selectedTab.value == "Income") R.string.incomeAmount else R.string.expenseAmount
    )
    val categoryLabel = stringResource(
        if (selectedTab.value == "Income") R.string.incomeCategory else R.string.expenseCategory
    )
    val dateLabel = stringResource(
        if (selectedTab.value == "Income") R.string.incomeDate else R.string.expenseDate
    )
    val noteLabel = stringResource(
        if (selectedTab.value == "Income") R.string.incomeNote else R.string.expenseNote
    )

    SelectTransaction(
        showTabs = true,
        tabOptions = listOf(
            "Expense",
            "Income"
        ),
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
                    title = "Amount",
                    value = amount.value,
                    onValueChange = { amount.value = it },
                    showDollarIcon = true
                )


                Spacer(modifier = Modifier.height(20.dp))

                CustomDropdownMenu(
                    categoryLabel,
                    categoryOptions = categoryOptions,
                    selectedOption = categoryName.value,
                    onOptionSelected = { categoryName.value = it }
                )

                Spacer(modifier = Modifier.height(20.dp))

                SimpleTextField(
                    title = dateLabel,
                    value = date.value,
                    onValueChange = { date.value = it },
                    onIconClick = { datePickerDialog.show() },
                )

                Spacer(modifier = Modifier.height(20.dp))

                SimpleTextField(
                    title = noteLabel,
                    value = note.value,
                    onValueChange = { note.value = it },
                )

                Spacer(modifier = Modifier.height(20.dp))

                SimpleButton(stringResource(R.string.save)) {
                    if (viewModel.isTransactionValid()) {
                        viewModel.saveTransaction {
                            Toast.makeText(
                                context,
                                if (transactionId != null) context.getString(R.string.updated_successfully)
                                else context.getString(R.string.added_successfully),
                                Toast.LENGTH_SHORT
                            ).show()
                            navigateToHome = true
                        }
                    } else {
                        if (amount.value.isBlank() || amount.value.toDoubleOrNull() == null) {
                            Toast.makeText(
                                context,
                                context.getString(R.string.please_enter_valid_amount),
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            Toast.makeText(
                                context,
                                context.getString(R.string.please_fill_required_fields),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }
    }
}