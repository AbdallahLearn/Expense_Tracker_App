package com.example.expense_tracking_project.presentation.ui.resetpassword


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon

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
    buttonText: String = "",
    onButtonClick: (List<FormField>) -> Unit,
    image: (@Composable () -> Unit)? = null
) {
    val fieldStates = remember {
        fields.map { mutableStateOf(it.value) }
    }

    val passwordVisibilityStates = remember {
        fields.map { mutableStateOf(false) }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        // Purple curved header
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
                modifier = Modifier.padding(top = 80.dp)
            )
        }

        Card(
            shape = RoundedCornerShape(32.dp),
            elevation = CardDefaults.cardElevation(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(top = 150.dp, start = 24.dp, end = 24.dp, bottom = 150.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(24.dp),
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
                    val passwordVisible = passwordVisibilityStates[index]
                    Text(
                        text = field.label,
                        color = Color.Gray,
                        fontSize = 14.sp,
                        modifier = Modifier
                            .padding(bottom = 4.dp)
                            .align(Alignment.Start)
                    )
                    OutlinedTextField(
                        value = textState.value,
                        onValueChange = { textState.value = it },
                        singleLine = true,
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
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                }

                Spacer(modifier = Modifier.height(100.dp))

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
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
