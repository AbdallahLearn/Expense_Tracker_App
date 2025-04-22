package com.example.expense_tracking_project.screens.expenseTracking.data.data_source.remote

import com.example.expense_tracking_project.core.local.entities.Category
import com.example.expense_tracking_project.screens.expenseTracking.data.data_source.RemoteCategoryDataSource
import com.example.expense_tracking_project.screens.expenseTracking.data.remote.sync.CategoryApi
import javax.inject.Inject

class RemoteCategoryDataSourceImpl @Inject constructor(
    private val categoryApi: CategoryApi,
): RemoteCategoryDataSource {
    override suspend fun insertCategory(category: Category) {
        categoryApi.getCategory()
    }
}