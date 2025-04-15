package com.example.expense_tracking_project.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.expense_tracking_project.screens.expenseTracking.presentation.screens.HomeScreen
import com.example.expense_tracking_project.screens.authentication.presentation.screens.LoginScreen
import com.example.expense_tracking_project.screens.authentication.presentation.screens.SignUpScreen
import androidx.navigation.NavHostController
import com.example.expense_tracking_project.screens.authentication.presentation.screens.CheckEmailScreen
import com.example.expense_tracking_project.screens.expenseTracking.presentation.screens.TransactionScreen
import com.example.expense_tracking_project.screens.expenseTracking.presentation.screens.AddExpenseScreen
import com.example.expense_tracking_project.screens.expenseTracking.presentation.screens.EditScreen
import com.example.expense_tracking_project.screens.expenseTracking.presentation.screens.ProfileScreen
import com.example.expense_tracking_project.screens.expenseTracking.presentation.screens.StatisticsScreen
import com.example.expense_tracking_project.screens.onBoardingScreen.presentation.screens.onBoardingScreen
import com.example.expense_tracking_project.screens.authentication.presentation.screens.ResetPasswordScreen
import com.example.expense_tracking_project.screens.expenseTracking.presentation.vmModels.ThemeViewModel

//@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigation(
    navController: NavHostController,
    showOnboarding: Boolean,
    onFinish: () -> Unit,
    themeViewModel: ThemeViewModel,
    isDarkTheme: Boolean

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
            HomeScreen(
                navController = navController,
                themeViewModel = themeViewModel,
                isDarkTheme = isDarkTheme
            )
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

        composable(Screen.AddTransaction.route) {
            TransactionScreen(
                navController,
                isDarkTheme = isDarkTheme
            )
        }


        composable(Screen.AddExpense.route) {
            AddExpenseScreen(navController)
        }
        composable(Screen.Profile.route) {
            ProfileScreen(navController )
        }
        composable(Screen.Edit.route) {
            EditScreen(navController)
        }
        composable(Screen.Statistics.route) {
            StatisticsScreen(navController)
        }




    }
}


