package com.example.expense_tracking_project.screens.expenseTracking.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.expense_tracking_project.navigation.Screen
import com.example.expense_tracking_project.screens.authentication.presentation.vmModels.SignOutViewModel
import com.example.expense_tracking_project.screens.expenseTracking.presentation.component.CustomBottomBar

@Composable
fun ProfileScreen(
    navController: NavController,
//    onSignOut: () -> Unit
    signOutViewModel: SignOutViewModel = viewModel()
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = {
                              signOutViewModel.signout()
                              navController.navigate(Screen.Login.route)
                              },
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(vertical = 16.dp)
                ) {
                    Text("Sign Out")
                }
            }
        }
    }
}
