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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.expense_tracking_project.R
import com.example.expense_tracking_project.presentation.ui.resetPassword.DesignScreen
import com.example.expense_tracking_project.presentation.ui.resetPassword.FormField
import com.example.expense_tracking_project.presentation.vm.AuthState
import com.example.expense_tracking_project.presentation.vm.SignUpViewModel
import com.example.expense_tracking_project.presentation.vm.ValidationInputViewModel


@Composable
fun SignUpScreen(
    navController: NavController,
    viewModel: ValidationInputViewModel = viewModel(),
    signUpViewModel: SignUpViewModel = viewModel()
) {
    val context = LocalContext.current
    val authState by signUpViewModel.authState.observeAsState()

    // Define the form fields
    val fields = listOf(
        FormField(label = stringResource(R.string.name) , value = viewModel.name),
        FormField(label = stringResource(R.string.email), value = viewModel.email),
        FormField(label = stringResource(R.string.password), value = viewModel.password, isPassword = true),
        FormField(label = stringResource(R.string.confirm_password), value = viewModel.confirmPassword, isPassword = true)
    )

    // Design Screen UI
    DesignScreen(
        title = stringResource(R.string.signup),
        instruction =  stringResource(R.string.signup_prompt),
        fields = fields,
        buttonText =  stringResource(R.string.signup),
        onButtonClick = { updatedFields ->
            viewModel.name = updatedFields[0].value
            viewModel.email = updatedFields[1].value
            viewModel.password = updatedFields[2].value
            viewModel.confirmPassword = updatedFields[3].value

            viewModel.validateName()
            viewModel.validateEmail()
            viewModel.validatePassword()
            viewModel.validateConfirmPassword()

            if (viewModel.isFormValid(
                    name = viewModel.name,
                    email = viewModel.email,
                    password = viewModel.password,
                    confirmPassword = viewModel.confirmPassword
                )) {
                signUpViewModel.signup(
                    name = viewModel.name,
                    email = viewModel.email,
                    password = viewModel.password,
                    confirmPassword = viewModel.confirmPassword
                )
            } else {
                Toast.makeText(context, "Please fill all fields correctly", Toast.LENGTH_SHORT).show()
            }
        },
        footerText = {
            Row {
                Text(text = stringResource(R.string.already_have_account), color = Color.Black)
                Text(
                    text = stringResource(R.string.login),
                    color = Color(0xFF5C4DB7),
                    modifier = Modifier.clickable {
                        navController.navigate("login")
                    }
                )
            }
        }
    )


    // Handling different auth states
    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Authenticated -> {
                Toast.makeText(context, "Account created!", Toast.LENGTH_SHORT).show()
                navController.navigate("login") {
                    popUpTo("signup") { inclusive = true }
                }
            }
            is AuthState.Error -> {
                Toast.makeText(context, (authState as AuthState.Error).message, Toast.LENGTH_LONG).show()
            }
            else -> Unit
        }
    }
}
