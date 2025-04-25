package com.example.expense_tracking_project.screens.expenseTracking.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.expense_tracking_project.R
import com.example.expense_tracking_project.navigation.Screen
import com.example.expense_tracking_project.screens.authentication.presentation.component.BackgroundLayout
import com.example.expense_tracking_project.screens.authentication.presentation.vmModels.SignOutViewModel
import com.example.expense_tracking_project.screens.expenseTracking.presentation.component.ConfirmationDialog
import com.example.expense_tracking_project.screens.expenseTracking.presentation.vmModels.EditProfileViewModel

@Composable
fun ProfileScreen(
    navController: NavController,
    signOutViewModel: SignOutViewModel = viewModel(),
    profileViewModel: EditProfileViewModel = viewModel(),
) {
    BackgroundLayout(title = stringResource(R.string.profile))

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 100.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            painter = painterResource(id = R.drawable.unknown),
            contentDescription = stringResource(R.string.profile_picture),
            modifier = Modifier
                .padding(top = 90.dp)
                .size(100.dp)
                .clip(CircleShape)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                profileViewModel.openSignOutDialog()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            Text(text = stringResource(R.string.sign_out))
        }

        if (profileViewModel.showSignOutDialog.value) {
            ConfirmationDialog(
                title = stringResource(R.string.sign_out),
                message = stringResource(R.string.confirm_sign_out_message),
                onConfirm = {
                    signOutViewModel.signout()
                    navController.navigate(Screen.Login) {
                        popUpTo(0) // clears backstack
                    }
                    profileViewModel.closeSignOutDialog()
                },
                onDismiss = {
                    profileViewModel.closeSignOutDialog()
                }
            )
        }
    }
}
