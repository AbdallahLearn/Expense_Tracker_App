package com.example.expense_tracking_project

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.expense_tracking_project.navigation.AppNavigation
import com.example.expense_tracking_project.presentation.ui.LoginScreen
import com.example.expense_tracking_project.presentation.ui.SignUpScreen
import com.example.expense_tracking_project.ui.theme.Expense_Tracking_ProjectTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Expense_Tracking_ProjectTheme {
                //Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                //LoginScreen()
                //SignUpScreen()
                AppNavigation()
            }
        }
    }
}