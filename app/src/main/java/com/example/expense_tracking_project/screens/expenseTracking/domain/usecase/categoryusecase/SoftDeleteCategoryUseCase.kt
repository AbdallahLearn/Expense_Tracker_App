package com.example.expense_tracking_project.screens.expenseTracking.domain.usecase.categoryusecase

import com.example.expense_tracking_project.screens.expenseTracking.domain.repository.CategoryRepository
import javax.inject.Inject

class SoftDeleteCategoryUseCase @Inject constructor(
    private val categoryRepository: CategoryRepository
) {
    suspend operator fun invoke(categoryId: Int) {
        categoryRepository.softDeleteCategory(categoryId)
    }
}