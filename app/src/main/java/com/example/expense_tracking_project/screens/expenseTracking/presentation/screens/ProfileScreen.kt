package com.example.expense_tracking_project.screens.expenseTracking.presentation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.expense_tracking_project.navigation.Screen
import com.example.expense_tracking_project.screens.authentication.presentation.vmModels.SignOutViewModel
import com.example.expense_tracking_project.screens.expenseTracking.presentation.component.ConfirmationDialog

@Composable
fun ProfileScreen(
    navController: NavController,
    signOutViewModel: SignOutViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    var showSignOutDialog by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier.fillMaxSize(),
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
                    showSignOutDialog = true
                },
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                Text("Sign Out")
            }

            if (showSignOutDialog) {
                ConfirmationDialog(
                    title = "Sign Out",
                    message = "Are you sure you want to sign out?",
                    onConfirm = {
                        signOutViewModel.signout()
                        navController.navigate(Screen.Login) {
                            popUpTo(0) // clears backstack
                        }
                        showSignOutDialog = false
                    },
                    onDismiss = {
                        showSignOutDialog = false
                    }
                )
            }
        }
    }
}