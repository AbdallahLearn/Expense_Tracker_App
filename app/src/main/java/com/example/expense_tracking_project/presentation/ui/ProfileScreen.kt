package com.example.expense_tracking_project.presentation.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.expense_tracking_project.navigation.Screen
import com.example.expense_tracking_project.presentation.ui.dialogs.ConfirmationDialog
import com.example.expense_tracking_project.presentation.vm.SignInViewModel
import com.example.expense_tracking_project.presentation.vm.SignOutViewModel

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
                var showDialog by remember { mutableStateOf(false) }

                if (showDialog) {
                    ConfirmationDialog(
                        title = "Sign Out",
                        message = "Are you sure you want to sign out?",
                        confirmButtonText = "Sign Out",
                        confirmButtonColor = Color.Red,
                        onConfirm = {
                            signOutViewModel.signout()
                            navController.navigate(Screen.Login.route) {
                                popUpTo(0) // optional: clears backstack
                            }
                            showDialog = false
                        },
                        onDismiss = {
                            showDialog = false
                        }
                    )
                }

                Button(
                    onClick = {
                        showDialog = true // Show confirmation dialog
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
