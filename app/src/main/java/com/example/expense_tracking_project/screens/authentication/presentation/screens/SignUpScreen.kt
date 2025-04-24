package com.example.expense_tracking_project.screens.authentication.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.expense_tracking_project.R
import com.example.expense_tracking_project.navigation.Screen
import com.example.expense_tracking_project.screens.authentication.presentation.component.BackgroundLayout
import com.example.expense_tracking_project.screens.authentication.presentation.component.SimpleButton
import com.example.expense_tracking_project.screens.authentication.presentation.component.SimpleTextField
import com.example.expense_tracking_project.screens.authentication.presentation.vmModels.SignUpViewModel
import com.example.expense_tracking_project.screens.authentication.presentation.vmModels.handleAuthStateSignUp

@Composable
fun SignUpScreen(navController: NavController) {
    val context = LocalContext.current
    val signUpViewModel: SignUpViewModel = hiltViewModel()
    val authState = signUpViewModel.authState.collectAsState().value

    LaunchedEffect(authState) {
        handleAuthStateSignUp(authState, context, navController)
    }

    BackgroundLayout(title = stringResource(R.string.signup))

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 100.dp, start = 24.dp, end = 24.dp, bottom = 100.dp)
            .border(
                width = 1.dp,
                color = Color.White,
                shape = RoundedCornerShape(30.dp) // optional rounded corners
            ),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            shape = RoundedCornerShape(32.dp),
            elevation = CardDefaults.cardElevation(8.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(18.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Spacer(modifier = Modifier.height(8.dp))

                SimpleTextField( // name
                    title = stringResource(R.string.name),
                    value = signUpViewModel.name,
                    onValueChange = { signUpViewModel.name = it }
                )
                Spacer(modifier = Modifier.height(8.dp))

                SimpleTextField( // email
                    title = stringResource(R.string.email),
                    value = signUpViewModel.email,
                    onValueChange = { signUpViewModel.email = it }
                )
                Spacer(modifier = Modifier.height(8.dp))

                SimpleTextField(
                    // password
                    title = stringResource(R.string.password),
                    value = signUpViewModel.password,
                    onValueChange = { signUpViewModel.password = it },
                    isPassword = true,
                )
                Spacer(modifier = Modifier.height(8.dp))

                SimpleTextField(
                    // confirm password
                    title = stringResource(R.string.confirm_password),
                    value = signUpViewModel.confirmPassword,
                    onValueChange = { signUpViewModel.confirmPassword = it },
                    isPassword = true,
                )
                Spacer(modifier = Modifier.height(8.dp))

                SimpleButton("Sign Up") {
                    signUpViewModel.signUp(
                        name = signUpViewModel.name,
                        email = signUpViewModel.email,
                        password = signUpViewModel.password,
                        confirmPassword = signUpViewModel.confirmPassword
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = stringResource(R.string.already_have_account),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = stringResource(R.string.login),
                        color = Color(0xFF5C4DB7),
                        modifier = Modifier.clickable {
                            navController.navigate(Screen.Login)
                        }
                    )
                }
            }
        }
    }
}