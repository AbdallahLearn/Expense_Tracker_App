package com.example.expense_tracking_project.presentation.ui.resetPassword

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.expense_tracking_project.R
import com.example.expense_tracking_project.navigation.ScreenType


data class FormField(
    val label: String,
    var value: String = "",
    val isPassword: Boolean = false
)

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
    screenType: ScreenType,
    emailError: String? = null,
    passwordError: String? = null
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

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        val screenWidth = maxWidth
        val isCompact = screenWidth < 400.dp
        val horizontalPadding = if (isCompact) 16.dp else 24.dp
        val cardTopPadding = if (isCompact) 100.dp else 150.dp
        val cardBottomPadding = if (isCompact) 50.dp else 150.dp
        val fontTitleSize = if (isCompact) 18.sp else 20.sp
        val fontLabelSize = if (isCompact) 10.sp else 14.sp
        val fontButtonSize = if (isCompact) 14.sp else 16.sp
        val textFieldHeight = if (isCompact) 50.dp else 56.dp
        val textFieldFontSize = if (isCompact) 14.sp else 16.sp

        // Top Box (Header)
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
                fontSize = fontTitleSize,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 60.dp)
            )
        }

        // Main Card
        val cardModifier = if (screenType == ScreenType.LOGIN || screenType == ScreenType.RESET_PASSWORD ) {
            Modifier
                .fillMaxWidth()
                .height(700.dp) // 👈 Example: smaller height for Login screen
                .padding(top = cardTopPadding, start = horizontalPadding, end = horizontalPadding, bottom = cardBottomPadding)
        } else {
            Modifier
                .fillMaxWidth()
                .fillMaxHeight() // 👈 default full height
                .padding(top = cardTopPadding, start = horizontalPadding, end = horizontalPadding, bottom = cardBottomPadding)
        }

        Card(
            shape = RoundedCornerShape(32.dp),
            elevation = CardDefaults.cardElevation(8.dp),
            modifier = cardModifier
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
                    fontSize = fontLabelSize,
                    modifier = Modifier.padding(end = 55.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                image?.invoke()

                fields.forEachIndexed { index, field ->
                    val textState = fieldStates[index]
                    val passwordVisible = passwordVisibilityStatesSafe[index]

                    Text(
                        text = field.label,
                        color = Color.Gray,
                        fontSize = fontLabelSize,
                        modifier = Modifier
                            .padding(bottom = 1.dp)
                            .align(Alignment.Start)
                    )

                    OutlinedTextField(
                        value = textState.value,
                        onValueChange = { textState.value = it },
                        singleLine = true,
                        textStyle = TextStyle(color = Color.Black, fontSize = textFieldFontSize),
                        visualTransformation = if (field.isPassword && !passwordVisible.value)
                            PasswordVisualTransformation() else VisualTransformation.None,
                        trailingIcon = {
                            if (field.isPassword) {
                                IconButton(onClick = {
                                    passwordVisible.value = !passwordVisible.value
                                }) {
                                    Icon(
                                        imageVector = if (passwordVisible.value)
                                            Icons.Filled.Visibility
                                        else Icons.Filled.VisibilityOff,
                                        contentDescription = "Toggle Password Visibility"
                                    )
                                }
                            }
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF5C4DB7),
                            unfocusedBorderColor = Color(0xFF5C4DB7),
                            cursorColor = Color(0xFF5C4DB7),
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(textFieldHeight)
                    )

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
                                    fontSize = fontLabelSize
                                )
                            }
                        }

                        onForgotPassword?.let {
                            Text(
                                text = stringResource(R.string.forgot_password),
                                color = Color(0xFF5C4DB7),
                                fontSize = fontLabelSize,
                                modifier = Modifier
                                    .padding(top = 6.dp)
                                    .clickable { it() }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                }

                Spacer(modifier = Modifier.height(10.dp))

                Button(
                    onClick = {
                        val updatedFields = fields.mapIndexed { i, field ->
                            field.copy(value = fieldStates[i].value)
                        }
                        onButtonClick(updatedFields)
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
                        fontSize = fontButtonSize,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                footerText?.invoke()
            }
        }
    }
}
