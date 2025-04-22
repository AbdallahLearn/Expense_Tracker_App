package com.example.expense_tracking_project.screens.expenseTracking.presentation.vmModels

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.expense_tracking_project.core.local.entities.BudgetEntity
import com.example.expense_tracking_project.core.local.entities.Category
import com.example.expense_tracking_project.screens.expenseTracking.domain.usecase.budgetusecase.GetAllbudgetsUseCase
import com.example.expense_tracking_project.screens.expenseTracking.domain.usecase.categoryusecase.GetAllCategoriesUseCase
import com.example.expense_tracking_project.screens.expenseTracking.domain.usecase.categoryusecase.InsertCategoryUseCase
import com.example.expense_tracking_project.screens.expenseTracking.domain.usecase.categoryusecase.SoftDeleteCategoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject



@HiltViewModel
@RequiresApi(Build.VERSION_CODES.O)
class EditCategoryViewModel @Inject constructor(
    private val insertCategoryUseCase: InsertCategoryUseCase,
    private val getAllCategoriesUseCase: GetAllCategoriesUseCase,
    private val softDeleteCategoryUseCase: SoftDeleteCategoryUseCase,
    private  val getAllbudgetsUseCase: GetAllbudgetsUseCase

) : ViewModel() {
    val categories = mutableStateOf<List<Category>>(emptyList())
    val budgetList = mutableStateOf<List<BudgetEntity>>(emptyList())
    var selectedBudgetId = mutableStateOf<Int?>(null)
    val budget = mutableStateOf("")

    val categoryName = mutableStateOf("")
    val categoryColor= mutableStateOf("")
    val categoryType = mutableStateOf("")
    val note = mutableStateOf("")
    val editingCategoryId = mutableStateOf<Int?>(null)
    fun saveCategory(
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        if (categoryName.value.isBlank() || categoryType.value.isBlank()) {
            onFailure("Please fill required fields")
            return
        }

        val category = Category(
            categoryId = editingCategoryId.value ?: 0,
            budgetId = selectedBudgetId.value,
            categoryName = categoryName.value,
            type = categoryType.value,
            color = categoryColor.value,
            isDeleted = false,
            createdAt = Date(),
            updatedAt = Date()
        )

        viewModelScope.launch {
            try {
                insertCategoryUseCase(category)
                editingCategoryId.value = null
                onSuccess()
            } catch (e: Exception) {
                onFailure(e.localizedMessage ?: "Unknown error")
            }
        }
    }
    fun loadCategories() {
        viewModelScope.launch {
            categories.value = getAllCategoriesUseCase()
        }
    }
    fun softDeleteCategory(categoryId: Int, onComplete: () -> Unit) {
        viewModelScope.launch {
            softDeleteCategoryUseCase(categoryId)
            loadCategories() // refresh the list
            onComplete()
        }
    }
    fun loadCategoryById(categoryId: Int) {
        viewModelScope.launch {
            val category = getAllCategoriesUseCase().find { it.categoryId == categoryId }
            category?.let {
                editingCategoryId.value = it.categoryId
                categoryName.value = it.categoryName
                categoryColor.value = it.color
                categoryType.value = it.type
                budget.value = it.budgetId.toString()
                // note.value = it.note ?: ""
            }
        }
    }

    fun loadBudgets() {
        viewModelScope.launch {
            budgetList.value = getAllbudgetsUseCase()
        }
    }


}





