package com.example.expense_tracking_project.screens.expenseTracking.presentation.screens

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.expense_tracking_project.navigation.Screen
import com.example.expense_tracking_project.screens.authentication.data.model.AuthState
import com.example.expense_tracking_project.screens.authentication.presentation.vmModels.SignInViewModel

@Composable
fun AuthenticationHandler(
    authState: AuthState,
    navController: NavController,
    signInViewModel: SignInViewModel
) {
    val context = LocalContext.current

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
                val errorMessage = authState.message
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
            }

            AuthState.Loading -> {}
        }
    }
}