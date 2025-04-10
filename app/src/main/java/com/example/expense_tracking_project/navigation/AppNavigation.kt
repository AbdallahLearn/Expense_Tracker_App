package com.example.expense_tracking_project.navigation

import androidx.compose.runtime.Composable

import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.expense_tracking_project.presentation.ui.resetPassword.CheckEmailScreen
import com.example.expense_tracking_project.presentation.ui.resetPassword.Login
import com.example.expense_tracking_project.presentation.ui.resetPassword.ResetPasswordScreen
import com.example.expense_tracking_project.presentation.ui.resetPassword.Screen


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
        composable(Screen.log.route) {
            Login(navController)
        }

    }
}
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.expense_tracking_project.presentation.ui.HomeScreen
import com.example.expense_tracking_project.presentation.ui.LoginScreen
import com.example.expense_tracking_project.presentation.ui.OnboardingPageV2
import com.example.expense_tracking_project.presentation.ui.onBoardingScreen

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
            HomeScreen()
        }
    }
}
