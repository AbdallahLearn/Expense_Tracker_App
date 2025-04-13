package com.example.expense_tracking_project.presentation.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController


@Composable
fun EditScreen(navController: NavController) {
    Scaffold(
        bottomBar = {
            CustomBottomBar(
                selectedIndex = 2, // Edit tab index
                onItemSelected = { index ->
                    when (index) {
                        0 -> navController.navigate("home")
                        1 -> navController.navigate("statistics")
                        2 -> navController.navigate("edit")
                        3 -> navController.navigate("profile")
                    }
                },
                navController = navController
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            Text("Edit", style = MaterialTheme.typography.headlineMedium)
        }
    }
}
