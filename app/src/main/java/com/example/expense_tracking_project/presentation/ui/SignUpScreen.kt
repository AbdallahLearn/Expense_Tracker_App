package com.example.expense_tracking_project.presentation.ui


import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.expense_tracking_project.presentation.vm.AuthState
import com.example.expense_tracking_project.presentation.vm.SignUpViewModel
import com.example.expense_tracking_project.presentation.vm.ValidationInputViewModel

@Composable
fun SignUpScreen(navController : NavController,
                 viewModel: ValidationInputViewModel = viewModel() ,
                 signUpViewModel: SignUpViewModel = viewModel() ) {
    val name = viewModel.name
    val email = viewModel.email
    val password = viewModel.password
    val confirmPassword = viewModel.confirmPassword
    var showPassword by remember { mutableStateOf(false) }
    var showConfirmPassword by remember { mutableStateOf(false) }

    val authState by signUpViewModel.authState.observeAsState()
    val context = LocalContext.current

    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Authenticated -> {
                Toast.makeText(context, "Account created!", Toast.LENGTH_SHORT).show()
                navController.navigate("login")
            }
            is AuthState.Error -> {
                val errorMessage = (authState as AuthState.Error).message
                Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
            }
            else -> Unit
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Top Purple Curve Area
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color(0xFF5C4DB7), Color(0xFF5C4DB7))
                    ),
                    shape = RoundedCornerShape(bottomStart = 80.dp, bottomEnd = 80.dp)
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 60.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Sign Up",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }
        }

        // Card Content
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(top = 130.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                shape = RoundedCornerShape(16.dp),
                elevation = 8.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                ) {
                    // Name
                    Text(
                        text = "NAME",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Gray,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    OutlinedTextField(
                        value = name,
                        onValueChange = { viewModel.name = it
                            viewModel.validateName()},
                        isError = viewModel.nameError != null,
                        modifier = Modifier.fillMaxWidth()
                    )
                    if (viewModel.nameError != null){
                        Text(
                            text = viewModel.nameError ?: "" ,color = Color.Red
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Email
                    Text(
                        text = "EMAIL",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Gray,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    OutlinedTextField(
                        value = email,
                        onValueChange = { viewModel.email = it
                            viewModel.validateEmail() },
                        //keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email),
                        isError = viewModel.emailError != null,
                        modifier = Modifier.fillMaxWidth()
                    )
                    if (viewModel.emailError != null) {
                        Text(
                            text = viewModel.emailError ?: "", color = Color.Red)
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Password
                    Text(
                        text = "PASSWORD",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Gray,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    OutlinedTextField(
                        value = password,
                        onValueChange = { viewModel.password = it
                            viewModel.validatePassword() },
                        visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(onClick = { showPassword = !showPassword }) {
                                Icon(
                                    imageVector = if (showPassword) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                    contentDescription = "Toggle Password"
                                )
                            }
                        },
                        isError = viewModel.passwordError != null,
                        modifier = Modifier.fillMaxWidth()
                    )
                    if (viewModel.passwordError != null) {
                        Text(text = viewModel.passwordError ?: "", color = Color.Red)
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Confirm Password
                    Text(
                        text = "CONFIRM PASSWORD",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Gray,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    OutlinedTextField(
                        value = confirmPassword,
                        onValueChange = { viewModel.confirmPassword = it
                            viewModel.validateConfirmPassword() },
                        visualTransformation = if (showConfirmPassword) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(onClick = { showConfirmPassword = !showConfirmPassword }) {
                                Icon(
                                    imageVector = if (showConfirmPassword) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                    contentDescription = "Toggle Confirm Password"
                                )
                            }
                        },
                        isError = viewModel.confirmPasswordError != null,
                        modifier = Modifier.fillMaxWidth()
                    )
                    if (viewModel.confirmPasswordError != null) {
                        Text(text = viewModel.confirmPasswordError ?: "", color = Color.Red)
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Sign Up Button
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Button(
                            onClick = {
                                val isValid = viewModel.nameError == null && viewModel.emailError == null && viewModel.passwordError == null && viewModel.confirmPasswordError == null
                                if (isValid){
                                    signUpViewModel.signup(
                                        name = viewModel.name,
                                        email = viewModel.email,
                                        password = viewModel.password,
                                        confirmPassword = viewModel.confirmPassword
                                    )
                                   // navController.navigate("login")
                                }
                            },
                            modifier = Modifier
                                .width(220.dp)
                                .height(48.dp),
                            shape = RoundedCornerShape(50),
                            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF5C4DB7))
                        )
                        {
                            Text("Sign Up", color = Color.White, fontSize = 16.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Already have account
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Already have an account?",
                            color = Color(0xFF5C4DB7),
                            fontWeight = FontWeight.SemiBold,
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            "Sign In",
                            color = Color(0xFF000000),
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.clickable {
                                navController.navigate("login")
                            }
                        )
                    }
                }
            }
        }
    }
}
