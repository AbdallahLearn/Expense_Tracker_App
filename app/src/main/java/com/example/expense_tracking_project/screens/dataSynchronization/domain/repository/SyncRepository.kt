package com.example.expense_tracking_project.screens.dataSynchronization.domain.repository

import androidx.browser.trusted.Token
import com.example.expense_tracking_project.core.local.entities.BudgetEntity

interface SyncRepository {
    suspend fun syncBudgets() : Unit
    suspend fun getBudgetsFromApi(token: String) : List <BudgetEntity>
}
