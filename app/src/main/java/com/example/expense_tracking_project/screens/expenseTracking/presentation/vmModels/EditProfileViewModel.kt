package com.example.expense_tracking_project.screens.expenseTracking.presentation.vmModels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class EditProfileViewModel : ViewModel() {

    // State to control dialog visibility
    val showSignOutDialog: MutableState<Boolean> = mutableStateOf(false) // the dialog not visible

    fun openSignOutDialog() { // show sign dialog
        showSignOutDialog.value = true
    }

    fun closeSignOutDialog() { // if click cancel
        showSignOutDialog.value = false
    }
}
