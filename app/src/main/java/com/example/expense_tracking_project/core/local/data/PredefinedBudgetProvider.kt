package com.example.expense_tracking_project.core.local.data

object PredefinedBudgetProvider {

//    private val budgetIntervals = listOf(
//        "1 Month",
//        "2 Months",
//        "3 Months",
//        "6 Months",
//        "1 Year"
//    )

    private val budgetOptions = listOf(
        "1000.00",
        "2000.00",
        "5000.00",
        "10000.00",
        "20000.00",
        "50000.00",
    )

    fun getAllBudgets(): List<String> {
        return budgetOptions
    }
}
