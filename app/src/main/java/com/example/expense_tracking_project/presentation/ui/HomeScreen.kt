package com.example.expense_tracking_project.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import com.example.expense_tracking_project.R


@Composable
fun HomeScreen(navController: NavController) {
        Scaffold(
            bottomBar = {
                CustomBottomBar(
                    selectedIndex = 0,
                    onItemSelected = { index ->
                        // Handle navigation
                    },
                    onFabClick = {
                        // Handle FAB click
                    }
                )
            }
        ) { padding ->
            Box(modifier = Modifier.fillMaxSize()) {

                // Curved Background Box
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .background(
                            color = Color(0xFF5C4DB7),
                            shape = RoundedCornerShape(bottomStart = 35.dp, bottomEnd = 35.dp)
                        )
                )

                // Foreground content
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(horizontal = 16.dp)
                ) {
                    Spacer(modifier = Modifier.height(24.dp)) // Top spacing
                    TopSection(
                        name = "Enjelin Morgeana")
                    BudgetCard(income = 0.0, expenses = 0.0)
                    TimeTabSection()
                    RecentTransactions()
                }
            }
        }
    }
@Composable
fun TopSection(name: String ) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(stringResource(id = R.string.welcome), style = MaterialTheme.typography.labelMedium , color = Color.White)
            Text(name, style = MaterialTheme.typography.titleMedium , color = Color.White)
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { /* Search */ }) {
                Icon(Icons.Default.Search, contentDescription = stringResource(id = R.string.search), tint = Color.White)
            }
            IconButton(onClick = {}) {
                Icon(Icons.Default.DarkMode, contentDescription =  stringResource(id = R.string.theme) , tint = Color.White)
            }
        }
    }
}
@Composable
fun BudgetCard(income: Double, expenses: Double) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(210.dp)
            .padding(16.dp), // Add padding to the entire card
        shape = RoundedCornerShape(16.dp), // Rounded corners
        colors = CardDefaults.cardColors(containerColor = Color(0xFF5C4DB7)) // Card background color
    ) {
        Column(
            modifier = Modifier.padding(16.dp)  // Add padding to the column inside the card
        ) {
            // Top Card (Budget Text)
            Text(
                text = stringResource(R.string.budget),
                color = Color.White,
                modifier = Modifier.padding(bottom = 6.dp)  // Padding below the "Budget" text
            )

            // Bottom Card (Budget Amount with Riyal Icon)
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Riyal Icon
                Image(
                    painter = painterResource(id = R.drawable.saudi_riyal_symbol),  // Use your image resource here
                    contentDescription = "Riyal Icon",
                    modifier = Modifier.size(24.dp),  // Set the size of the icon
                    contentScale = ContentScale.Fit,
                    colorFilter = ColorFilter.tint(Color.White) // Change the color of Riyal symbol
                )

                // Budget Amount Text
                Text(
                    " 00.00",  // Replace this with dynamic value if needed
                    color = Color.White,
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(start = 8.dp)  // Padding after the image
                )
            }

            Spacer(modifier = Modifier.height(20.dp)) // Add some spacing

            // Bottom Card (Income and Expenses)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.saudi_riyal_symbol),  // Use your image resource here
                        contentDescription = "Riyal Icon",
                        modifier = Modifier.size(24.dp),
                        contentScale = ContentScale.Fit,
                        colorFilter = ColorFilter.tint(Color.White) // Change the color of Riyal symbol
                    )
                    Text(
                        "${stringResource(R.string.income)}\n $${income}",
                        color = Color.White,
                        modifier = Modifier.padding(start = 8.dp)  // Padding after the image
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.saudi_riyal_symbol),  // Use your image resource here
                        contentDescription = "Riyal Icon",
                        modifier = Modifier.size(24.dp),
                        contentScale = ContentScale.Fit,
                        colorFilter = ColorFilter.tint(Color.White)
                    )
                    Text(
                        "${stringResource(R.string.expenses)}\n $${expenses}",
                        color = Color.White,
                        modifier = Modifier.padding(start = 8.dp)  // Padding after the image
                    )
                }
            }
        }
    }
}

@Composable
fun TimeTabSection() {
    var selectedTab by remember { mutableStateOf("Today") }
    val tabs = listOf(
        stringResource(id = R.string.today),
        stringResource(id = R.string.week),
        stringResource(id = R.string.month),
        stringResource(id = R.string.year)
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        tabs.forEach { tab ->
            TextButton(
                onClick = { selectedTab = tab },
                colors = ButtonDefaults.textButtonColors(
                    containerColor = if (tab == selectedTab) Color(0xFFFFEBCD) else Color.Transparent
                )
            ) {
                Text(text = tab, color = if (tab == selectedTab) Color.Black else Color.Gray)
            }
        }
    }
}

@Composable
fun RecentTransactions() { // Later Add Lazy Column so transactions of the user appear here
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(stringResource(R.string.recent_transactions), style = MaterialTheme.typography.titleSmall)
            Text(stringResource(R.string.see_all),
                style = MaterialTheme.typography.bodySmall ,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable {  /* So the user can navigate to transactions screen */  })
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(stringResource(R.string.no_data_available), style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
    }
}



