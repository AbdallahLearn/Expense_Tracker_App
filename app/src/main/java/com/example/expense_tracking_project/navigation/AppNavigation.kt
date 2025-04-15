package com.example.expense_tracking_project.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.expense_tracking_project.screens.authentication.presentation.screens.CheckEmailScreen
import com.example.expense_tracking_project.screens.authentication.presentation.screens.LoginScreen
import com.example.expense_tracking_project.screens.authentication.presentation.screens.ResetPasswordScreen
import com.example.expense_tracking_project.screens.authentication.presentation.screens.SignUpScreen
import com.example.expense_tracking_project.screens.expenseTracking.presentation.component.CustomBottomBar
import com.example.expense_tracking_project.screens.expenseTracking.presentation.screens.AddExpenseScreen
import com.example.expense_tracking_project.screens.expenseTracking.presentation.screens.EditScreen
import com.example.expense_tracking_project.screens.expenseTracking.presentation.screens.HomeScreen
import com.example.expense_tracking_project.screens.expenseTracking.presentation.screens.ProfileScreen
import com.example.expense_tracking_project.screens.expenseTracking.presentation.screens.StatisticsScreen
import com.example.expense_tracking_project.screens.expenseTracking.presentation.screens.TransactionScreen
import com.example.expense_tracking_project.screens.onBoardingScreen.presentation.screens.OnBoardingScreen

@Composable
fun AppNavigation(
    navController: NavHostController,
    changeAppTheme: () -> Unit,
    isDarkTheme: Boolean,
) {
    val currentBackStackEntry = navController.currentBackStackEntryAsState().value
    val currentRoute = currentBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            if (currentRoute in bottomBarScreens.map { it.route }) {
                CustomBottomBar(
                    selectedIndex = bottomBarScreens.indexOfFirst { it.route == currentRoute },
                    onItemSelected = { selectedIndex ->
                        val selectedRoute = bottomBarScreens[selectedIndex].route
                        navController.navigate(selectedRoute) {
                            popUpTo(Screen.Home.route) { inclusive = false }
                            launchSingleTop = true
                        }
                    },
                    navController = navController
                )
            }
        }

    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Onboarding.route,
            modifier = Modifier.padding(padding)
        ) {
            composable(Screen.Onboarding.route) {
                OnBoardingScreen(navController)
            }
            composable(Screen.Login.route) {
                LoginScreen(navController)
            }
            composable(Screen.SignUp.route) {
                SignUpScreen(navController)
            }
            composable(Screen.Home.route) {
                HomeScreen(
                    navController = navController,
                    changeAppTheme = changeAppTheme
                )
            }
            composable(Screen.AddTransaction.route) {
                TransactionScreen(navController, isDarkTheme)
            }
            composable(Screen.AddExpense.route) {
                AddExpenseScreen(navController)
            }
            composable(Screen.Edit.route) {
                EditScreen(navController)
            }
            composable(Screen.Profile.route) {
                ProfileScreen(navController)
            }
            composable(Screen.Statistics.route) {
                StatisticsScreen(navController)
            }
            composable(Screen.CheckEmail.route) {
                CheckEmailScreen(navController)
            }
            composable(Screen.ResetPassword.route) {
                ResetPasswordScreen(navController)
            }
        }
    }
}

val bottomBarScreens = listOf(
    Screen.Home,
    Screen.Statistics,
    Screen.Edit,
    Screen.Profile
)

