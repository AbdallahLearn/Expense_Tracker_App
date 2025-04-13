package com.example.expense_tracking_project

import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.expense_tracking_project.navigation.AppNavigation
import androidx.navigation.compose.rememberNavController
import com.example.expense_tracking_project.navigation.Screen
import com.example.expense_tracking_project.data.model.AuthState
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
            val authState by signInViewModel.authState.observeAsState(AuthState.Loading)

            val context = LocalContext.current

            val themeViewModel: ThemeViewModel = viewModel()
            val isDarkTheme by themeViewModel.isDarkTheme.collectAsState()

            Expense_Tracking_ProjectTheme(darkTheme = isDarkTheme) {
                // Removed inner Scaffold to avoid double padding
                Box(modifier = Modifier.fillMaxSize()) {

                    // Handle authentication state and navigation
                    LaunchedEffect(authState) {
                        when (authState) {
                            is AuthState.Authenticated -> {
                                if (signInViewModel.isPasswordResetCompleted()) {
                                    signInViewModel.authenticate(false)
                                    navController.navigate(Screen.Login.route) {
                                        popUpTo(Screen.Onboarding.route) { inclusive = true }
                                    }
                                } else {
                                    navController.navigate(Screen.Home.route) {
                                        popUpTo(Screen.Onboarding.route) { inclusive = true }
                                    }
                                }
                            }

                            is AuthState.Unauthenticated -> {
                                navController.navigate(Screen.Onboarding.route) {
                                    popUpTo(Screen.Home.route) { inclusive = true }
                                }
                            }

                            is AuthState.Error -> {
                                val errorMessage = (authState as AuthState.Error).message
                                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                            }

                            AuthState.Loading -> {}
                        }
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

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Expense_Tracking_ProjectTheme {
        Greeting("Android")
    }
}

