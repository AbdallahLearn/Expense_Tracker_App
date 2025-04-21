package com.example.expense_tracking_project.screens.expenseTracking.data.remote

data class TransactionResponse(
    val transactions: List<TransactionDto>
) // { "transactions" : [ .. ] }