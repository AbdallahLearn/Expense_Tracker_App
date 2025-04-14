package com.example.expense_tracking_project.screens.expenseTracking.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.expense_tracking_project.screens.expenseTracking.presentation.component.CustomBottomBar

@Composable
fun StatisticsScreen(navController: NavController, modifier: Modifier = Modifier) {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Statistics", style = MaterialTheme.typography.headlineMedium)
    }
}