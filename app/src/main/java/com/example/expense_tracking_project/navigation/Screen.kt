package com.example.expense_tracking_project.navigation


//Type-Safe Navigation: Passing and Receiving Arguments with CustomNavType
sealed class Screen(val route:String) {
    //@Serializable
    object Onboarding:Screen("onboarding")
    //@Serializable

    object Login: Screen("login")
    //@Serializable
    object Home:Screen("home")

    // Example with passing arguments using Kotlin serialization
    //@Serializable
    //data class UserProfile(val userId: Int) : Screen("userProfile/{userId}")
}