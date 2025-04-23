package com.example.expense_tracking_project.screens.authentication.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.expense_tracking_project.screens.expenseTracking.presentation.vmModels.TransactionTypeFilter
import androidx.core.graphics.toColorInt

@Composable
fun BackgroundLayout(
    title: String = "",
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .background(
                    Color(0xFF5C4DB7),
                    shape = RoundedCornerShape(bottomStart = 35.dp, bottomEnd = 35.dp)
                ),
            contentAlignment = Alignment.TopCenter
        ) {
            Text(
                text = title,
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 60.dp)
            )
        }
    }
}

@Composable
fun SimpleTextField(
    title: String = "",
    isPassword: Boolean = false,
    value: String,
    onValueChange: (String) -> Unit,
    onIconClick: (() -> Unit)? = null, // trigger for calendar icon
) {
    var passwordVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 0.dp)
    ) {
        Text(
            text = title,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text("") },
            modifier = Modifier.fillMaxWidth(),
            readOnly = onIconClick != null, // make it read-only if calendar is being used
            visualTransformation = if (isPassword && !passwordVisible)
                PasswordVisualTransformation()
            else VisualTransformation.None,
            trailingIcon = {
                when {
                    isPassword -> {
                        val icon =
                            if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                        val description = if (passwordVisible) "Hide password" else "Show password"
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(imageVector = icon, contentDescription = description)
                        }
                    }

                    onIconClick != null -> {
                        IconButton(onClick = onIconClick) {
                            Icon(
                                imageVector = Icons.Default.DateRange,
                                contentDescription = "Select Date"
                            )
                        }
                    }
                }
            }
        )
    }
}

@Composable
fun ColorPickerDialog(
    selectedColor: String,
    onColorSelected: (String) -> Unit,
    onDismiss: () -> Unit,
) {
    val colors = listOf(
        "#F44336", "#E91E63", "#9C27B0", "#673AB7", "#3F51B5",
        "#2196F3", "#03A9F4", "#00BCD4", "#009688", "#4CAF50",
        "#8BC34A", "#CDDC39", "#FFC107", "#FF9800", "#FF5722"
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {},
        title = { Text("Choose Category Color") },
        text = {
            Column {
                colors.chunked(5).forEach { row ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        row.forEach { colorHex ->
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .padding(4.dp)
                                    .background(Color(colorHex.toColorInt()), shape = CircleShape)
                                    .clickable { onColorSelected(colorHex) }
                            )
                        }
                    }
                }
            }
        }
    )
}


@Composable
fun SimpleButton(title: String = "", onButtonClick: () -> Unit) {
    Button(
        onClick = onButtonClick,
        shape = RoundedCornerShape(50),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5C4DB7)),
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .shadow(10.dp, shape = RoundedCornerShape(50))
    ) {
        Text(
            text = title,
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

// Select Transaction Type
@Composable
fun SelectTransaction(
    showTabs: Boolean = false,
    tabOptions: List<String> = listOf(),
    onTabSelected: (String) -> Unit,
) {
    var activeButton by remember { mutableStateOf(tabOptions.firstOrNull().orEmpty()) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .background(
                    Color(0xFF5C4DB7),
                    shape = RoundedCornerShape(bottomStart = 35.dp, bottomEnd = 35.dp)
                ),
            contentAlignment = Alignment.TopCenter
        ) {
            if (showTabs && tabOptions.isNotEmpty()) {
                Box(
                    modifier = Modifier
                        .padding(top = 64.dp)
                        .width(320.dp)
                        .height(50.dp)
                        .background(
                            color = Color(0xFFF0F0F0),
                            shape = RoundedCornerShape(
                                topStart = 20.dp,
                                bottomEnd = 20.dp,
                                topEnd = 20.dp,
                                bottomStart = 20.dp
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(4.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        tabOptions.forEach { option ->
                            val isSelected = activeButton == option

                            Button(
                                onClick = {
                                    activeButton = option
                                    onTabSelected(option)
                                },
                                shape = RoundedCornerShape(50), // rounded tab shape
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (isSelected) Color.White else Color.Transparent
                                ),
                                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 0.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight()
                            ) {
                                Text(
                                    text = option,
                                    color = if (isSelected) Color(0xFF5C4DB7) else Color.Gray,
                                    fontSize = 16.sp,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDropdownMenuBudget(
    // Drop down for Budget menu
    label: String,
    BudgetOptions: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selectedOption.ifBlank { "Select" },
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            BudgetOptions.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDropdownMenu(
    // Drop down for Category menu
    label: String,
    categoryOptions: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selectedOption.ifBlank { "Select" },
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            categoryOptions.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun SelectEditingTab(
    showTabs: Boolean = false,
    tabOptions: List<String> = listOf(),
    onTabSelected: (String) -> Unit,
) {
    var activeButton by remember { mutableStateOf(tabOptions.firstOrNull().orEmpty()) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .background(
                    Color(0xFF5C4DB7),
                    shape = RoundedCornerShape(bottomStart = 35.dp, bottomEnd = 35.dp)
                ),
            contentAlignment = Alignment.TopCenter
        ) {
            if (showTabs && tabOptions.isNotEmpty()) {
                Box(
                    modifier = Modifier
                        .padding(top = 64.dp)
                        .width(320.dp)
                        .height(50.dp)
                        .background(
                            color = Color(0xFFF0F0F0),
                            shape = RoundedCornerShape(
                                topStart = 20.dp,
                                bottomEnd = 20.dp,
                                topEnd = 20.dp,
                                bottomStart = 20.dp
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(4.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        tabOptions.forEach { option ->
                            val isSelected = activeButton == option

                            Button(
                                onClick = {
                                    activeButton = option
                                    onTabSelected(option)
                                },
                                shape = RoundedCornerShape(50), // rounded tab shape
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (isSelected) Color.White else Color.Transparent
                                ),
                                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 0.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight()
                            ) {
                                Text(
                                    text = option,
                                    color = if (isSelected) Color(0xFF5C4DB7) else Color.Gray,
                                    fontSize = 16.sp,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DropdownFilter(
    typeFilter: TransactionTypeFilter,
    onTypeSelected: (TransactionTypeFilter) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(8.dp)
            .clip(RoundedCornerShape(8.dp))
    ) {
        listOf(
            TransactionTypeFilter.ALL to "All",
            TransactionTypeFilter.INCOME to "Income",
            TransactionTypeFilter.EXPENSES to "Expenses"
        ).forEach { (filterType, label) ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onTypeSelected(filterType) }
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                androidx.compose.material3.Checkbox(
                    checked = typeFilter == filterType,
                    onCheckedChange = { onTypeSelected(filterType) }
                )
                Text(text = label, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}