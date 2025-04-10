//package com.example.expense_tracking_project.presentation.ui
//
//import androidx.compose.animation.AnimatedVisibility
//import androidx.compose.animation.core.tween
//import androidx.compose.animation.fadeIn
//import androidx.compose.animation.fadeOut
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.layout.width
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material3.Button
//import androidx.compose.material3.ButtonDefaults
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.OutlinedButton
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.res.stringResource
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.navigation.NavController
//
//
//@Composable
//fun onBoardingScreen(page: Int, isTablet: Boolean, navController: NavController, onFinish: () -> Unit) {
//    val titles = listOf(
//        stringResource(R.string.numerous_free_trial_courses),
//        stringResource(R.string.quick_easy_learning),
//        stringResource(R.string.create_study_plan)
//    )
//    val descriptions = listOf(
//        stringResource(R.string.free_courses_message),
//        stringResource(R.string.easy_fast_learning),
//        stringResource(R.string.study_plan_message)
//    )
//    val images = listOf(
//        R.drawable.male, R.drawable.female, R.drawable.laptop_male
//    )
//
//    var visible by remember { mutableStateOf(false) }
//    LaunchedEffect(page) { visible = true }
//
//    Column(
//        modifier = Modifier.fillMaxSize(),
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Center
//    ) {
//        AnimatedVisibility(
//            visible = visible,
//            enter = fadeIn(animationSpec = tween(1000)),
//            exit = fadeOut(animationSpec = tween(500))
//        ) {
//            Image(
//                painter = painterResource(id = images[page]),
//                contentDescription = "Onboarding Image",
//                modifier = Modifier.size(if (isTablet) 350.dp else 250.dp)
//            )
//        }
//        Spacer(modifier = Modifier.height(16.dp))
//        Text(
//            text = titles[page],
//            fontSize = if (isTablet) 28.sp else 24.sp,
//            fontWeight = FontWeight.Bold,
//            color = MaterialTheme.colorScheme.primary,
//            textAlign = TextAlign.Center
//        )

//        Spacer(modifier = Modifier.height(8.dp))
//        Text(
//            text = descriptions[page],
//            fontSize = if (isTablet) 18.sp else 16.sp,
//            color = Color.Gray,
//            textAlign = TextAlign.Center
//        )
//
//        if (page == 2) { // Last page
//            Spacer(modifier = Modifier.height(24.dp))
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.SpaceEvenly
//            ) {
//                Button(
//                    onClick = {
//                        onFinish() // Hide onboarding
//                        navController.navigate("signup")
//                    },
//                    modifier = Modifier.weight(1f),
//                    shape = RoundedCornerShape(12.dp),
//                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xff4257f5))
//                ) {
//                    Text(text = stringResource(R.string.sign_up), color = Color.White)
//                }
//                Spacer(modifier = Modifier.width(12.dp))
//                OutlinedButton(
//                    onClick = {
//                        onFinish() // Hide onboarding
//                        navController.navigate("login")
//                    },
//                    modifier = Modifier.weight(1f),
//                    shape = RoundedCornerShape(12.dp),
//                    colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.onSurface)
//                ) {
//                    Text(text = stringResource(R.string.log_in), color = Color.White)
//                }
//            }
//        }
//    }
//}