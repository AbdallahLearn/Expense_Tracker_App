package com.example.expense_tracking_project.presentation.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.expense_tracking_project.navigation.Screen

@Composable
fun LoginScreen(navController: NavController) {
    Column {
        Text(text = "You are on the Login page")

        Button(onClick = {
            navController.navigate(Screen.Home.route) {
                // Remove the Login screen (and any screens above it) from the back stack
                // So that pressing the back button won't take the user back to the login screen
                popUpTo(Screen.Login.route) {
                    // This makes sure the Login screen itself is also removed (not just the screens above it)
                    inclusive = true
                }
            }
        }) {
            Text(text = "Login and go to Home")
        }
        Button(onClick = {
            navController.navigate(Screen.signup.route) {
                // Remove the Login screen (and any screens above it) from the back stack
                // So that pressing the back button won't take the user back to the login screen
                popUpTo(Screen.Login.route) {
                    // This makes sure the Login screen itself is also removed (not just the screens above it)
                    inclusive = true
                }
            }
        }) {
            Text(text = "Rest password")
        }
    }
}
