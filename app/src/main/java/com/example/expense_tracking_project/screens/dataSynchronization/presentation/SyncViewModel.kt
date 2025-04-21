package com.example.expense_tracking_project.screens.dataSynchronization.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expense_tracking_project.core.connectivity.NetworkConnectivityObserver
import com.example.expense_tracking_project.screens.dataSynchronization.domain.usecase.SyncBudgetsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SyncViewModel @Inject constructor(
    private val syncBudgetsUseCase: SyncBudgetsUseCase,
    private val networkObserver : NetworkConnectivityObserver
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

    fun syncNow() { // to call it from UI
        viewModelScope.launch {
            try {
                syncBudgetsUseCase.execute()
                _syncStatus.value = "Synced successfully"
            } catch (e:Exception){
                _syncStatus.value = "Sync failed"
            }

        }
    }
}
