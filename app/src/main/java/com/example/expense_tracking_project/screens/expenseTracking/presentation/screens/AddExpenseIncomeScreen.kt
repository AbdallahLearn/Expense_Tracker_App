package com.example.expense_tracking_project.screens.expenseTracking.presentation.screens

import android.os.Build
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.example.expense_tracking_project.R
import com.example.expense_tracking_project.navigation.Screen
import com.example.expense_tracking_project.screens.authentication.presentation.component.BackgroundLayout
import com.example.expense_tracking_project.screens.authentication.presentation.component.CustomDropdownMenu
import com.example.expense_tracking_project.screens.authentication.presentation.component.DesignScreen
import com.example.expense_tracking_project.screens.authentication.presentation.component.FormField
import com.example.expense_tracking_project.screens.authentication.presentation.component.SelectTransaction
import com.example.expense_tracking_project.screens.authentication.presentation.component.SimpleButton
import com.example.expense_tracking_project.screens.authentication.presentation.component.SimpleTextField
import com.example.expense_tracking_project.utils.isAtLeastOreo
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AddExpenseScreen(
    navController: NavController
) {
    val context = LocalContext.current
    var navigateToHome by remember { mutableStateOf(false) }

    // Calendar to choose the date (By default the date as current date)
    val formatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy", Locale.ENGLISH)
    val currentDate = LocalDate.now()

    // Form fields for the Expenses
    val expensesAmountState = remember { mutableStateOf("") }
    val expensesCategoryState = remember { mutableStateOf("") }
    val expensesDateState = remember { mutableStateOf(currentDate.format(formatter)) }
    val expensesNoteState = remember { mutableStateOf("") }

    // Form fields for the Income
    val incomeAmountState = remember { mutableStateOf("") }
    val incomeCategoryState = remember { mutableStateOf("") }
    val incomeDateState = remember { mutableStateOf(currentDate.format(formatter)) }
    val incomeNoteState = remember { mutableStateOf("") }

    // Calendar 
    val calendar = java.util.Calendar.getInstance()
    val datePickerDialog = remember {
        android.app.DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                val selectedDate = LocalDate.of(year, month + 1, dayOfMonth)
                val formattedDate = selectedDate.format(formatter)
                expensesDateState.value = formattedDate
                incomeDateState.value = formattedDate
            },
            calendar.get(java.util.Calendar.YEAR),
            calendar.get(java.util.Calendar.MONTH),
            calendar.get(java.util.Calendar.DAY_OF_MONTH)
        )
    }

    LaunchedEffect(navigateToHome) {
        if (navigateToHome) {
            navController.navigate(Screen.Home)
        }
    }

    var selectedTab by remember { mutableStateOf("Expenses") }

    val amountState = if (selectedTab == "Income") incomeAmountState else expensesAmountState
    val categoryState = if (selectedTab == "Income") incomeCategoryState else expensesCategoryState
    val dateState = if (selectedTab == "Income") incomeDateState else expensesDateState
    val noteState = if (selectedTab == "Income") incomeNoteState else expensesNoteState

    val amountLabel =
        if (selectedTab == "Income") stringResource(R.string.incomeAmount) else stringResource(R.string.expenseAmount)
    val categoryLabel =
        if (selectedTab == "Income") stringResource(R.string.incomeCategory) else stringResource(R.string.expenseCategory)
    val dateLabel =
        if (selectedTab == "Income") stringResource(R.string.incomeDate) else stringResource(R.string.expenseDate)
    val noteLabel =
        if (selectedTab == "Income") stringResource(R.string.incomeNote) else stringResource(R.string.expenseNote)


        BackgroundLayout()

        SelectTransaction(
            showTabs = true,
            tabOptions = listOf("Expenses", "Income"),
            onTabSelected = { selectedTab = it }
        )

        Spacer(modifier = Modifier.height(20.dp))

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 100.dp, start = 24.dp, end = 24.dp, bottom = 100.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                shape = RoundedCornerShape(32.dp),
                elevation = CardDefaults.cardElevation(8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White)
                        .padding(18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {

                    Spacer(modifier = Modifier.height(100.dp))

                    SimpleTextField(
                        title = amountLabel,
                        value = amountState.value,
                        onValueChange = { amountState.value = it })

                    Spacer(modifier = Modifier.height(20.dp))

                    CustomDropdownMenu(
                        categoryLabel,
                        categoryOptions = if (selectedTab == "Income") {
                            listOf("Salary", "Bonus", "Freelance", "Investment")
                        } else {
                            listOf("Food", "Shopping", "Entertainment", "Bills")
                        },
                        selectedOption = categoryState.value,
                        onOptionSelected = { categoryState.value = it }
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    SimpleTextField(
                        title = dateLabel,
                        value = dateState.value,
                        onValueChange = { dateState.value = it },
                        onIconClick = { datePickerDialog.show() }
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    SimpleTextField(
                        title = noteLabel,
                        value = noteState.value,
                        onValueChange = { noteState.value = it })

                    Spacer(modifier = Modifier.height(20.dp))

                    // Button Save
                    SimpleButton("Save") {}


                }
        }
    }
}

