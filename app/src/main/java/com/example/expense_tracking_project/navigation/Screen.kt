package com.example.expense_tracking_project.navigation


//Type-Safe Navigation: Passing and Receiving Arguments with CustomNavType
sealed class Screen(val route: String) {
    //@Serializable
    object Onboarding : Screen("onboarding")
    //@Serializable

    object Login :
        Screen("loginMouck") //change the name to go simple login page to test the navigations between screen

    //@Serializable
    object Home : Screen("home") // home screen

    // Example with passing arguments using Kotlin serialization
    //@Serializable
    //data class UserProfile(val userId: Int) : Screen("userProfile/{userId}")
    object SignUp : Screen("signup")
    object ResetPassword : Screen("ResetPasswordScreen")
    object CheckEmail : Screen("CheckEmailScreen")
    object log : Screen("Login")
    object Profile : Screen("profile") // profile screen
    object Statistics : Screen("statistics") // statistic screen
    object Edit : Screen("edit") // edit screen

    object AddTransaction : Screen("TransactionScreen")
    object AddExpense : Screen("addexpense")


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