package com.example.expense_tracking_project.presentation.ui.resetPassword

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.expense_tracking_project.R





@Composable
fun ResetPasswordScreen(navController: NavController){
    DesignScreen(
        title = "Reset Password",
        instruction = "Enter your Gmail to reset the password",
        fields = listOf(
            FormField("Email"),
        ),
        buttonText = "Send verification",
        onButtonClick = {
            navController.navigate(Screen.CheckEmail.route)
        }
    )

}
@Composable
fun CheckEmailScreen(navController: NavController) {
    DesignScreen(
        title = "Check your mail",
        instruction = "We have send a password recover to your email",
        buttonText = "Open email app",
        image = {
            Image(
                painter = painterResource(id = R.drawable.email),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .height(200.dp)
                    .fillMaxWidth()
            )
        },
        onButtonClick = {
            navController.navigate(Screen.log.route)//nav to log in
        }
    )
}

@Composable
fun Login(navController: NavController){
    DesignScreen(
        title = "Log in",
        instruction = "",
        fields = listOf(
            FormField("Email"),
            FormField("Password", isPassword =true),
        ),
        buttonText = "Send verification",
        onButtonClick = {
            navController.navigate(Screen.CheckEmail.route)
        }
    )

}