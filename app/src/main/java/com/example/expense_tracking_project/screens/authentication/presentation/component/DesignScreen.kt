package com.example.expense_tracking_project.screens.authentication.presentation.component

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.expense_tracking_project.R

data class FormField(
    val label: String,
    var value: String = "",
    val isPassword: Boolean = false,
    val onClick: (() -> Unit)? = null
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DesignScreen(
    title: String = "",
    instruction: String = "",
    fields: List<FormField> = emptyList(),
    fieldStates: List<MutableState<String>> = emptyList(),
    passwordVisibilityStates: List<MutableState<Boolean>> = emptyList(),
    buttonText: String = "",
    onButtonClick: (List<FormField>) -> Unit,
    image: (@Composable () -> Unit)? = null,
    rememberMeState: MutableState<Boolean>? = null,
    onForgotPassword: (() -> Unit)? = null,
    footerText: (@Composable () -> Unit)? = null,
    emailError: String? = null,
    passwordError: String? = null,
    onTabSelected: (String) -> Unit,
    showTabs: Boolean = false
) {
    if (fields.size != fieldStates.size) {
        Log.e("DesignScreen", "Mismatched fieldStates and fields length")
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Something went wrong. Please restart the app.")
        }
        return
    }

    val passwordVisibilityStatesSafe = fields.mapIndexed { index, _ ->
        passwordVisibilityStates.getOrNull(index) ?: remember { mutableStateOf(false) }
    }

    var activeButton by remember { mutableStateOf("Expenses") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
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
            if(showTabs) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = {
                            activeButton = "Income"
                            onTabSelected("Income")
                        },
                        shape = RectangleShape,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (activeButton == "Income") Color.White else Color(
                                0xFFF4F6F6
                            )
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .height(130.dp)
                            .padding(bottom = 85.dp)
                    ) {
                        Text(
                            text = "Income",
                            color = Color(0xFF5C4DB7),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Button(
                        onClick = {
                            activeButton = "Expenses"
                            onTabSelected("Expenses")
                        },
                        shape = RectangleShape,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (activeButton == "Expenses") Color.White else Color(
                                0xFFF4F6F6
                            )
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .height(130.dp)
                            .padding(bottom = 85.dp)
                    ) {
                        Text(
                            text = "Expenses",
                            color = Color(0xFF5C4DB7),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Text(
                text = title,
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 60.dp)
            )
        }

        Card(
            shape = RoundedCornerShape(32.dp),
            elevation = CardDefaults.cardElevation(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(top = 150.dp, start = 24.dp, end = 24.dp, bottom = 30.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(18.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Text(
                    text = instruction,
                    color = Color.Gray,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(end = 55.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                image?.let {
                    it()
                    Spacer(modifier = Modifier.height(16.dp))
                }

                fields.forEachIndexed { index, field ->
                    val textState = fieldStates[index]
                    val passwordVisible = passwordVisibilityStatesSafe[index]

                    Text(
                        text = field.label,
                        color = Color.Gray,
                        fontSize = 14.sp,
                        modifier = Modifier
                            .padding(bottom = 4.dp)
                            .align(Alignment.Start)
                    )

                    // Handle category as a dropdown
                    if (field.label.lowercase().contains("category")) {
                        var expanded by remember { mutableStateOf(false) }
                        val categoryOptions = if (activeButton == "Income") {
                            listOf("Salary", "Bonus", "Freelance", "Investment", "Other")
                        } else {
                            listOf("Food", "Transport", "Entertainment", "Shopping", "Bills", "Other")
                        }

                        ExposedDropdownMenuBox(
                            expanded = expanded,
                            onExpandedChange = { expanded = !expanded }
                        ) {
                            OutlinedTextField(
                                readOnly = true,
                                value = textState.value,
                                onValueChange = {},
                                label = { Text(field.label) },
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(expanded)
                                },
                                modifier = Modifier.fillMaxWidth()
                            )

                            ExposedDropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                categoryOptions.forEach { option ->
                                    DropdownMenuItem(
                                        text = { Text(option) },
                                        onClick = {
                                            textState.value = option
                                            expanded = false
                                        }
                                    )
                                }
                            }
                        }


                    } else {
                        OutlinedTextField(
                            value = textState.value,
                            onValueChange = {
                                if (field.onClick == null) textState.value = it
                            },
                            singleLine = true,
                            textStyle = TextStyle(color = Color.Black),
                            visualTransformation = if (field.isPassword && !passwordVisible.value)
                                PasswordVisualTransformation() else VisualTransformation.None,
                            trailingIcon = {
                                when {
                                    field.isPassword -> {
                                        IconButton(onClick = {
                                            passwordVisible.value = !passwordVisible.value
                                        }) {
                                            Icon(
                                                imageVector = if (passwordVisible.value)
                                                    Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                                                contentDescription = null
                                            )
                                        }
                                    }

                                    field.onClick != null -> {
                                        IconButton(onClick = { field.onClick?.invoke() }) {
                                            Icon(
                                                imageVector = Icons.Filled.CalendarToday,
                                                contentDescription = "Select Date",
                                                tint = Color.Gray
                                            )
                                        }
                                    }
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable(enabled = field.onClick != null) {
                                    field.onClick?.invoke()
                                },
                            readOnly = field.onClick != null,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFF5C4DB7),
                                unfocusedBorderColor = Color(0xFF5C4DB7),
                                cursorColor = Color(0xFF5C4DB7),
                                focusedTextColor = Color.Black,
                                unfocusedTextColor = Color.Black
                            )
                        )
                    }

                    val errorText = when (field.label.lowercase()) {
                        "email" -> emailError
                        "password" -> passwordError
                        else -> null
                    }

                    if (!errorText.isNullOrEmpty()) {
                        Text(
                            text = errorText,
                            color = Color.Red,
                            fontSize = 12.sp,
                            modifier = Modifier
                                .align(Alignment.Start)
                                .padding(start = 8.dp, top = 2.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                }

                if (rememberMeState != null || onForgotPassword != null) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        rememberMeState?.let {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Checkbox(
                                    checked = it.value,
                                    onCheckedChange = { checked -> it.value = checked }
                                )
                                Text(
                                    text = stringResource(R.string.remember_me),
                                    color = Color.Gray,
                                    fontSize = 14.sp
                                )
                            }
                        }

                        onForgotPassword?.let {
                            Text(
                                text = stringResource(R.string.forgot_password),
                                color = Color(0xFF5C4DB7),
                                fontSize = 14.sp,
                                modifier = Modifier
                                    .padding(top = 6.dp)
                                    .clickable { it() }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }

                Spacer(modifier = Modifier.height(50.dp))

                Button(
                    onClick = {
                        if (fields.isNotEmpty() && fieldStates.size == fields.size) {
                            val updatedFields = fields.mapIndexed { i, field ->
                                field.copy(value = fieldStates[i].value)
                            }
                            onButtonClick(updatedFields)
                        } else {
                            onButtonClick(emptyList())
                        }
                    },
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5C4DB7)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .shadow(10.dp, shape = RoundedCornerShape(50))
                ) {
                    Text(
                        text = buttonText,
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))
                footerText?.invoke()
            }
        }
    }
}
