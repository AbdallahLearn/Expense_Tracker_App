package com.example.expense_tracking_project.screens.authentication.presentation.vmModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expense_tracking_project.core.local.db.AppDatabase
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.expense_tracking_project.screens.expenseTracking.data.data_source.local.AuthPreferences

@HiltViewModel
class SignOutViewModel @Inject constructor(
    private val appDatabase: AppDatabase,
    private val sharedPreferences: AuthPreferences
) : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _authState = MutableStateFlow<AuthState>(AuthState.Unauthenticated)
    val authState: StateFlow<AuthState> = _authState

    init {
        checkAuthStatus()
    }

    fun checkAuthStatus() {
        _authState.value = if (auth.currentUser == null) {
            AuthState.Unauthenticated
        } else {
            AuthState.Authenticated
        }
    }


    fun signout(onComplete: () -> Unit) {
        viewModelScope.launch {
            appDatabase.clearAllData()
            sharedPreferences.clearToken()
            FirebaseAuth.getInstance().signOut()
            onComplete()
        }
    }
}