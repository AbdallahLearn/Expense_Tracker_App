package com.example.expense_tracking_project

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.expense_tracking_project.navigation.AppNavigation
import androidx.navigation.compose.rememberNavController
import com.example.expense_tracking_project.navigation.Screen
import com.example.expense_tracking_project.presentation.vm.AuthState
import com.example.expense_tracking_project.presentation.vm.SignInViewModel

import com.example.expense_tracking_project.ui.theme.Expense_Tracking_ProjectTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val navController = rememberNavController()
            val signInViewModel: SignInViewModel = viewModel()
            val authState by signInViewModel.authState.observeAsState(AuthState.Loading)

            val context = LocalContext.current

            Expense_Tracking_ProjectTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        // Handle authentication state and navigation
                        LaunchedEffect(authState) {
                            when (authState) {
                                is AuthState.Authenticated -> {
                                    // Check if the password reset flow was completed
                                    if (signInViewModel.isPasswordResetCompleted()) {
                                        // Force the user to login again after password reset
                                        signInViewModel.authenticate(false)
                                        navController.navigate(Screen.Login.route) {
                                            popUpTo(Screen.Onboarding.route) { inclusive = true }
                                        }
                                    } else {
                                        // Normal flow to navigate to Home
                                        navController.navigate(Screen.Home.route) {
                                            popUpTo(Screen.Onboarding.route) { inclusive = true }
                                        }
                                    }
                                }

                                is AuthState.Unauthenticated -> {
                                    // If user is unauthenticated, navigate to Onboarding
                                    navController.navigate(Screen.Onboarding.route) {
                                        popUpTo(Screen.Home.route) { inclusive = true }
                                    }
                                }

                                is AuthState.Error -> {
                                    // Handle authentication error
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
                                // Navigate to Login and clear Onboarding from backstack
                                navController.navigate(Screen.Login.route) {
                                    popUpTo(Screen.Onboarding.route) { inclusive = true }
                                }
                            }
                        )
                    }
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

