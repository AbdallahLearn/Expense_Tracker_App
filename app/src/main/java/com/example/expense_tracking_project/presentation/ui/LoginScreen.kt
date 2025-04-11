package com.example.expense_tracking_project.presentation.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
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
import com.example.expense_tracking_project.presentation.vm.SignInViewModel

@Composable
fun LoginScreen(
    navController: NavController,
    signInViewModel: SignInViewModel = viewModel()
) {
    val context = LocalContext.current
    val authState by signInViewModel.authState.observeAsState()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val rememberMe = remember { mutableStateOf(false) }

    val fields = listOf(
        FormField(label = stringResource(R.string.email), value = email),
        FormField(label = stringResource(R.string.password), value = password, isPassword = true)
    )

    DesignScreen(
        title = stringResource(R.string.login),
        instruction = stringResource(R.string.login_prompt),
        fields = fields,
        buttonText = stringResource(R.string.login),
        rememberMeState = rememberMe,
        onForgotPassword = {
            // Navigate to ForgotPassword screen
            navController.navigate(Screen.ResetPassword.route)
        },
        onButtonClick = { updatedFields ->
            email = updatedFields[0].value
            password = updatedFields[1].value
            signInViewModel.login(email = email, password = password)
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
        }

    )

    // Optional Auth Handling
//    LaunchedEffect(authState) {
//        when (authState) {
//            is AuthState.Authenticated -> {
//                Toast.makeText(context, "Success Login!", Toast.LENGTH_SHORT).show()
//                navController.navigate(Screen.Home.route) {
//                    popUpTo(Screen.Login.route) { inclusive = true }
//                }
//            }
//            is AuthState.Error -> {
//                Toast.makeText(context, (authState as AuthState.Error).message, Toast.LENGTH_LONG).show()
//            }
//            else -> Unit
//        }
//    }
}


