
package com.example.expense_tracking_project.screens.expenseTracking.presentation.vmModels

import android.app.DatePickerDialog
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expense_tracking_project.core.local.data.PredefinedBudgetProvider
import com.example.expense_tracking_project.core.local.entities.BudgetEntity
import com.example.expense_tracking_project.screens.expenseTracking.domain.usecase.budgetusecase.GetAllbudgetsUseCase
import com.example.expense_tracking_project.screens.expenseTracking.domain.usecase.budgetusecase.InsertBudgetUseCase
import com.example.expense_tracking_project.screens.expenseTracking.domain.usecase.budgetusecase.UpdateBudgetUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneId.systemDefault
import java.time.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject

@HiltViewModel
@RequiresApi(Build.VERSION_CODES.O)
class EditBudgetCategoryViewModel @Inject constructor(
    private val insertBudgetUseCase: InsertBudgetUseCase,
    private val getAllBudgetsUseCase: GetAllbudgetsUseCase,
    private val updateBudgetUseCase : UpdateBudgetUseCase
) : ViewModel() {

    // Formatter for displaying date
    private val formatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy", Locale.ENGLISH)
    private val _budgetList = mutableStateOf<List<BudgetEntity>>(emptyList())
    val budgetList: State<List<BudgetEntity>> = _budgetList

    // State fields
    val budgetAmount = mutableStateOf("")
    val startDate = mutableStateOf(LocalDate.now().format(formatter))
    val selectedInterval = mutableStateOf("")
    val budget = mutableStateOf("") // This is the main budget field
    val note = mutableStateOf("")
    val categoryName = mutableStateOf("")
    val categoryType = mutableStateOf("")

    // Provide predefined intervals (optional)
    fun getOptionalBudgets(): List<String> {
        return PredefinedBudgetProvider.getAllBudgets()
    }

    // Launch date picker
    fun getStartDatePicker(context: Context): DatePickerDialog {
        val calendar = Calendar.getInstance()
        val dateParts = startDate.value.split(", ", " ").filter { it.isNotBlank() }
        if (dateParts.size >= 4) {
            calendar.set(Calendar.DAY_OF_MONTH, dateParts[1].toInt())
            calendar.set(Calendar.MONTH, convertMonthNameToInt(dateParts[2]))
            calendar.set(Calendar.YEAR, dateParts[3].toInt())
        }

        return DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                val selected = LocalDate.of(year, month + 1, dayOfMonth)
                startDate.value = selected.format(formatter)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
    }

    private fun convertMonthNameToInt(month: String): Int {
        return when (month.lowercase(Locale.ENGLISH)) {
            "jan" -> 0
            "feb" -> 1
            "mar" -> 2
            "apr" -> 3
            "may" -> 4
            "jun" -> 5
            "jul" -> 6
            "aug" -> 7
            "sep" -> 8
            "oct" -> 9
            "nov" -> 10
            "dec" -> 11
            else -> 0
        }
    }

    // Save budget logic
    fun saveBudget(onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        if (budgetAmount.value.isBlank() || selectedInterval.value.isBlank()) {
            onFailure("Please fill all fields")
            return
        }

        try {
            val amount = budgetAmount.value.toDoubleOrNull()
            if (amount == null || amount <= 0.0) {
                onFailure("Invalid amount")
                return
            }

            val start = LocalDate.parse(startDate.value, formatter)
            val intervalMonths = selectedInterval.value.split(" ")[0].toIntOrNull() ?: 1
            val end = start.plusMonths(intervalMonths.toLong())

            val now = Date()
            val budgetEntity = BudgetEntity(
                name = categoryName.value.ifBlank { "Default" },
                totalAmount = amount,
                startDate = Date.from(start.atStartOfDay(systemDefault()).toInstant()),
                endDate = Date.from(end.atStartOfDay(systemDefault()).toInstant()),
                createdAt = now,
                updatedAt = now
            )

            viewModelScope.launch {
                insertBudgetUseCase(budgetEntity)
                onSuccess()
            }

        } catch (e: Exception) {
            onFailure("Error saving budget: ${e.localizedMessage}")
        }
    }

    fun loadBudgets() {
        viewModelScope.launch {
            _budgetList.value = getAllBudgetsUseCase()
        }
    }

    fun softDeleteBudget(budget: BudgetEntity) {
        viewModelScope.launch {
            val updated = budget.copy(
                isDeleted = true,
                updatedAt = Date()
            )
            updateBudgetUseCase(updated)
            loadBudgets() // Refresh list
        }
    }

    // Save category logic (merged from SaveCategoryInDB)
    fun saveCategory(onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        if (categoryName.value.isBlank() || categoryType.value.isBlank()) {
            onFailure("Please fill required fields")
        } else {
            // Additional logic for saving category (if needed)
            onSuccess()
        }
    }
}