package com.example.expense_tracking_project.screens.expenseTracking.presentation.component

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.expense_tracking_project.R
import com.example.expense_tracking_project.navigation.Screen

@Composable
fun CustomBottomBar(
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit,
    navController: NavController,
//    onFabClick: () -> Unit
) {
    val items = listOf(
        Icons.Default.Home to stringResource(R.string.home),
        Icons.Default.BarChart to stringResource(R.string.statistics),
        Icons.Default.Settings to stringResource(R.string.edit),
        Icons.Default.Person to stringResource(R.string.profile)
    )
    Box {
        BottomAppBar(
            containerColor = MaterialTheme.colorScheme.background,
            tonalElevation = 4.dp,
            modifier = Modifier.height(130.dp) // height of the bottom bar
        ) {
            items.forEachIndexed { index, item ->
                if (index == 2) {
                    Spacer(Modifier.weight(1f)) // Space before FAB
                }

                // Create a Column to hold both the Icon and Text
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp), // Padding around icons and text
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    IconButton(
                        onClick = { onItemSelected(index) }
                    ) {
                        Icon(
                            imageVector = item.first,
                            contentDescription = null,
                            tint = if (selectedIndex == index) MaterialTheme.colorScheme.primary else Color.Gray
                        )
                    }
                    // Add the text under the icon
                    Text(
                        text = item.second,
                        style = MaterialTheme.typography.bodySmall,
                        color = if (selectedIndex == index) MaterialTheme.colorScheme.primary else Color.Gray
                    )
                }
            }
        }

        // Floating Action Button in the center
        FloatingActionButton(
//            onClick = onFabClick,
            onClick = {
                navController.navigate(Screen.AddExpense.route) // Navigate to AddExpense screen when FAB is clicked
            },
            containerColor = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .align(Alignment.Center) // Center the FAB in the BottomAppBar
                .offset(y = -28.dp) // Adjust to center vertically
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add",
                tint = Color.White
            )
        }
    }
}














