package com.example.expense_tracking_project.presentation.ui

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.expense_tracking_project.R
import com.example.expense_tracking_project.navigation.Screen
import com.example.expense_tracking_project.navigation.ScreenType
import com.example.expense_tracking_project.presentation.ui.resetPassword.DesignScreen
import com.example.expense_tracking_project.presentation.ui.resetPassword.FormField
import com.example.expense_tracking_project.presentation.vm.AuthState
import com.example.expense_tracking_project.presentation.vm.SignInViewModel
import com.example.expense_tracking_project.presentation.vm.ValidationInputViewModel

@Composable
fun LoginScreen(
    navController: NavController,
    signInViewModel: SignInViewModel = viewModel(),
    validationInputViewModel: ValidationInputViewModel = viewModel() // Add ValidationInputViewModel
) {
    val context = LocalContext.current
    val authState by signInViewModel.authState.observeAsState() // Observe authentication state

    // Observe validation errors
    val emailAndPasswordError = validationInputViewModel.emailAndPasswordError

    // Handle navigation to Home page after successful login
    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Authenticated -> {
                Toast.makeText(context, "Logged in successfully", Toast.LENGTH_SHORT).show()
                navController.navigate(Screen.Home.route)
            }
            is AuthState.Error -> {
                val errorMessage = (authState as AuthState.Error).message
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
            }
            else -> Unit // Handle other states if needed
        }
    }

    DesignScreen(
        title = stringResource(R.string.login),
        instruction = stringResource(R.string.login_prompt),
        fields = listOf(
            FormField(label = stringResource(R.string.email), value = validationInputViewModel.email),
            FormField(label = stringResource(R.string.password), value = validationInputViewModel.password, isPassword = true)
        ),
        fieldStates = listOf(
            remember { mutableStateOf(validationInputViewModel.email) },
            remember { mutableStateOf(validationInputViewModel.password) }
        ),
        passwordVisibilityStates = listOf(
            remember { mutableStateOf(false) }, // email (not used, but keeps indexing consistent)
            remember { mutableStateOf(false) }  // password
        ),
        buttonText = stringResource(R.string.login),
        rememberMeState = remember { mutableStateOf(false) },
        onForgotPassword = {
            navController.navigate(Screen.ResetPassword.route)
        },
        onButtonClick = { updatedFields ->
            // Update email and password in ValidationInputViewModel
            validationInputViewModel.email = updatedFields[0].value
            validationInputViewModel.password = updatedFields[1].value

            // Validate email and password
            validationInputViewModel.validateEmailAndPassword()

            // Check for validation errors
            if (emailAndPasswordError == null) {
                // Trigger login process if inputs are valid
                signInViewModel.login(
                    email = validationInputViewModel.email,
                    password = validationInputViewModel.password
                )
            } else {
                // Show validation error as a Toast message
                Toast.makeText(context, emailAndPasswordError, Toast.LENGTH_SHORT).show()
            }
        },
        footerText = {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(text = stringResource(R.string.dont_have_account), color = Color.Black)
                Text(
                    text = stringResource(R.string.signup),
                    color = Color(0xFF5C4DB7),
                    modifier = Modifier.clickable {
                        navController.navigate(Screen.SignUp.route)
                    }
                )
            }
        },
        screenType = ScreenType.LOGIN
    )
}