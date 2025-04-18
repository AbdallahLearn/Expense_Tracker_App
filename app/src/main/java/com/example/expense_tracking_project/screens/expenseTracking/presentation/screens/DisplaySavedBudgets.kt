package com.example.expense_tracking_project.screens.expenseTracking.presentation.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.expense_tracking_project.core.local.entities.BudgetEntity
import com.example.expense_tracking_project.navigation.Screen
import com.example.expense_tracking_project.screens.expenseTracking.presentation.component.ConfirmationDialog
import com.example.expense_tracking_project.screens.expenseTracking.presentation.component.DataCard
import com.example.expense_tracking_project.screens.expenseTracking.presentation.vmModels.EditBudgetViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DisplaySavedBudgets(budgetList: List<BudgetEntity>, navController: NavController) {
    val viewModel: EditBudgetViewModel = hiltViewModel()
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
    ) {
        items(budgetList) { budget ->
            var showDeleteDialog by remember { mutableStateOf(false) }
            DataCard(
                title = " ${budget.totalAmount}",
                subtitleItems = listOf(
                    "Start: ${budget.startDate}",
                    "End: ${budget.endDate}",
                    "Budget ID: ${budget.budgetId}",
                ),
                trailingContent = {
                    Row {
                        IconButton(onClick = {
                            navController.navigate(Screen.AddBudget(budgetId = budget.budgetId))
                        }) {
                            Icon(
                                imageVector = Icons.Outlined.Edit,
                                contentDescription = "Edit Budget",
                                tint = Color.Gray
                            )
                        }

                        IconButton(onClick = {
                            showDeleteDialog = true
                        }) {
                            Icon(
                                imageVector = Icons.Outlined.Delete,
                                contentDescription = "Delete Category",
                                tint = Color.Red
                            )
                        }
                    }
                }
            )

            if (showDeleteDialog) {
                ConfirmationDialog(
                    title = "Confirm Deletion",
                    message = "Are you sure you want to delete this budget?",
                    onConfirm = {
                        viewModel.softDeleteBudget(budget)
                        showDeleteDialog = false
                    },
                    onDismiss = {
                        showDeleteDialog = false
                    })
            }

        }
    }

}
