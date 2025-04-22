package com.example.expense_tracking_project.screens.dataSynchronization.domain.repository

import com.example.expense_tracking_project.core.local.entities.Category

interface SyncCategoryRepository {
    suspend fun syncCategory(): Unit
    suspend fun getCategoryFromApi() : List <Category>

}