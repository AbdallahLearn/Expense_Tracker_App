package com.example.expense_tracking_project.core.local.data

object PredefinedCategoryProvider {

    val incomeCategories = listOf(
        "Salary",
        "Bonus",
        "Freelance",
        "Investment"
    )

    val expenseCategories = listOf(
        "Food",
        "Shopping",
        "Entertainment",
        "Bills"
    )

    fun getCategoriesForType(type: String): List<String> {
        return when (type) {
            "Income" -> incomeCategories
            else -> expenseCategories
        }
    }
}