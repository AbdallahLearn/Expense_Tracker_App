package com.example.expense_tracking_project.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.expense_tracking_project.presentation.ui.resetpassword.CheckEmailScreen
import com.example.expense_tracking_project.presentation.ui.resetpassword.ResetPasswordScreen
import com.example.expense_tracking_project.presentation.ui.resetpassword.Screen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = Screen.ResetPassword.route) {
        composable(Screen.ResetPassword.route) {
            ResetPasswordScreen(navController)
        }

        composable(Screen.CheckEmail.route) {
            CheckEmailScreen(navController)
        }


    }
}