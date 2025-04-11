package com.example.expense_tracking_project.presentation.ui.resetPassword

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.expense_tracking_project.R
import com.google.firebase.auth.FirebaseAuth

@Composable
fun ResetPasswordScreen(navController: NavController) {
    val context = LocalContext.current
    val emailState = remember { mutableStateOf("") }

    DesignScreen(
        title = "Reset Password",
        instruction = "Enter your Gmail to reset the password",
        fields = listOf(FormField("Email")),
        fieldStates = listOf(emailState),
        passwordVisibilityStates = listOf(remember { mutableStateOf(false) }),
        buttonText = "Send verification",
        onButtonClick = {
            val email = emailState.value.trim()
            if (email.isNotEmpty()) {
                resetPassword(email) { success ->
                    if (success) {
                        Toast.makeText(context, "Reset link sent to $email", Toast.LENGTH_SHORT)
                            .show()
                        navController.navigate(Screen.CheckEmail.route)
                    } else {
                        Toast.makeText(context, "Failed to send reset email", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            } else {
                Toast.makeText(context, "Please enter an email", Toast.LENGTH_SHORT).show()
            }
        }
    )
}



import com.example.expense_tracking_project.navigation.Screen


@Composable
fun ResetPasswordScreen(navController: NavController) {
    DesignScreen(
        title = stringResource(R.string.reset_password),
        instruction = stringResource(R.string.enter_email_to_reset),
        fields = listOf(
            FormField(stringResource(R.string.email))
        ),
        buttonText = stringResource(R.string.send_verification),
        onButtonClick = { fields ->
            val email = fields.firstOrNull()?.value ?: ""
            if (email.isNotBlank()) {
                navController.navigate(Screen.CheckEmail.route)
            } else {
                // Show error message if needed
            }
        }
    )
}

@Composable
fun CheckEmailScreen(navController: NavController) {
    DesignScreen(
        title = stringResource(R.string.check_your_email),
        instruction = stringResource(R.string.recovery_email_sent),
        buttonText = stringResource(R.string.login),
        image = {
            Image(
                painter = painterResource(id = R.drawable.email),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .height(150.dp)
                    .fillMaxWidth()
            )
        },
        onButtonClick = {
            navController.navigate(Screen.Login.route)//nav to log in
        }
    )
}

@Composable
fun Login(navController: NavController) {
    val emailState = remember { mutableStateOf("") }
    val passwordState = remember { mutableStateOf("") }
    val passwordVisibilityState = remember { mutableStateOf(false) }

    DesignScreen(
        title = "Log in",
        instruction = "",
        fields = listOf(
            FormField("Email"),
            FormField("Password", isPassword = true)
        ),
        fieldStates = listOf(emailState, passwordState),
        passwordVisibilityStates = listOf(
            passwordVisibilityState,
            passwordVisibilityState
        ),
        buttonText = "Send verification",
        onButtonClick = {
            navController.navigate(Screen.CheckEmail.route)
        }
    )
}


fun resetPassword(email: String, onResult: (Boolean) -> Unit) {
    FirebaseAuth.getInstance().sendPasswordResetEmail(email)
        .addOnCompleteListener { task ->
            onResult(task.isSuccessful)
        }
}

