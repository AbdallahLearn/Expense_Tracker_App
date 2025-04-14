package com.example.expense_tracking_project

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.expense_tracking_project.navigation.AppNavigation
import androidx.navigation.compose.rememberNavController
import com.example.expense_tracking_project.navigation.Screen
import com.example.expense_tracking_project.screens.authentication.data.model.AuthState
import com.example.expense_tracking_project.screens.authentication.presentation.vmModels.SignInViewModel
import com.example.expense_tracking_project.screens.expenseTracking.presentation.vmModels.ThemeViewModel

import com.example.expense_tracking_project.data.model.AuthState
import com.example.expense_tracking_project.navigation.Screen
import com.example.expense_tracking_project.presentation.ui.AuthenticationHandler
import com.example.expense_tracking_project.presentation.ui.CustomBottomBar
import com.example.expense_tracking_project.presentation.vm.SignInViewModel
import com.example.expense_tracking_project.presentation.vm.ThemeViewModel
import com.example.expense_tracking_project.navigation.Screen
import com.example.expense_tracking_project.screens.authentication.data.model.AuthState
import com.example.expense_tracking_project.screens.authentication.presentation.vmModels.SignInViewModel
import com.example.expense_tracking_project.screens.expenseTracking.presentation.vmModels.ThemeViewModel

import com.example.expense_tracking_project.ui.theme.Expense_Tracking_ProjectTheme

class MainActivity : ComponentActivity() {
    //    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val navController = rememberNavController()
            val signInViewModel: SignInViewModel = viewModel()
            val authState by signInViewModel.authState.observeAsState()

            val themeViewModel: ThemeViewModel = viewModel()
            val isDarkTheme by themeViewModel.isDarkTheme.collectAsState()

            val currentBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = currentBackStackEntry?.destination?.route

            val selectedIndex = when (currentRoute) {
                "home" -> 0
                "statistics" -> 1
                "edit" -> 2
                "profile" -> 3
                else -> -1
            }

            Expense_Tracking_ProjectTheme(darkTheme = isDarkTheme) {
                androidx.compose.material3.Scaffold(
                    bottomBar = {
                        if (selectedIndex != -1) {
                            CustomBottomBar(
                                selectedIndex = selectedIndex,
                                onItemSelected = { index ->
                                    val route = when (index) {
                                        0 -> "home"
                                        1 -> "statistics"
                                        2 -> "edit"
                                        3 -> "profile"
                                        else -> null
                                    }
                                    route?.let {
                                        if (currentRoute != it) {
                                            navController.navigate(it)
                                        }
                                    }
                                },
                                navController = navController
                            )
                        }
                    }
                ) { padding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding)
                    ) {
                        if (authState != null) {
                            AuthenticationHandler(
                                authState = authState!!,
                                navController = navController,
                                signInViewModel = signInViewModel
                            )
                        }
                        AppNavigation(
                            navController = navController,
                            showOnboarding = authState == AuthState.Unauthenticated,
                            onFinish = {
                                navController.navigate(Screen.Login.route) {
                                    popUpTo(Screen.Onboarding.route) { inclusive = true }
                                }
                            },
                            themeViewModel = themeViewModel,
                            isDarkTheme = isDarkTheme
                        )

                    }
                }
            }
        }
    }
}