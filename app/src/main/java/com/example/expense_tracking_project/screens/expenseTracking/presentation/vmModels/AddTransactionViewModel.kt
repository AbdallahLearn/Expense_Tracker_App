package com.example.expense_tracking_project.screens.expenseTracking.presentation.vmModels

import android.app.DatePickerDialog
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import com.example.expense_tracking_project.core.local.data.PredefinedCategoryProvider
import com.example.expense_tracking_project.core.local.entities.Category
import com.example.expense_tracking_project.core.local.entities.Transaction
import com.example.expense_tracking_project.screens.dataSynchronization.domain.usecase.SyncTransactionUseCase
import com.example.expense_tracking_project.screens.expenseTracking.domain.usecase.transactionsusecase.InsertTransactionUseCase
import com.example.expense_tracking_project.screens.expenseTracking.domain.usecase.transactionsusecase.UpdateTransactionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import com.example.expense_tracking_project.core.local.dao.TransactionDao
import com.example.expense_tracking_project.screens.expenseTracking.domain.usecase.categoryusecase.GetAllCategoriesUseCase
import com.example.expense_tracking_project.screens.expenseTracking.domain.usecase.transactionsusecase.GetTransactionByIdUseCase
import kotlinx.coroutines.launch
import java.time.ZoneId
import java.time.ZoneId.systemDefault
import javax.inject.Inject
import kotlin.math.abs

@HiltViewModel
@RequiresApi(Build.VERSION_CODES.O)
class AddTransactionViewModel @Inject constructor(
    private val insertTransactionUseCase: InsertTransactionUseCase,
    private val updateTransactionUseCase: UpdateTransactionUseCase,
    private val getTransactionByIdUseCase: GetTransactionByIdUseCase,
    private val getAllCategoriesUseCase: GetAllCategoriesUseCase,
    private val syncTransactionUseCase: SyncTransactionUseCase,
    private val dao: TransactionDao
) : ViewModel() {

    // Format the date (Mon, 14 Apr 2025)
    private val formatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy", Locale.ENGLISH)

    // Editing state
    val editingTransactionId = mutableStateOf<Int?>(null)

    // Default selection type is Expenses
    val selectedTab = mutableStateOf("Expenses")

    // Transaction state variables
    val amount = mutableStateOf("")
    val categoryName = mutableStateOf("")
    val date = mutableStateOf(LocalDate.now().format(formatter))
    val note = mutableStateOf("")

    val categoryList = mutableStateOf<List<Category>>(emptyList())


    init {
        viewModelScope.launch {
            categoryList.value = getAllCategoriesUseCase()
        }
    }

    fun loadTransactionById(id: Int) {
        viewModelScope.launch {
            val transaction = getTransactionByIdUseCase(id)
            transaction?.let {
                editingTransactionId.value = id
                amount.value = abs(it.amount).toString()
                note.value = it.note ?: ""
                date.value = transaction.date.toInstant()
                    .atZone(systemDefault())
                    .toLocalDate()
                    .format(formatter)
                selectedTab.value = if (it.amount >= 0) "Income" else "Expenses"

                // Load category name
                it.categoryId?.let { categoryId ->
                    categoryName.value = categoryList.value
                        .firstOrNull { cat -> cat.categoryId == categoryId }
                        ?.categoryName ?: ""
                }
            }
        }
    }

    fun resetForm() {
        editingTransactionId.value = null
        selectedTab.value = "Expenses"
        amount.value = ""
        categoryName.value = ""
        date.value = LocalDate.now().format(formatter)
        note.value = ""
    }

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
                date.value = LocalDate.of(year, month + 1, dayOfMonth).format(formatter)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
    }

    private fun getSelectedCategoryId(): Int? {
        return categoryList.value.firstOrNull {
            it.categoryName.equals(categoryName.value, ignoreCase = true)
        }?.categoryId
    }

    fun isTransactionValid(): Boolean {
        return amount.value.isNotBlank() &&
                amount.value.toDoubleOrNull() != null &&
                categoryName.value.isNotBlank() &&
                date.value.isNotBlank()
    }

    fun saveTransaction(onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            if (!isTransactionValid()) return@launch

            val amountValue = amount.value.toDouble()
            val finalAmount = if (selectedTab.value == "Expenses") -amountValue else amountValue

            val selectedLocalDate = LocalDate.parse(date.value, formatter)
            val convertedDate = Date.from(selectedLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant())

            val transaction = Transaction(
                transactionId = editingTransactionId.value ?: 0,
                amount = finalAmount,
                categoryId = getSelectedCategoryId(),
                date = convertedDate,
                note = note.value,
                isDeleted = false,
                isSynced = editingTransactionId.value != null,
                createdAt = if (editingTransactionId.value == null) Date() else Date(),
                updatedAt = Date()
            )

            // Log the transaction to ensure values are correct
            Log.d("DEBUG", "Inserting transaction: $transaction")

            val category = categoryList.value.firstOrNull { it.categoryId == transaction.categoryId }
            Log.d("TRANSACTION", "Inserting transaction with localCategoryId = ${transaction.categoryId}, serverCategoryId = ${category?.categoryServerId}")

            if (editingTransactionId.value != null) {
                updateTransactionUseCase(transaction)
            } else {
                insertTransactionUseCase(transaction)
            }

            syncTransactionUseCase.execute()

            resetForm()
            onSuccess()
        }
    }

}