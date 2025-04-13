package com.example.expense_tracking_project.presentation.ui

import android.os.Build
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.example.expense_tracking_project.R
import com.example.expense_tracking_project.navigation.Screen
import com.example.expense_tracking_project.presentation.ui.resetPassword.DesignScreen
import com.example.expense_tracking_project.presentation.ui.resetPassword.FormField
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

    // Format: (Fri, 22 Feb 2025)
    val formatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy", Locale.ENGLISH)
    val currentDate = LocalDate.now()

    // Define the form fields
    val amountState = remember { mutableStateOf("") }
    val categoryState = remember { mutableStateOf("") }
    val dateState = remember { mutableStateOf(currentDate.format(formatter)) }
    val noteState = remember { mutableStateOf("") }

    // DatePickerDialog
    val calendar = java.util.Calendar.getInstance()
    val datePickerDialog = remember {
        android.app.DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                val selectedDate = LocalDate.of(year, month + 1, dayOfMonth)
                dateState.value = selectedDate.format(formatter)
            },
            calendar.get(java.util.Calendar.YEAR),
            calendar.get(java.util.Calendar.MONTH),
            calendar.get(java.util.Calendar.DAY_OF_MONTH)
        )
    }

    val fields = listOf(
        FormField(label = stringResource(R.string.amount), value = amountState.value),
        FormField(label = stringResource(R.string.category), value = categoryState.value),
        FormField(label = stringResource(R.string.date), value = dateState.value),
        FormField(label = stringResource(R.string.note), value = noteState.value),
    )
    val fieldStates = listOf(amountState, categoryState, dateState, noteState)

    // Handle navigation to the Home screen after successful add
    LaunchedEffect(navigateToHome) {
        if (navigateToHome) {
            navController.navigate(Screen.Home.route)
        }
    }

    // Design Screen UI
    DesignScreen(
        fields = fields,
        fieldStates = fieldStates,
        buttonText = stringResource(R.string.save),
        onButtonClick = {
            if (amountState.value.isNotBlank() &&
                categoryState.value.isNotBlank() &&
                dateState.value.isNotBlank() &&
                noteState.value.isNotBlank()
            ) {
                Toast.makeText(context, "Added Expense successfully", Toast.LENGTH_SHORT).show()
                navigateToHome = true
            } else {
                Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }
    )
}