package com.example.expense_tracking_project.navigation


//Type-Safe Navigation: Passing and Receiving Arguments with CustomNavType
//@Serializable
sealed class Screen(val route:String) {
    object Onboarding : Screen("onboarding")
    object Login : Screen("login")
    object SignUp : Screen("signup")
    object Home : Screen("home")
    object ResetPassword : Screen("resetPassword")
    object CheckEmail : Screen("checkEmail")
}
