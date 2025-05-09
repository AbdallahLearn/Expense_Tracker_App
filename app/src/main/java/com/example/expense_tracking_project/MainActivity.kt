package com.example.expense_tracking_project

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.rememberNavController
import com.example.expense_tracking_project.navigation.AppNavigation
import com.example.expense_tracking_project.screens.expenseTracking.presentation.vmModels.ThemeViewModel
import com.example.expense_tracking_project.ui.theme.Expense_Tracking_ProjectTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val themeViewModel: ThemeViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val navController = rememberNavController()
            val isDarkTheme by themeViewModel.isDarkTheme.collectAsState()

            Expense_Tracking_ProjectTheme(darkTheme = isDarkTheme) {
                AppNavigation(
                    navController = navController,
                    changeAppTheme = { themeViewModel.toggleTheme() },
                    isDarkTheme = isDarkTheme
                )
            }
        }
    }
}

