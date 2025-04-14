package com.example.expense_tracking_project.screens.expenseTracking.presentation.screens

import android.os.Build
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.expense_tracking_project.R
import com.example.expense_tracking_project.navigation.Screen
import com.example.expense_tracking_project.screens.authentication.presentation.component.DesignScreen
import com.example.expense_tracking_project.screens.authentication.presentation.component.FormField
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

//@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AddExpenseScreen(
    navController: NavController
) {
    val context = LocalContext.current
    var navigateToHome by remember { mutableStateOf(false) }

    // Format: (Fri, 22 Feb 2025)
    val formatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy", Locale.ENGLISH)
    val currentDate = LocalDate.now()

    // Define the form fields for expenses
    val expensesamountState = remember { mutableStateOf("") }
    val expensescategoryState = remember { mutableStateOf("") }
    val expensesdateState = remember { mutableStateOf(currentDate.format(formatter)) }
    val expensesnoteState = remember { mutableStateOf("") }

    // Define the form fields for income
    val incomeamountState = remember { mutableStateOf("") }
    val incomecategoryState = remember { mutableStateOf("") }
    val incomedateState = remember { mutableStateOf(currentDate.format(formatter)) }
    val incomenoteState = remember { mutableStateOf("") }

    // DatePickerDialog
    val calendar = java.util.Calendar.getInstance()
    val datePickerDialog = remember {
        android.app.DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                val selectedDate = LocalDate.of(year, month + 1, dayOfMonth)
                expensesdateState.value = selectedDate.format(formatter)
                incomedateState.value = selectedDate.format(formatter)
            },
            calendar.get(java.util.Calendar.YEAR),
            calendar.get(java.util.Calendar.MONTH),
            calendar.get(java.util.Calendar.DAY_OF_MONTH)
        )
    }

    val expensesFields = listOf(
        FormField(label = stringResource(R.string.expenseAmount), value = expensesamountState.value),
        FormField(label = stringResource(R.string.expenseCategory), value = expensescategoryState.value),
        FormField(
            label = stringResource(R.string.expenseDate),
            value = expensesdateState.value,
            onClick = {
                datePickerDialog.show()
            }
        ),
        FormField(label = stringResource(R.string.expenseNote), value = expensesnoteState.value),
    )

    val expensesFieldStates = listOf(expensesamountState, expensescategoryState, expensesdateState, expensesnoteState)

    val incomeFields = listOf(
        FormField(label = stringResource(R.string.incomeAmount), value = incomeamountState.value),
        FormField(label = stringResource(R.string.incomeCategory), value = incomecategoryState.value),
        FormField(
            label = stringResource(R.string.incomeDate),
            value = incomedateState.value,
            onClick = {
                datePickerDialog.show()
            }
        ),
        FormField(label = stringResource(R.string.incomeNote), value = incomenoteState.value),
    )

    val incomeFieldStates = listOf(incomeamountState, incomecategoryState, incomedateState, incomenoteState)

    // Track which tab is selected
    var selectedTab by remember { mutableStateOf("expenses") }

    val fields = if (selectedTab == "income") incomeFields else expensesFields
    val fieldStates = if (selectedTab == "income") incomeFieldStates else expensesFieldStates

    // Handle navigation to the Home screen after successful add
    LaunchedEffect(navigateToHome) {
        if (navigateToHome) {
            navController.navigate(Screen.Home.route)
        }
    }

    // Design Screen UI
    DesignScreen(
        showTabs = true,
        fields = fields,
        fieldStates = fieldStates,
        buttonText = stringResource(R.string.save),
        onButtonClick = {
            val requiredFields = if (selectedTab == "income") {
                listOf(incomeamountState, incomecategoryState, incomedateState)
            } else {
                listOf(expensesamountState, expensescategoryState, expensesdateState)
            }
                if (requiredFields.all { it.value.isNotBlank() }) {
                Toast.makeText(
                    context,
                    if (selectedTab == "income") "Added Income successfully" else "Added Expenses successfully",
                    Toast.LENGTH_SHORT
                ).show()
                navigateToHome = true
            } else {
                Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        },
        onTabSelected = { tab ->
            selectedTab = tab.lowercase()
        }
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun AddExpenseScreenPreview() {
    // Provide a basic NavController for preview
    val navController = rememberNavController()

    AddExpenseScreen(navController = navController)
}
