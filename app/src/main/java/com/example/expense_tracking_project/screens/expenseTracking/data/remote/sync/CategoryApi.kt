package com.example.expense_tracking_project.screens.expenseTracking.data.remote.sync

import com.example.expense_tracking_project.screens.expenseTracking.data.remote.CategoryResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface CategoryApi {

    @POST("/categories")
    suspend fun createCategory(
        @Body category: CategoryDto
    ): Response<Unit>

    @GET("/categories")
    suspend fun getCategory(
    ): Response<CategoryResponse>
}