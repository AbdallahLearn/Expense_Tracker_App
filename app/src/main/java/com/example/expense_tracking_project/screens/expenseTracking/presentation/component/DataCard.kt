package com.example.expense_tracking_project.screens.expenseTracking.presentation.component
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AttachMoney
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
@Composable
fun DataCard(
    title: String,
    subtitleItems: List<String>,
    trailingContent: @Composable (() -> Unit)? = null,
    titleIcon: ImageVector = Icons.Outlined.Category, // Default icon
    titleColor: Color = MaterialTheme.colorScheme.onBackground // Optional for amount color
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            // Title + Icons Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Icon before the title
                Icon(
                    imageVector = titleIcon,
                    contentDescription = "Transaction Icon",
                    tint = titleColor,
                    modifier = Modifier.padding(end = 8.dp)
                )

                // Title (amount)
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = titleColor,
                    modifier = Modifier.weight(1f)
                )

                trailingContent?.let {
                    it()
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Subtitle lines
            subtitleItems.forEach { subtitle ->
                Text(text = subtitle, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}
