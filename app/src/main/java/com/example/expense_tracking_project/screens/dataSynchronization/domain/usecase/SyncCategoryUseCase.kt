package com.example.expense_tracking_project.screens.dataSynchronization.domain.usecase

import com.example.expense_tracking_project.screens.dataSynchronization.domain.repository.SyncCategoryRepository
import javax.inject.Inject


class SyncCategoryUseCase @Inject constructor(
    private val syncCategoryRepository: SyncCategoryRepository
) {
    suspend fun execute() {
        syncCategoryRepository.syncCategory()
    }
}