package com.example.expense_tracking_project.screens.expenseTracking.presentation.vmModels

import android.app.DatePickerDialog
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expense_tracking_project.core.local.dao.CategoryDao
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import com.example.expense_tracking_project.core.local.data.PredefinedCategoryProvider
import com.example.expense_tracking_project.core.local.entities.Category
import com.example.expense_tracking_project.core.local.entities.Transaction
import com.example.expense_tracking_project.screens.dataSynchronization.domain.usecase.SyncTransactionUseCase
import com.example.expense_tracking_project.screens.expenseTracking.domain.usecase.transactionsusecase.GetAllTransactionsUseCase
import com.example.expense_tracking_project.screens.expenseTracking.domain.usecase.transactionsusecase.InsertTransactionUseCase
import com.example.expense_tracking_project.screens.expenseTracking.domain.usecase.transactionsusecase.UpdateTransactionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import androidx.compose.runtime.State
import kotlinx.coroutines.launch
import java.time.ZoneId
import javax.inject.Inject

@HiltViewModel
@RequiresApi(Build.VERSION_CODES.O)
class AddTransactionViewModel @Inject constructor(
    private val insertTransactionUseCase: InsertTransactionUseCase,
    private val updateTransactionUseCase: UpdateTransactionUseCase,
    private val getAllTransactionsUseCase: GetAllTransactionsUseCase,
    private val syncTransactionUseCase: SyncTransactionUseCase,
    private val categoryDao: CategoryDao
) : ViewModel() {

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

    val categoryList = mutableStateOf<List<Category>>(emptyList()) // <- List of categories

    private val _categories = mutableStateOf<List<Category>>(emptyList())
    val categories: State<List<Category>> = _categories


    init {
        viewModelScope.launch {
            categoryList.value = categoryDao.getAllCategories()
        }
    }

    // Get the State based on the selectedTab, either Income or Expenses
    fun getAmountState() = if (selectedTab.value == "Income") incomeAmount else expensesAmount
    fun getCategoryState() = if (selectedTab.value == "Income") incomeCategory else expensesCategory
    fun getDateState() = if (selectedTab.value == "Income") incomeDate else expensesDate
    fun getNoteState() = if (selectedTab.value == "Income") incomeNote else expensesNote

    // Get the predefined categories
    fun getCategoryOptions(): List<String> {
        return PredefinedCategoryProvider.getCategoriesForType(selectedTab.value)
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

    private fun getSelectedCategoryId(): Int? {
        val categoryName = getCategoryState().value
        return _categories.value.firstOrNull { it.categoryName.equals(categoryName, ignoreCase = true) }?.categoryId
    }

    fun saveTransaction() {
        viewModelScope.launch {
            val amount = getAmountState().value.toDouble()
            val finalAmount = if (selectedTab.value == "Expenses") -amount else amount

            val selectedLocalDate = LocalDate.parse(getDateState().value, formatter)
            val convertedDate = Date.from(selectedLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant())

            val selectedCategoryName = getCategoryState().value
            val selectedCategoryId = categoryList.value.firstOrNull { it.categoryName == selectedCategoryName }?.categoryId

            val transaction = Transaction(
                amount = finalAmount,
                categoryId = selectedCategoryId,
                date = convertedDate,
                note = getNoteState().value,
                createdAt = Date(),
                updatedAt = Date()
            )
            insertTransactionUseCase(transaction)
        }
    }
}