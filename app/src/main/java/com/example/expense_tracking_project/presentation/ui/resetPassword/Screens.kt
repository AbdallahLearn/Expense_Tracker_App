package com.example.expense_tracking_project.presentation.ui.resetPassword

sealed class Screen(val route: String) {
    object ResetPassword : Screen("ResetPasswordScreen")
    object CheckEmail : Screen("CheckEmailScreen")
    object log : Screen("Login")

}
