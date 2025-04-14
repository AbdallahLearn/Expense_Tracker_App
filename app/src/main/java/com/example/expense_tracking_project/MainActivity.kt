package com.example.expense_tracking_project

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.expense_tracking_project.navigation.AppNavigation
import androidx.navigation.compose.rememberNavController
import com.example.expense_tracking_project.data.model.AuthState
import com.example.expense_tracking_project.navigation.Screen
import com.example.expense_tracking_project.presentation.ui.AuthenticationHandler
import com.example.expense_tracking_project.presentation.vm.SignInViewModel
import com.example.expense_tracking_project.presentation.vm.ThemeViewModel
import com.example.expense_tracking_project.ui.theme.Expense_Tracking_ProjectTheme

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val navController = rememberNavController()
            val signInViewModel: SignInViewModel = viewModel()
            val authState by signInViewModel.authState.observeAsState()

            val themeViewModel: ThemeViewModel = viewModel()
            val isDarkTheme by themeViewModel.isDarkTheme.collectAsState()

            Expense_Tracking_ProjectTheme(darkTheme = isDarkTheme) {
                // Removed inner Scaffold to avoid double padding
                Box(modifier = Modifier.fillMaxSize()) {
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