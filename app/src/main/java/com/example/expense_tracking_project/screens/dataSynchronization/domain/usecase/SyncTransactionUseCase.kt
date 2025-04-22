package com.example.expense_tracking_project.screens.dataSynchronization.domain.usecase

import com.example.expense_tracking_project.screens.dataSynchronization.domain.repository.SyncTransactionRepository
import javax.inject.Inject

class SyncTransactionUseCase @Inject constructor( // get unSynced transactions from Room , send them , marks them as synced, downloads budget from server and stores them locally
    private val syncTransactionRepository: SyncTransactionRepository
) {
    suspend fun execute() {
        syncTransactionRepository.syncTransactions()
    }
}