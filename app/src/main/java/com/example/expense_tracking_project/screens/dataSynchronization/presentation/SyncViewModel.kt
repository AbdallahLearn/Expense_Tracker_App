package com.example.expense_tracking_project.screens.dataSynchronization.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expense_tracking_project.core.connectivity.NetworkConnectivityObserver
import com.example.expense_tracking_project.screens.dataSynchronization.domain.usecase.SyncBudgetsUseCase
import com.example.expense_tracking_project.screens.dataSynchronization.domain.usecase.SyncCategoryUseCase
import com.example.expense_tracking_project.screens.dataSynchronization.domain.usecase.SyncTransactionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SyncViewModel @Inject constructor(
    private val syncBudgetsUseCase: SyncBudgetsUseCase,
    private val networkObserver : NetworkConnectivityObserver,
    private val syncCategoryUseCase: SyncCategoryUseCase,
    private val syncTransactionUseCase: SyncTransactionUseCase
) : ViewModel() {
    private val _syncStatus = MutableStateFlow<String?>(null)
    val syncStatus: StateFlow<String?> = _syncStatus // expose sync status

    init {
        viewModelScope.launch { // trigger sync when internet is available
            networkObserver.observe().collect { status ->
                if (status == "Available") {
                    syncNow()
                }
            }
        }
    }

    private fun syncNow() { // to call it from UI
        viewModelScope.launch {
            try {
                val token = "Bearer eyJhbGciOiJSUzI1NiIsImtpZCI6IjkwOTg1NzhjNDg4MWRjMDVlYmYxOWExNWJhMjJkOGZkMWFiMzRjOGEiLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJodHRwczovL3NlY3VyZXRva2VuLmdvb2dsZS5jb20vZXhwZW5zZS10cmFja2VyLTc4YjA3IiwiYXVkIjoiZXhwZW5zZS10cmFja2VyLTc4YjA3IiwiYXV0aF90aW1lIjoxNzQ1MzAyMDE1LCJ1c2VyX2lkIjoiTDZEZkdUUnV5M1FOYW9RcGlzeHlDWWFQU1psMiIsInN1YiI6Ikw2RGZHVFJ1eTNRTmFvUXBpc3h5Q1lhUFNabDIiLCJpYXQiOjE3NDUzMDIwMTUsImV4cCI6MTc0NTMwNTYxNSwiZW1haWwiOiJmYXJlZWhhQGF0b21jYW1wLmNvbSIsImVtYWlsX3ZlcmlmaWVkIjpmYWxzZSwiZmlyZWJhc2UiOnsiaWRlbnRpdGllcyI6eyJlbWFpbCI6WyJmYXJlZWhhQGF0b21jYW1wLmNvbSJdfSwic2lnbl9pbl9wcm92aWRlciI6InBhc3N3b3JkIn19.W1AFjk91VvHQTATSFpIa8YNyc8rX1igMiFoCjB8RlYoxTsH_ey3AzBuGUaA56W-r_-pIo2RAUfIqQdZM-KrjrcItmbgHg596Zfev1uQrHY6J-oF9BMaTOVi70Nr8mAzANMn_tPT_XytJBGMbsUlP1huJkcHJVIIAP1fxh9Z8jgwfDEnvtqIjL1vy_hCt9rJrU3st1QRR8DWvN2bCW68HHslVKg4S_KKWFfmFv11YohF1AmgU417ilKBEQOAW7zpVg2iDuxJzavLnS5niW9b7R2b9bm2QTLuAyAU7VQtFR7D0xLqx-vGD28RkdWgr3agWjxCV7crLi2rORpqI8rbvBQ"
                syncBudgetsUseCase.execute(token)
                syncCategoryUseCase.execute()
                syncTransactionUseCase.execute()
                _syncStatus.value = "Synced successfully"
            } catch (e: Exception) {
                _syncStatus.value = "Sync failed"
            }

        }
    }
}