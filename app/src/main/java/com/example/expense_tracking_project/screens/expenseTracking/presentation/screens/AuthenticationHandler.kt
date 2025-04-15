package com.example.expense_tracking_project.screens.expenseTracking.presentation.screens

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.expense_tracking_project.navigation.Screen
import com.example.expense_tracking_project.screens.authentication.presentation.vmModels.AuthState

import com.example.expense_tracking_project.screens.authentication.presentation.vmModels.SignInViewModel

@Composable
fun AuthenticationHandler(
    authState: AuthState,
    navController: NavController,
    signInViewModel: SignInViewModel
) {
    val context = LocalContext.current

    // Collect the password reset completion state
    val isPasswordResetCompleted by signInViewModel.isPasswordResetCompleted.collectAsState()

    // Handle authentication state and navigation
    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Authenticated -> {
                if (isPasswordResetCompleted) {  // Use the collected value here
                    signInViewModel.authenticate(false)
                    navController.navigate(Screen.Login) {
                        popUpTo(Screen.Onboarding) { inclusive = true }
                    }
                } else {
                    navController.navigate(Screen.Home) {
                        popUpTo(Screen.Onboarding) { inclusive = true }
                    }
                }
            }

            is AuthState.Unauthenticated -> {
                navController.navigate(Screen.Onboarding) {
                    popUpTo(Screen.Home) { inclusive = true }
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
