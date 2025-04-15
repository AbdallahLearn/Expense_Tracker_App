package com.example.expense_tracking_project

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.expense_tracking_project.navigation.AppNavigation
import com.example.expense_tracking_project.navigation.Screen
import com.example.expense_tracking_project.screens.authentication.data.repository.AuthRepositoryImpl
import com.example.expense_tracking_project.screens.authentication.domain.usecase.LoginUseCase
import com.example.expense_tracking_project.screens.authentication.presentation.vmModels.AuthState
import com.example.expense_tracking_project.screens.authentication.presentation.vmModels.SignInViewModel
import com.example.expense_tracking_project.screens.expenseTracking.presentation.component.CustomBottomBar
import com.example.expense_tracking_project.screens.expenseTracking.presentation.vmModels.ThemeViewModel
import com.example.expense_tracking_project.ui.theme.Expense_Tracking_ProjectTheme
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        FirebaseApp.initializeApp(this)

        setContent {
            val navController = rememberNavController()
            val loginUseCase = remember {
                LoginUseCase(AuthRepositoryImpl(FirebaseAuth.getInstance()))
            }

            val signInViewModel: SignInViewModel = hiltViewModel()


            // Use collectAsState() for StateFlow
            val authState by signInViewModel.authState.collectAsState()

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
                        // Here we compare using `is` to check the type
                        val showOnboarding = authState is AuthState.Unauthenticated
                        AppNavigation(
                            navController = navController,
                            showOnboarding = showOnboarding,
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













