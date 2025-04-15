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
import com.example.expense_tracking_project.screens.authentication.domain.usecase.SignUpUseCase
import com.example.expense_tracking_project.screens.authentication.presentation.component.DesignScreen
import com.example.expense_tracking_project.screens.authentication.presentation.component.FormField
import com.example.expense_tracking_project.screens.authentication.presentation.vmModels.*
import com.google.firebase.auth.FirebaseAuth

@Composable
fun SignUpScreen(navController: NavController) {
    val context = LocalContext.current

    // ViewModels
    val signUpViewModel: SignUpViewModel = viewModel(
        factory = SignUpViewModelFactory(
            SignUpUseCase(AuthRepositoryImpl(FirebaseAuth.getInstance()))
        )
    )
    val validationViewModel: ValidationInputViewModel = viewModel()

    // State holders
    val authState = signUpViewModel.authState.collectAsState().value

    val name = remember { mutableStateOf("") }
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val confirmPassword = remember { mutableStateOf("") }
    val passwordVisibility = remember { mutableStateOf(false) }
    val confirmPasswordVisibility = remember { mutableStateOf(false) }

    // Auth result listener
    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Authenticated -> {
                Toast.makeText(context, "Sign-up successful", Toast.LENGTH_SHORT).show()
                navController.navigate(Screen.Login) {
                    popUpTo(Screen.SignUp) { inclusive = true }
                }
            }

            is AuthState.Error -> {
                Toast.makeText(context, authState.message, Toast.LENGTH_SHORT).show()
            }

            else -> Unit
        }
    }

    DesignScreen(
        title = stringResource(R.string.signup),
        instruction = stringResource(R.string.signup_prompt),
        fields = listOf(
            FormField(stringResource(R.string.name), name.value),
            FormField(stringResource(R.string.email), email.value),
            FormField(stringResource(R.string.password), password.value, isPassword = true),
            FormField(stringResource(R.string.confirm_password), confirmPassword.value, isPassword = true)
        ),
        fieldStates = listOf(name, email, password, confirmPassword),
        passwordVisibilityStates = listOf(passwordVisibility, confirmPasswordVisibility),
        buttonText = stringResource(R.string.signup),

        onButtonClick = {
            val isValid = validationViewModel.isFormValid(
                name.value,
                email.value,
                password.value,
                confirmPassword.value
            )

            if (isValid) {
                signUpViewModel.signUp(
                    name.value,
                    email.value,
                    password.value,
                    confirmPassword.value
                )
            }
        },

        footerText = {
            Column {
                validationViewModel.fieldsError?.let { error ->
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = error,
                        color = Color.Red,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(text = stringResource(R.string.already_have_account), color = Color.Black)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(R.string.login),
                        color = Color(0xFF5C4DB7),
                        modifier = Modifier.clickable {
                            navController.navigate(Screen.Login)
                        }
                    )
                }
            }
        },

        onTabSelected = {}
    )
}
