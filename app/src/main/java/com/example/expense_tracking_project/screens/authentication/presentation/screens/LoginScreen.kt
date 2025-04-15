package com.example.expense_tracking_project.screens.authentication.presentation.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.expense_tracking_project.R
import com.example.expense_tracking_project.navigation.Screen
import com.example.expense_tracking_project.screens.authentication.presentation.component.BackgroundLayout
import com.example.expense_tracking_project.screens.authentication.presentation.component.SimpleButton
import com.example.expense_tracking_project.screens.authentication.presentation.component.SimpleTextField
import com.example.expense_tracking_project.screens.authentication.presentation.vmModels.AuthState
import com.example.expense_tracking_project.screens.authentication.presentation.vmModels.SignInViewModel
import com.example.expense_tracking_project.screens.authentication.presentation.vmModels.ValidationInputViewModel

@Composable
fun LoginScreen(
    navController: NavController,
    validationInputViewModel: ValidationInputViewModel = viewModel()
) {
    val context = LocalContext.current
    val email = remember { mutableStateOf(validationInputViewModel.email) }
    val password = remember { mutableStateOf(validationInputViewModel.password) }

    val signInViewModel: SignInViewModel = hiltViewModel()
    val authState by signInViewModel.authState.collectAsState()

    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Authenticated -> {
                Toast.makeText(context, "Logged in successfully", Toast.LENGTH_SHORT).show()
                navController.navigate(Screen.Home) {
                    popUpTo(Screen.Login) { inclusive = true }
                }
            }
            is AuthState.Error -> {
                val message = (authState as AuthState.Error).message
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }
            else -> Unit
        }
    }


        BackgroundLayout("Login")
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 100.dp, start = 24.dp, end = 24.dp, bottom = 100.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                shape = RoundedCornerShape(32.dp),
                elevation = CardDefaults.cardElevation(8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White)
                        .padding(18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {
                    Spacer(modifier = Modifier.height(100.dp))
                    SimpleTextField("Email", value = email.value, onValueChange = { email.value = it })
                    Spacer(modifier = Modifier.height(20.dp))
                    SimpleTextField("Password", value = password.value, onValueChange = { password.value = it }, isPassword = true)
                    Spacer(modifier = Modifier.height(20.dp))

                    // Using SimpleButton with two parameters: title and onClick action
                    SimpleButton("Save") {
                        val emailInput = email.value.trim()
                        val passwordInput = password.value.trim()

                        validationInputViewModel.email = emailInput
                        validationInputViewModel.password = passwordInput
                        validationInputViewModel.validateEmailAndPassword()

                        validationInputViewModel.emailAndPasswordError?.let {
                            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                        } ?: signInViewModel.login(emailInput, passwordInput)
                    }
                     Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(text = stringResource(R.string.dont_have_account), color = Color.Black)
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = stringResource(R.string.signup),
                            color = Color(0xFF5C4DB7),
                            modifier = Modifier.clickable {
                                navController.navigate(Screen.SignUp)
                            }
                        )
                    }
                }
            }



        }
    }


