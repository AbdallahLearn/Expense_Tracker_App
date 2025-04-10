package com.example.expense_tracking_project.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.expense_tracking_project.presentation.ui.HomeScreen
import com.example.expense_tracking_project.presentation.ui.LoginScreen
import com.example.expense_tracking_project.presentation.ui.SignUpScreen
import androidx.navigation.NavHostController
import com.example.expense_tracking_project.presentation.ui.onBoardingScreen
import com.example.expense_tracking_project.presentation.ui.resetPassword.CheckEmailScreen
import com.example.expense_tracking_project.presentation.ui.resetPassword.ResetPasswordScreen

@Composable
fun AppNavigation(
    navController: NavHostController,
    showOnboarding: Boolean,
    onFinish: () -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = if (showOnboarding) Screen.Onboarding.route else Screen.Login.route
    ) {
        composable(Screen.Onboarding.route) {
            onBoardingScreen(
                navController = navController,
                onFinish = onFinish
            )
        }

        composable(Screen.Login.route) {
            LoginScreen(navController = navController)
        }

        composable(Screen.Home.route) {
            HomeScreen(navController)
        }

        composable(Screen.ResetPassword.route) {
            ResetPasswordScreen(navController)
        }

        composable(Screen.CheckEmail.route) {
            CheckEmailScreen(navController)
        }

        composable(Screen.SignUp.route) {
            SignUpScreen(navController)
        }

    }
}


