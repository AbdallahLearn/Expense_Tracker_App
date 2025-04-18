package com.example.expense_tracking_project.screens.expenseTracking.presentation.vmModels

import android.app.DatePickerDialog
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
class EditBudgetViewModel @Inject constructor(
    private val insertBudgetUseCase: InsertBudgetUseCase,
    private val getAllBudgetsUseCase: GetAllbudgetsUseCase,
    private val updateBudgetUseCase: UpdateBudgetUseCase
) : ViewModel() {

    private val formatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy", Locale.ENGLISH)

    val budgetList = mutableStateOf<List<BudgetEntity>>(emptyList())
    val budgetId = mutableStateOf<Int?>(null)
    val budgetAmount = mutableStateOf("")
    val startDate = mutableStateOf("")
    val selectedInterval = mutableStateOf("")

    fun saveBudget(onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        if (budgetAmount.value.isBlank() || selectedInterval.value.isBlank()) {
            onFailure("Please fill all fields")
            return
        }

        val amount = budgetAmount.value.toDoubleOrNull()
        if (amount == null || amount <= 0.0) {
            onFailure("Invalid amount")
            return
        }

        try {
            val start = LocalDate.parse(startDate.value, formatter)
            val intervalMonths = selectedInterval.value.split(" ")[0].toIntOrNull() ?: 1
            val end = start.plusMonths(intervalMonths.toLong())
            val now = Date()

            val budget = BudgetEntity(
                budgetId = budgetId.value ?: 0,
                name = "Budget",
                totalAmount = amount,
                startDate = Date.from(start.atStartOfDay(systemDefault()).toInstant()),
                endDate = Date.from(end.atStartOfDay(systemDefault()).toInstant()),
                createdAt = now,
                updatedAt = now
            )

            viewModelScope.launch {
                if (budgetId.value == null) {
                    insertBudgetUseCase(budget)
                } else {
                    updateBudgetUseCase(budget.copy(updatedAt = now))
                }
                loadBudgets()
                budgetId.value = null
                onSuccess()
            }
        } catch (e: Exception) {
            onFailure("Error saving budget: ${e.localizedMessage}")
        }
    }

    fun loadBudgets() {
        viewModelScope.launch {
            budgetList.value = getAllBudgetsUseCase()
        }
    }

    fun loadBudgetById(id: Int) {
        viewModelScope.launch {
            getAllBudgetsUseCase().find { it.budgetId == id }?.let { budget ->
                budgetId.value = budget.budgetId
                budgetAmount.value = budget.totalAmount.toString()
                startDate.value = budget.startDate.toInstant()
                    .atZone(systemDefault())
                    .toLocalDate()
                    .format(formatter)

                val intervalMonths = budget.endDate.toInstant().atZone(systemDefault())
                    .toLocalDate().monthValue -
                        budget.startDate.toInstant().atZone(systemDefault())
                            .toLocalDate().monthValue

                selectedInterval.value = "${intervalMonths.coerceAtLeast(1)} Month"
            }
        }
    }

    fun softDeleteBudget(budget: BudgetEntity) {
        viewModelScope.launch {
            updateBudgetUseCase(
                budget.copy(isDeleted = true, updatedAt = Date())
            )
            loadBudgets()
        }
    }

    fun getStartDatePicker(context: Context): DatePickerDialog {
        val calendar = Calendar.getInstance()
        val parts = startDate.value.split(", ", " ").filter { it.isNotBlank() }
        if (parts.size >= 4) {
            calendar.set(Calendar.DAY_OF_MONTH, parts[1].toInt())
            calendar.set(Calendar.MONTH, convertMonthNameToInt(parts[2]))
            calendar.set(Calendar.YEAR, parts[3].toInt())
        }

        return DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                startDate.value = LocalDate.of(year, month + 1, dayOfMonth).format(formatter)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
    }

    private fun convertMonthNameToInt(month: String): Int {
        return listOf(
            "jan",
            "feb",
            "mar",
            "apr",
            "may",
            "jun",
            "jul",
            "aug",
            "sep",
            "oct",
            "nov",
            "dec"
        )
            .indexOf(month.lowercase(Locale.ENGLISH))
            .coerceAtLeast(0)
    }
}
