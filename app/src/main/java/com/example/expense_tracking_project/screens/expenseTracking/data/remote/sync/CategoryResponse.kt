package com.example.expense_tracking_project.screens.expenseTracking.data.remote

import com.example.expense_tracking_project.screens.expenseTracking.data.remote.sync.CategoryDto

class CategoryResponse(
    val categories: List<CategoryDto>
) // { "categories" : [ .. ] }