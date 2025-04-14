package com.example.expense_tracking_project.screens.authentication.presentation.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.expense_tracking_project.R
import com.example.expense_tracking_project.navigation.Screen
import com.example.expense_tracking_project.screens.authentication.presentation.component.DesignScreen
import com.example.expense_tracking_project.screens.authentication.presentation.component.FormField
import com.example.expense_tracking_project.screens.authentication.presentation.vmModels.SignUpViewModel
import com.example.expense_tracking_project.screens.authentication.presentation.vmModels.ValidationInputViewModel

@Composable
fun SignUpScreen(
    navController: NavController,
    viewModel: ValidationInputViewModel = viewModel(),
    signUpViewModel: SignUpViewModel = viewModel()
) {
    val context = LocalContext.current
    var navigateToLogin by remember { mutableStateOf(false) }

    // Observe validation errors
    val fieldsError = viewModel.fieldsError

    // Define the form fields
    val nameState = remember { mutableStateOf(viewModel.name) }
    val emailState = remember { mutableStateOf(viewModel.email) }
    val passwordState = remember { mutableStateOf(viewModel.password) }
    val confirmPasswordState = remember { mutableStateOf(viewModel.confirmPassword) }

    val fields = listOf(
        FormField(label = stringResource(R.string.name), value = nameState.value),
        FormField(label = stringResource(R.string.email), value = emailState.value),
        FormField(
            label = stringResource(R.string.password),
            value = passwordState.value,
            isPassword = true
        ),
        FormField(
            label = stringResource(R.string.confirm_password),
            value = confirmPasswordState.value,
            isPassword = true
        )
    )
    val fieldStates = listOf(nameState, emailState, passwordState, confirmPasswordState)

    // Handle navigation to the Login screen after successful sign-up
    LaunchedEffect(navigateToLogin) {
        if (navigateToLogin) {
            navController.navigate(Screen.Login.route)
        }
    }

    // Design Screen UI
    DesignScreen(
        title = stringResource(R.string.signup), // Income / Expense
        instruction = stringResource(R.string.signup_prompt), // Remove
        fields = fields,
        fieldStates = fieldStates,
        buttonText = stringResource(R.string.signup),
        onButtonClick = { updatedFields ->
            viewModel.name = updatedFields[0].value
            viewModel.email = updatedFields[1].value
            viewModel.password = updatedFields[2].value
            viewModel.confirmPassword = updatedFields[3].value

            viewModel.validateFields()

            if (viewModel.isFormValid(
                    name = viewModel.name,
                    email = viewModel.email,
                    password = viewModel.password,
                    confirmPassword = viewModel.confirmPassword
                )
            ) {
                signUpViewModel.signup(
                    name = viewModel.name,
                    email = viewModel.email,
                    password = viewModel.password,
                    confirmPassword = viewModel.confirmPassword
                )
                // Show success toast and trigger navigation
                Toast.makeText(context, "Created account successfully", Toast.LENGTH_SHORT).show()
                navigateToLogin = true
            } else {
                Toast.makeText(context, viewModel.fieldsError, Toast.LENGTH_SHORT).show()

            }
        },
        footerText = {

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(text = stringResource(R.string.already_have_account), color = Color.Black)
                Text(
                    text = stringResource(R.string.login),
                    color = Color(0xFF5C4DB7),
                    modifier = Modifier.clickable {
                        try {
                            navController.navigate(Screen.Login.route)
                        } catch (e: Exception) {
                            Log.e("SignUpScreen", "Navigation error: ${e.message}")
                            Toast.makeText(context, "Navigation failed: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                )
            }
        },
        onTabSelected = {}
    )
}