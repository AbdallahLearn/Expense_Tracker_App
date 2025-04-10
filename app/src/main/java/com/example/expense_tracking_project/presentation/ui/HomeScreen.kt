package com.example.expense_tracking_project.presentation.ui


import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun HomeScreen(navController : NavController){
    Text("Hello", fontSize = 24.sp, fontWeight = FontWeight.Bold)

}