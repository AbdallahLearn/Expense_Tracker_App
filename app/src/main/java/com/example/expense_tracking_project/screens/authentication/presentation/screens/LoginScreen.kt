package com.example.expense_tracking_project.screens.authentication.presentation.screens

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.expense_tracking_project.R
import com.example.expense_tracking_project.navigation.Screen
import com.example.expense_tracking_project.screens.authentication.data.repository.AuthRepositoryImpl
import com.example.expense_tracking_project.screens.authentication.domain.usecase.LoginUseCase
import com.example.expense_tracking_project.screens.authentication.presentation.component.DesignScreen
import com.example.expense_tracking_project.screens.authentication.presentation.component.FormField
import com.example.expense_tracking_project.screens.authentication.presentation.vmModels.*
import com.google.firebase.auth.FirebaseAuth

@Composable
fun LoginScreen(
    navController: NavController,
    validationInputViewModel: ValidationInputViewModel = viewModel()
) {
    val context = LocalContext.current
    // These lines were removed (but are still needed):
    val email = remember { mutableStateOf(validationInputViewModel.email) }
    val password = remember { mutableStateOf(validationInputViewModel.password) }

    // Initialize LoginUseCase
    val loginUseCase = remember {
        LoginUseCase(AuthRepositoryImpl(FirebaseAuth.getInstance()))
    }
    val factory = remember { SignInViewModelFactory(loginUseCase) }
    val signInViewModel: SignInViewModel = viewModel(factory = factory)



    // Collect authState
    val authState by signInViewModel.authState.collectAsState()

    // Handle authentication state
    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Authenticated -> {
                Toast.makeText(context, "Logged in successfully", Toast.LENGTH_SHORT).show()
                navController.navigate(Screen.Home.route) {
                    popUpTo(Screen.Login.route) { inclusive = true }
                }
            }
            is AuthState.Error -> {
                val message = (authState as AuthState.Error).message
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }
            else -> Unit
        }
    }

    // UI for LoginScreen (DesignScreen, etc.)
    DesignScreen(
        title = stringResource(R.string.login),
        instruction = stringResource(R.string.login_prompt),
        fields = listOf(
            FormField(label = stringResource(R.string.email), value = email.value),
            FormField(label = stringResource(R.string.password), value = password.value, isPassword = true)
        ),
        fieldStates = listOf(email, password),
        passwordVisibilityStates = listOf(remember { mutableStateOf(false) }),
        buttonText = stringResource(R.string.login),
        rememberMeState = remember { mutableStateOf(false) },
        onForgotPassword = { navController.navigate(Screen.ResetPassword.route) },
        onButtonClick = { fields ->
            val emailInput = fields[0].value.trim()
            val passwordInput = fields[1].value.trim()
            validationInputViewModel.email = emailInput
            validationInputViewModel.password = passwordInput
            validationInputViewModel.validateEmailAndPassword()

            validationInputViewModel.emailAndPasswordError?.let {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            } ?: signInViewModel.login(emailInput, passwordInput)
        },
        footerText = {
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
                        navController.navigate(Screen.SignUp.route)
                    }
                )
            }
        },
        onTabSelected = {}
    )
}
