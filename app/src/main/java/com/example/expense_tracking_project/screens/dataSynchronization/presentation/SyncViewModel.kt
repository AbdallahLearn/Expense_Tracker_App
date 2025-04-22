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
                val token =
                    "Bearer eyJhbGciOiJSUzI1NiIsImtpZCI6IjkwOTg1NzhjNDg4MWRjMDVlYmYxOWExNWJhMjJkOGZkMWFiMzRjOGEiLCJ0eXAiOiJKV1QifQ.eyJuYW1lIjoiYWJlZXIiLCJpc3MiOiJodHRwczovL3NlY3VyZXRva2VuLmdvb2dsZS5jb20vZXhwZW5zZS10cmFja2VyLTc4YjA3IiwiYXVkIjoiZXhwZW5zZS10cmFja2VyLTc4YjA3IiwiYXV0aF90aW1lIjoxNzQ1MzEyODYxLCJ1c2VyX2lkIjoiaDRCYmNoWm5NMVpqaVFCWHNBTXRyd3l1dk52MiIsInN1YiI6Img0QmJjaFpuTTFaamlRQlhzQU10cnd5dXZOdjIiLCJpYXQiOjE3NDUzMTI4NjEsImV4cCI6MTc0NTMxNjQ2MSwiZW1haWwiOiJhYnJhQGdtYWlsLmNvbSIsImVtYWlsX3ZlcmlmaWVkIjpmYWxzZSwiZmlyZWJhc2UiOnsiaWRlbnRpdGllcyI6eyJlbWFpbCI6WyJhYnJhQGdtYWlsLmNvbSJdfSwic2lnbl9pbl9wcm92aWRlciI6InBhc3N3b3JkIn19.EofkPu7vKUkuGlwT9rzBAIzSDjBPUlhJ9BxEKrP0tzmZYBVi7kuXLrTajyhVGPrX_nCF-VRS86YXpNM0PP6AVpClKrEEBr3gXPa1fvSuyicHWY2voDRFTTgdURbqSOpgcQzyNcppzsKNec1d3TH1r_ujiz48KCN7oCNDiBRKe21kHh5YhwLN7AMxPpVKSGwsem9Fw3FCbYU8dWP6tyI7R3vckJaoLyVoMid1T6Hov5ZU9u_Vbp2TL26vRpjAH1XgYU633ZxeR3Lnn1Oq2jMYsX7PH-3eG8ocGabQZRr4WBYc6mhC3m1y-q5CPl-UhRkhjHR_ZTd3I7Ku_DVNQOLE9Q"
                syncBudgetsUseCase.execute(token)
                syncCategoryUseCase.execute(token)
                syncTransactionUseCase.execute()
                _syncStatus.value = "Synced successfully"
            } catch (e: Exception) {
                _syncStatus.value = "Sync failed"
            }

        }
    }
}