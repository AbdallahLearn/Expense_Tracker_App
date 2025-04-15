package com.example.expense_tracking_project.navigation

import kotlinx.serialization.Serializable

//Type-Safe Navigation: Passing and Receiving Arguments with CustomNavType
@Serializable
sealed class Screen(val route: String) {
    @Serializable data object Onboarding : Screen("onboarding")
    @Serializable data object Login : Screen("login")
    @Serializable data object SignUp : Screen("signup")
    @Serializable data object Home : Screen("home")
    @Serializable data object AddTransaction : Screen("add_transaction")
    @Serializable data object AddExpense : Screen("add_expense")
    @Serializable data object Edit : Screen("edit")
    @Serializable data object Profile : Screen("profile")
    @Serializable data object Statistics : Screen("statistics")
    @Serializable data object CheckEmail : Screen("check_email")
    @Serializable data object ResetPassword : Screen("reset_password")
}

//@Serializable
/*sealed class Screen(val route:String) {
    object Onboarding : Screen("onboarding")
    object Login : Screen("login")
    object SignUp : Screen("signup")
    object Home : Screen("home")
    object ResetPassword : Screen("resetPassword")
    object CheckEmail : Screen("checkEmail")
}*/