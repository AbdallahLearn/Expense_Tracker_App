package com.example.expense_tracking_project.presentation.ui

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.expense_tracking_project.presentation.vm.SignOutViewModel
import com.example.expense_tracking_project.navigation.Screen

@Composable
fun HomeScreen(
    navController: NavController,
    signOutViewModel: SignOutViewModel = viewModel()
) {
    val context = LocalContext.current
    var selectedIndex by remember { mutableStateOf(0) } // Home Icon

    // Handle item selection in BottomAppBar
    val onItemSelected: (Int) -> Unit = { index ->
        selectedIndex = index
    }

    // Handle FAB click (you can customize this action)
    val onFabClick: () -> Unit = {
        // Handle FAB click logic here (e.g., navigate to a new screen, show a dialog, etc.)
        Toast.makeText(context, "FAB clicked!", Toast.LENGTH_SHORT).show()
    }

    // Sign-out action
    fun signOut() {
        signOutViewModel.signout()
        Toast.makeText(context, "Logged out successfully", Toast.LENGTH_SHORT).show()
        navController.navigate(Screen.Login.route) {
            popUpTo(navController.graph.startDestinationId) {
                inclusive = true
            }
        }
    }
    // Add Bottom Bar
    Scaffold(
        bottomBar = {
            CustomBottomBar(
                selectedIndex = selectedIndex,
                onItemSelected = onItemSelected,
                onFabClick = onFabClick
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues) // Ensure content doesn't overlap with BottomBar
                .padding(16.dp), // Additional padding for inner content
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Hello, Welcome to the Home Page!")

            Spacer(modifier = Modifier.height(16.dp))

            // Sign Out Button
            Button(
                onClick = { signOut() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Text(text = "Sign Out")
            }
        }
    }
}
