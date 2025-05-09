package com.example.expense_tracking_project.screens.expenseTracking.data.remote

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface BudgetApi { // remote API interface ( how the app communicate with server )

    @POST("/budgets") // convert BudgetDto -> JSON
    suspend fun createBudget(
        @Body budget: BudgetDto
    ): Response<Unit>

    @GET("/budgets")
    suspend fun getBudgets(): Response<BudgetResponse>

}