package com.example.expense_tracking_project.screens.authentication.presentation.screens

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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.expense_tracking_project.R
import com.example.expense_tracking_project.navigation.Screen
import com.example.expense_tracking_project.screens.authentication.data.repository.AuthRepositoryImpl
import com.example.expense_tracking_project.screens.authentication.domain.usecase.LoginUseCase
import com.google.firebase.auth.FirebaseAuth


//@Composable
//fun ResetPasswordScreen(navController: NavController) {
//    val context = LocalContext.current
//    val emailState = remember { mutableStateOf("") }
//
//    // Initialize LoginUseCase
//    val loginUseCase = LoginUseCase(AuthRepositoryImpl(FirebaseAuth.getInstance()))
//
//    // Use the ViewModel factory to pass LoginUseCase
//    val signInViewModel: SignInViewModel = hiltViewModel()
//
//    DesignScreen(
//        title = "Reset Password",
//        instruction = "Enter your Gmail to reset the password",
//        fields = listOf(FormField(label = "Email", value = emailState.value)),
//        fieldStates = listOf(emailState),
//        passwordVisibilityStates = listOf(),
//        buttonText = "Send verification",
//        onButtonClick = {
//            val email = emailState.value.trim()
//            if (email.isNotEmpty()) {
//                FirebaseAuth.getInstance().sendPasswordResetEmail(email)
//                    .addOnCompleteListener { task ->
//                        if (task.isSuccessful) {
//                            Toast.makeText(context, "Reset link sent to $email", Toast.LENGTH_SHORT).show()
//                            signInViewModel.setPasswordResetCompleted(true)
//                            navController.navigate(Screen.CheckEmail.route)
//                        } else {
//                            Toast.makeText(context, "Failed to send reset email", Toast.LENGTH_SHORT).show()
//                        }
//                    }
//            } else {
//                Toast.makeText(context, "Please enter an email", Toast.LENGTH_SHORT).show()
//            }
//        },
//        onTabSelected = {}
//    )
//}
//
//@Composable
//fun CheckEmailScreen(navController: NavController) {
//    DesignScreen(
//        title = stringResource(R.string.check_your_email),
//        instruction = stringResource(R.string.recovery_email_sent),
//        buttonText = stringResource(R.string.login),
//        image = {
//            Image(
//                painter = painterResource(id = R.drawable.email),
//                contentDescription = null,
//                contentScale = ContentScale.Fit,
//                modifier = Modifier
//                    .height(150.dp)
//                    .fillMaxWidth()
//            )
//        },
//        onButtonClick = {
//            navController.navigate(Screen.Login.route) {
//                popUpTo(Screen.CheckEmail.route) { inclusive = true }
//            }
//        },
//        onTabSelected = {}
//    )
//}
//
