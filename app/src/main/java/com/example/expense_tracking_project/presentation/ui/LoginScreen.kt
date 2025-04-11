package com.example.expense_tracking_project.presentation.ui

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
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
import com.example.expense_tracking_project.presentation.ui.resetPassword.DesignScreen
import com.example.expense_tracking_project.presentation.ui.resetPassword.FormField
import com.example.expense_tracking_project.presentation.vm.AuthState
import com.example.expense_tracking_project.presentation.vm.SignInViewModel
import com.example.expense_tracking_project.presentation.vm.ValidationInputViewModel

@Composable
fun LoginScreen(
    navController: NavController,
    signInViewModel: SignInViewModel = viewModel(),
    validationViewModel: ValidationInputViewModel = viewModel()
) {
    val context = LocalContext.current
    val authState by signInViewModel.authState.observeAsState()

    val emailState = remember { mutableStateOf("") }
    val passwordState = remember { mutableStateOf("") }
    val rememberMe = remember { mutableStateOf(false) }

    val fields = listOf(
        FormField(label = stringResource(R.string.email), value = emailState.value),
        FormField(label = stringResource(R.string.password), value = passwordState.value, isPassword = true)
    )
    val fieldStates = listOf(emailState, passwordState)
    val passwordVisibilityStates = listOf(
        remember { mutableStateOf(false) }, // email not needed but keeps index
        remember { mutableStateOf(false) }
    )

    DesignScreen(
        title = stringResource(R.string.login),
        instruction = stringResource(R.string.login_prompt),
        fields = fields,
        fieldStates = fieldStates,
        passwordVisibilityStates = passwordVisibilityStates,
        buttonText = stringResource(R.string.login),
        rememberMeState = rememberMe,
        emailError = validationViewModel.emailError,
        passwordError = validationViewModel.passwordError,
        onForgotPassword = {
            navController.navigate(Screen.ResetPassword.route)
        },
        onButtonClick = { updatedFields ->
            val email = updatedFields[0].value
            val password = updatedFields[1].value

            validationViewModel.email = email
            validationViewModel.password = password

            validationViewModel.validateEmail()
            validationViewModel.validatePassword()

            if (validationViewModel.emailError == null && validationViewModel.passwordError == null) {
                signInViewModel.login(email = email, password = password)
            }
        },
        footerText = {
            Row(
                modifier = Modifier.fillMaxWidth(),
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
        }
    )

    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Authenticated -> {
                Toast.makeText(context, "Success Login!", Toast.LENGTH_SHORT).show()
                navController.navigate(Screen.Home.route) {
                    popUpTo(Screen.Login.route) { inclusive = true }
                }
            }
            is AuthState.Error -> {
                Toast.makeText(context, (authState as AuthState.Error).message, Toast.LENGTH_LONG).show()
            }
            else -> Unit
        }
    }
}
