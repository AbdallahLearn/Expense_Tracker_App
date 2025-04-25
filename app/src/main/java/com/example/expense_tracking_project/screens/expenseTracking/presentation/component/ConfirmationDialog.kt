package com.example.expense_tracking_project.screens.expenseTracking.presentation.component

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.expense_tracking_project.R

@Composable
fun ConfirmationDialog(
    title: String,
    message: String,
     confirmButtonText:String = stringResource(R.string.confirm_button_text),
    dismissButtonText: String = stringResource(R.string.dismiss_button_text),

    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = title) },
        text = { Text(text = message) },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(confirmButtonText)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(dismissButtonText)
            }
        }
    )
}
