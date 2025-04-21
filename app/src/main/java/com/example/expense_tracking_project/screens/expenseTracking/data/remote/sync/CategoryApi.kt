package com.example.expense_tracking_project.screens.expenseTracking.data.remote.sync

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface CategoryApi {

    @POST("/categories")
    suspend fun createCategory(
        @Header("Authorization") token: String,
        @Body category: CategoryDto
    ): Response<Unit>

//    @GET("/categories")
//    suspend fun getCategory(
//        @Header("Authorization") token: String
//    ): Response<List<CategoryDto>>

}