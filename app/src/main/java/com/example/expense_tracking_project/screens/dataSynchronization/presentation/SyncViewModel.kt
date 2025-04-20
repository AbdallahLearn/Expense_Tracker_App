package com.example.expense_tracking_project.screens.dataSynchronization.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expense_tracking_project.screens.dataSynchronization.domain.usecase.SyncBudgetsUseCase
import com.example.expense_tracking_project.screens.dataSynchronization.domain.usecase.SyncCategoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SyncViewModel @Inject constructor(
    private val syncBudgetsUseCase: SyncBudgetsUseCase,
    private val syncCategoryUseCase: SyncCategoryUseCase
) : ViewModel() {
    private val _syncStatus = MutableStateFlow<String?>(null)
    val syncStatus: StateFlow<String?> = _syncStatus // expose sync status

    fun syncNow() { // to call it from UI
        viewModelScope.launch {
            try {
                syncBudgetsUseCase.execute()
                syncCategoryUseCase.execute()
                _syncStatus.value = "Synced successfully"
            } catch (e: Exception) {
                _syncStatus.value = "Sync failed"
            }

        }
    }
}