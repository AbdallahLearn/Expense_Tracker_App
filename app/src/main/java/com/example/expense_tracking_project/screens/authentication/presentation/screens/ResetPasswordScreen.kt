package com.example.expense_tracking_project.screens.authentication.presentation.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.expense_tracking_project.R
import com.example.expense_tracking_project.navigation.Screen
import com.example.expense_tracking_project.screens.authentication.presentation.component.BackgroundLayout
import com.example.expense_tracking_project.screens.authentication.presentation.component.SimpleButton
import com.example.expense_tracking_project.screens.authentication.presentation.component.SimpleTextField
import com.example.expense_tracking_project.screens.authentication.presentation.vmModels.ForgotPasswordViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun ResetPasswordScreen(navController: NavController) {

    val context = LocalContext.current
    val forgotPasswordViewModel: ForgotPasswordViewModel = hiltViewModel()
    val emailState = remember { mutableStateOf(forgotPasswordViewModel.email) }

    BackgroundLayout(title = "Reset Password")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp) // unified horizontal padding
            .padding(top = 100.dp, bottom = 100.dp)// vertical padding
        ,
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            shape = RoundedCornerShape(32.dp),
            elevation = CardDefaults.cardElevation(8.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = Color.White,
                    shape = RoundedCornerShape(30.dp) // optional rounded corners
                )
                .wrapContentHeight()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp), // padding inside the card
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Enter your Gmail to reset the password",
                    fontSize = 14.sp,
                    color = Color.Gray
                )

                SimpleTextField(
                    title = "Email",
                    value = emailState.value,
                    onValueChange = { emailState.value = it }
                )

                SimpleButton(
                    title = "Send verification",
                    onButtonClick = {
                        val email = emailState.value.trim()
                        if (email.isNotEmpty()) {
                            forgotPasswordViewModel.forgotPassword()
                            FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                            Toast.makeText(context, "Reset link sent to $email", Toast.LENGTH_SHORT)
                                .show()
                            navController.navigate(Screen.CheckEmail)
                        } else {
                            Toast.makeText(
                                context,
                                "Failed to send reset email",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun CheckEmailScreen(navController: NavController) {
    Box(modifier = Modifier.fillMaxSize()) {
        BackgroundLayout(title = stringResource(R.string.check_your_email))

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 220.dp, start = 24.dp, end = 24.dp, bottom = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                shape = RoundedCornerShape(32.dp),
                elevation = CardDefaults.cardElevation(8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.email),
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .height(150.dp)
                            .fillMaxWidth()
                    )

                    Text(
                        text = stringResource(R.string.recovery_email_sent),
                        fontSize = 14.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )

                    SimpleButton(
                        title = stringResource(R.string.login),
                        onButtonClick = {
                            navController.navigate(Screen.Login) {
                                popUpTo(Screen.CheckEmail) { inclusive = true }
                            }
                        }
                    )
                }
            }
        }
    }
}