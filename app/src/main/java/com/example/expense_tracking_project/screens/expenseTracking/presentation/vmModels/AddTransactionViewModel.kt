package com.example.expense_tracking_project.screens.expenseTracking.presentation.vmModels

import android.app.DatePickerDialog
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
class AddTransactionViewModel : ViewModel() {

    // Format the date (Mon, 14 Apr 2025)
    private val formatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy", Locale.ENGLISH)

    // Default selection type is Expenses
    val selectedTab = mutableStateOf("Expenses")

    // Expense transaction state variables
    val expensesAmount = mutableStateOf("")
    val expensesCategory = mutableStateOf("")
    val expensesDate = mutableStateOf(LocalDate.now().format(formatter))
    val expensesNote = mutableStateOf("")

    // Income transaction state variables
    val incomeAmount = mutableStateOf("")
    val incomeCategory = mutableStateOf("")
    val incomeDate = mutableStateOf(LocalDate.now().format(formatter))
    val incomeNote = mutableStateOf("")

    // Get the State based on the selectedTab, either Income or Expenses
    fun getAmountState() = if (selectedTab.value == "Income") incomeAmount else expensesAmount
    fun getCategoryState() = if (selectedTab.value == "Income") incomeCategory else expensesCategory
    fun getDateState() = if (selectedTab.value == "Income") incomeDate else expensesDate
    fun getNoteState() = if (selectedTab.value == "Income") incomeNote else expensesNote

    // Predefined Categories
    fun getCategoryOptions(): List<String> {
        return if (selectedTab.value == "Income") {
            listOf("Salary", "Bonus", "Freelance", "Investment")
        } else {
            listOf("Food", "Shopping", "Entertainment", "Bills")
        }
    }

    // Update the date by what the user choose using Calendar
    fun updateDate(context: Context): DatePickerDialog {
        val calendar = Calendar.getInstance()
        return DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                val selectedDate = LocalDate.of(year, month + 1, dayOfMonth).format(formatter)
                if (selectedTab.value == "Income") {
                    incomeDate.value = selectedDate
                } else {
                    expensesDate.value = selectedDate
                }
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
    }

    // Function to check the validation of the transaction
    fun isTransactionValid(): Boolean {
        val amount = getAmountState().value
        val category = getCategoryState().value
        val date = getDateState().value
        // Check if amount is not blank and is a valid number
        val isAmountValid = amount.isNotBlank() && amount.toDoubleOrNull() != null
        return isAmountValid && category.isNotBlank() && date.isNotBlank()
    }

}