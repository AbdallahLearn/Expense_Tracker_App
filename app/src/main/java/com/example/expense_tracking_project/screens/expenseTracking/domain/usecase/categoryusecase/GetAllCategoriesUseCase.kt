package com.example.expense_tracking_project.screens.expenseTracking.domain.usecase.categoryusecase

import com.example.expense_tracking_project.core.local.entities.Category
import com.example.expense_tracking_project.screens.expenseTracking.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllCategoriesUseCase @Inject constructor(
    private val categoryRepository: CategoryRepository
) {
    suspend operator fun invoke(): List<Category> {
        return categoryRepository.getAllCategories()
    }
}