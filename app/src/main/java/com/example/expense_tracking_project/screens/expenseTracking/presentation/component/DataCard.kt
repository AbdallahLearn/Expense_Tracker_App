package com.example.expense_tracking_project.screens.expenseTracking.presentation.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun DataCard(
    title: String,
    subtitleItems: List<String>,
    dateStart: Date? = null,
    dateEnd: Date? = null,
    titleColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    trailingContent: @Composable (() -> Unit)? = null
) {
    val backgroundColor = MaterialTheme.colorScheme.surface
    val borderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(1.dp, borderColor, RoundedCornerShape(8.dp)),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.titleMedium, color = titleColor)
                subtitleItems.forEach {
                    Text(it, style = MaterialTheme.typography.bodySmall)
                    dateStart?.let {
                        val formatted = SimpleDateFormat("MMMM dd, yyyy", Locale.ENGLISH).format(it)
                        Text("Selected: $formatted", style = MaterialTheme.typography.bodySmall)
                    }
                    dateEnd?.let {
                        val formatted = SimpleDateFormat("MMMM dd, yyyy", Locale.ENGLISH).format(it)
                        Text("Selected: $formatted", style = MaterialTheme.typography.bodySmall)
                    }

                }
            }
            if (trailingContent != null) {
                trailingContent()
            }
        }
    }
}


