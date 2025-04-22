package com.example.expense_tracking_project.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.example.expense_tracking_project.screens.authentication.presentation.screens.CheckEmailScreen
//import com.example.expense_tracking_project.screens.authentication.presentation.screens.CheckEmailScreen
import com.example.expense_tracking_project.screens.authentication.presentation.screens.LoginScreen
import com.example.expense_tracking_project.screens.authentication.presentation.screens.ResetPasswordScreen
//import com.example.expense_tracking_project.screens.authentication.presentation.screens.ResetPasswordScreen
import com.example.expense_tracking_project.screens.authentication.presentation.screens.SignUpScreen
import com.example.expense_tracking_project.screens.dataVisualization.presentation.screens.StatisticsScreen
import com.example.expense_tracking_project.screens.expenseTracking.presentation.component.CustomBottomBar
import com.example.expense_tracking_project.screens.expenseTracking.presentation.screens.AddBudgetScreen
import com.example.expense_tracking_project.screens.expenseTracking.presentation.screens.AddCategoryScreen
import com.example.expense_tracking_project.screens.expenseTracking.presentation.screens.AddExpenseScreen
import com.example.expense_tracking_project.screens.expenseTracking.presentation.screens.EditScreen
import com.example.expense_tracking_project.screens.expenseTracking.presentation.screens.HomeScreen
import com.example.expense_tracking_project.screens.expenseTracking.presentation.screens.ProfileScreen
import com.example.expense_tracking_project.screens.onBoardingScreen.presentation.screens.OnBoardingScreen
import kotlinx.serialization.json.Json

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigation(
    navController: NavHostController,
    changeAppTheme: () -> Unit,
    isDarkTheme: Boolean,
) {
    val currentBackStackEntry = navController.currentBackStackEntryAsState().value
    val currentRoute = currentBackStackEntry?.destination?.route

    val showBottomBar = bottomBarScreens.any { screen ->
        screen::class.qualifiedName == currentRoute
    }

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                CustomBottomBar(
                    selectedIndex = bottomBarScreens.indexOfFirst { screen ->
                        screen::class.qualifiedName == currentRoute
                    },
                    onItemSelected = { selectedIndex ->
                        val selectedScreen = bottomBarScreens[selectedIndex]
                        navController.navigate(selectedScreen) {
                            popUpTo(Screen.Home) { inclusive = false }
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
            startDestination = Screen.Onboarding,
            modifier = Modifier.padding(padding)
        ) {
            composable<Screen.Onboarding> {
                OnBoardingScreen(navController)
            }
            composable<Screen.Login> {
                LoginScreen(navController)
            }
            composable<Screen.SignUp> {
                SignUpScreen(navController)
            }
            composable<Screen.Home> {
                HomeScreen(
                    navController = navController,
                    changeAppTheme = changeAppTheme,
                    isDarkTheme = isDarkTheme
                )
            }
            composable<Screen.AddExpense> {
                AddExpenseScreen(navController)
            }
            composable<Screen.Edit> {
                EditScreen(navController)
            }
            composable<Screen.Profile> {
                ProfileScreen(navController)
            }
            composable<Screen.Statistics> {
                StatisticsScreen(navController)
            }
            composable<Screen.CheckEmail> {
                CheckEmailScreen(navController)
            }
            composable<Screen.ResetPassword> {
                ResetPasswordScreen(navController)
            }
            composable<Screen.AddBudget> {backStackEntry ->
                val budgetId = backStackEntry.arguments?.getInt("budgetId")
                AddBudgetScreen(navController, budgetId = budgetId)
            }
            composable<Screen.AddCategory> { backStackEntry ->
                val categoryId = backStackEntry.arguments?.getInt("categoryId")
                AddCategoryScreen(navController, categoryId = categoryId)
            }


            composable(
                "add_expense?transactionId={transactionId}",
                arguments = listOf(navArgument("transactionId") {
                    type = NavType.IntType
                    defaultValue = -1
                })
            ) { backStackEntry ->
                val transactionId = backStackEntry.arguments?.getInt("transactionId")
                AddExpenseScreen(
                    navController = navController,
                    transactionId = if (transactionId == -1) null else transactionId
                )
            }



            composable(
                route = "add_budget?budgetData={budgetData}",
                arguments = listOf(navArgument("budgetData") { type = NavType.StringType })
            ) { backStackEntry ->
                val budgetData = backStackEntry.arguments?.getString("budgetData")
                val budget = budgetData?.let {
                    Json.decodeFromString<Screen.AddBudget>(it)
                }
                AddBudgetScreen(navController, budgetId = budget?.budgetId)
            }
            composable(
                route = "add_category?categoryData={categoryData}",
                arguments = listOf(navArgument("categoryData") { type = NavType.StringType })
            ) { backStackEntry ->
                val categoryData = backStackEntry.arguments?.getString("categoryData")
                val category = categoryData?.let {
                    Json.decodeFromString<Screen.AddCategory>(it)
                }
                AddCategoryScreen(navController, categoryId = category?.categoryId)
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