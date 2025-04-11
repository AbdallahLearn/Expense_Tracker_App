package com.example.expense_tracking_project.presentation.ui

// <<<<<<< LYM(63-34)-DesignLoginAndSignInputWithValidInput

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController


@Composable
fun HomeScreen(navController : NavController){
    Text("Hello")

}
// =======
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun HomeScreen() {
    Column {
        Text(text = "You are in Home page")
    }
}
// >>>>>>> dev
