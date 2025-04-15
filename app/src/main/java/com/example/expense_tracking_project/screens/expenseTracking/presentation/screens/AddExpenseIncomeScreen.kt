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
import com.example.expense_tracking_project.utils.isAtLeastOreo
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun AddExpenseScreen(
    navController: NavController
) {
    val context = LocalContext.current
    var navigateToHome by remember { mutableStateOf(false) }

    // API Level check using utility function
    if (!isAtLeastOreo()) {
        Toast.makeText(context, "This feature requires Android 8.0 (API 26) or higher", Toast.LENGTH_LONG).show()
        return
    }

    val formatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy", Locale.ENGLISH)
    val currentDate = LocalDate.now()

    // Form fields
    val expensesAmountState = remember { mutableStateOf("") }
    val expensesCategoryState = remember { mutableStateOf("") }
    val expensesDateState = remember { mutableStateOf(currentDate.format(formatter)) }
    val expensesNoteState = remember { mutableStateOf("") }

    val incomeAmountState = remember { mutableStateOf("") }
    val incomeCategoryState = remember { mutableStateOf("") }
    val incomeDateState = remember { mutableStateOf(currentDate.format(formatter)) }
    val incomeNoteState = remember { mutableStateOf("") }

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

    val expensesFields = listOf(
        FormField(label = stringResource(R.string.expenseAmount), value = expensesAmountState.value),
        FormField(label = stringResource(R.string.expenseCategory), value = expensesCategoryState.value),
        FormField(
            label = stringResource(R.string.expenseDate),
            value = expensesDateState.value,
            onClick = { datePickerDialog.show() }
        ),
        FormField(label = stringResource(R.string.expenseNote), value = expensesNoteState.value),
    )

    val expensesFieldStates = listOf(
        expensesAmountState, expensesCategoryState, expensesDateState, expensesNoteState
    )

    val incomeFields = listOf(
        FormField(label = stringResource(R.string.incomeAmount), value = incomeAmountState.value),
        FormField(label = stringResource(R.string.incomeCategory), value = incomeCategoryState.value),
        FormField(
            label = stringResource(R.string.incomeDate),
            value = incomeDateState.value,
            onClick = { datePickerDialog.show() }
        ),
        FormField(label = stringResource(R.string.incomeNote), value = incomeNoteState.value),
    )

    val incomeFieldStates = listOf(
        incomeAmountState, incomeCategoryState, incomeDateState, incomeNoteState
    )

    var selectedTab by remember { mutableStateOf("expenses") }

    val fields = if (selectedTab == "income") incomeFields else expensesFields
    val fieldStates = if (selectedTab == "income") incomeFieldStates else expensesFieldStates

    LaunchedEffect(navigateToHome) {
        if (navigateToHome) {
            navController.navigate(Screen.Home)
        }
    }

    DesignScreen(
        showTabs = true,
        fields = fields,
        fieldStates = fieldStates,
        buttonText = stringResource(R.string.save),
        onButtonClick = {
            val requiredFields = if (selectedTab == "income") {
                listOf(incomeAmountState, incomeCategoryState, incomeDateState)
            } else {
                listOf(expensesAmountState, expensesCategoryState, expensesDateState)
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
