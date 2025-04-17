package com.example.expense_tracking_project.screens.expenseTracking.presentation.vmModels

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class EditScreenViewModel @Inject constructor (): ViewModel() {

    private val _selectedTab = MutableStateFlow("Category")
    val selectedTab: StateFlow<String> = _selectedTab

    private val _searchText = MutableStateFlow("")
    val searchText: StateFlow<String> = _searchText

    fun updateTab(tab: String) {
        _selectedTab.value = tab
    }

    fun updateSearch(text: String) {
        _searchText.value = text
    }
}
