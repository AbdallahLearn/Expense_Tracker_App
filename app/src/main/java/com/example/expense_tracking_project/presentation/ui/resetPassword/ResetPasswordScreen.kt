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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.expense_tracking_project.R
import com.example.expense_tracking_project.navigation.Screen
import com.example.expense_tracking_project.presentation.vm.SignInViewModel
import com.google.firebase.auth.FirebaseAuth



@Composable
fun ResetPasswordScreen(navController: NavController, signInViewModel: SignInViewModel = viewModel()) {
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
                        signInViewModel.setPasswordResetCompleted(true) // Set the flag
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
            // No fields here, just navigate to Login screen
            navController.popBackStack() // Clear CheckEmail screen from the back stack
            // Navigate to Login screen and clear CheckEmail from the backstack
            navController.navigate(Screen.Login.route) {
                popUpTo(Screen.CheckEmail.route) { inclusive = true }
            }
        }
    )
}


fun resetPassword(email: String, onResult: (Boolean) -> Unit) {
    FirebaseAuth.getInstance().sendPasswordResetEmail(email)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Log out the user after successfully sending the reset email
                FirebaseAuth.getInstance().signOut()
            }
            onResult(task.isSuccessful)
        }
}