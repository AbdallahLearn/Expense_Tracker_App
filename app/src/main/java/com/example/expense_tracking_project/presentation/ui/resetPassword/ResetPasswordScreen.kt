package com.example.expense_tracking_project.presentation.ui.resetPassword

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.expense_tracking_project.R
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
                    .height(200.dp)
                    .fillMaxWidth()
            )
        },
        onButtonClick = {
            navController.navigate(Screen.Login.route)//nav to log in
        }
    )
}

