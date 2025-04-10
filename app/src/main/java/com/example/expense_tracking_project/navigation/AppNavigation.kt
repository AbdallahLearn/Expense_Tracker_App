package com.example.expense_tracking_project.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.expense_tracking_project.presentation.ui.HomeScreen
import com.example.expense_tracking_project.presentation.ui.LoginScreen
import com.example.expense_tracking_project.presentation.ui.SignUpScreen

@Composable
fun AppNavigation (){
    // Create NavController
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "login") {

        composable("login"){
            LoginScreen(navController)
        }
        composable("signup"){
            SignUpScreen(navController)
        }
        composable("homescreen"){
            HomeScreen(navController)
        }
    }


}