package com.example.expense_tracking_project.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun ProfileScreen(
    navController: NavController,
    onSignOut: () -> Unit
) {
    Scaffold(
        bottomBar = {
            CustomBottomBar(
                selectedIndex = 3, // Profile tab index
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
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = { onSignOut() },
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                Text("Sign Out")
            }
        }
    }
}
