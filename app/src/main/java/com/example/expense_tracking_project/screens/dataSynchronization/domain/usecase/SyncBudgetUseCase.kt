package com.example.expense_tracking_project.screens.dataSynchronization.domain.usecase

import com.example.expense_tracking_project.screens.dataSynchronization.domain.repository.SyncRepository
import javax.inject.Inject

class SyncBudgetsUseCase @Inject constructor( // get unsynced budgets from Room , send them , marks them as synced, downloads budget from server and stores them locally
    private val syncRepository: SyncRepository
) {
    suspend fun execute() {
        syncRepository.syncBudgets()
        syncRepository.getBudgetsFromApi()
    }
}