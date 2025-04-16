package com.example.expense_tracking_project.navigation

import kotlinx.serialization.Serializable

//Type-Safe Navigation: Passing and Receiving Arguments with CustomNavType
@Serializable
sealed class Screen {
    @Serializable data object Onboarding : Screen()
    @Serializable data object Login : Screen()
    @Serializable data object SignUp : Screen()
    @Serializable data object Home : Screen()
//    @Serializable data object AddTransaction : Screen()
    @Serializable data object AddExpense : Screen()
    @Serializable data object Edit : Screen()
    @Serializable data object Profile : Screen()
    @Serializable data object Statistics : Screen()
    @Serializable data object CheckEmail : Screen()
    @Serializable data object ResetPassword : Screen()
    @Serializable data object AddBudget : Screen() // add budget screen
    @Serializable data object AddCategory : Screen() // add category screen


}
