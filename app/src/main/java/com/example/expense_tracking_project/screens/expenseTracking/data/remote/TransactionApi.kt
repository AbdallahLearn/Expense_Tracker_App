package com.example.expense_tracking_project.screens.expenseTracking.data.remote

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface TransactionApi { // remote API interface ( how the app communicate with server )

    @POST("/expenses") // convert TransactionDto -> JSON
    suspend fun createTransaction(
        @Body transaction: TransactionDto
    ): Response<Unit>

    @GET("/expenses")
    suspend fun getTransaction(): Response<TransactionResponse>
}