package com.example.expense_tracking_project.screens.expenseTracking.presentation.vmModels

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expense_tracking_project.core.local.entities.Category
import com.example.expense_tracking_project.screens.expenseTracking.domain.usecase.categoryusecase.GetAllCategoriesUseCase
import com.example.expense_tracking_project.screens.expenseTracking.domain.usecase.categoryusecase.InsertCategoryUseCase
import com.example.expense_tracking_project.screens.expenseTracking.domain.usecase.categoryusecase.SoftDeleteCategoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject


@HiltViewModel
@RequiresApi(Build.VERSION_CODES.O)
class EditCategoryViewModel @Inject constructor(
    private val insertCategoryUseCase: InsertCategoryUseCase,
    private val getAllCategoriesUseCase: GetAllCategoriesUseCase,
    private val softDeleteCategoryUseCase: SoftDeleteCategoryUseCase

) : ViewModel() {
    val categories = mutableStateOf<List<Category>>(emptyList())

    val categoryName = mutableStateOf("")
    val categoryType = mutableStateOf("")
    val budget = mutableStateOf("")
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
            budgetId = budget.value.toIntOrNull() ?: 0,
            categoryName = categoryName.value,
            type = categoryType.value,
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
                categoryType.value = it.type
                budget.value = it.budgetId.toString()
                // note.value = it.note ?: ""
            }
        }
    }


}






