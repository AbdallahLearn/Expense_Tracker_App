package com.example.expense_tracking_project.screens.onBoardingScreen.presentation.screens


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.expense_tracking_project.R
import com.example.expense_tracking_project.ui.theme.Expense_Tracking_ProjectTheme


@Composable
fun onBoardingScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    onFinish: () -> Unit
) {
    val pagerState = rememberPagerState { 3 }
    val screenWidth = LocalConfiguration.current.screenWidthDp
    val isTablet = screenWidth > 600

    Box(modifier = Modifier.fillMaxSize()) {



        Column(
            modifier = Modifier
                .fillMaxSize()

                .padding(if (isTablet) 32.dp else 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.weight(1f)
            ) { page ->
                OnboardingPageV2(page = page, isTablet = isTablet, navController = navController, onFinish = onFinish)
            }



        }
    }
}

@Composable
fun OnboardingPageV2(
    page: Int,
    isTablet: Boolean,
    navController: NavController,
    onFinish: () -> Unit
) {
    val titles = listOf(
        stringResource(R.string.onboarding_title_1),
        stringResource(R.string.onboarding_title_2),
        stringResource(R.string.onboarding_title_3)
    )
    val descriptions = listOf(
        stringResource(R.string.onboarding_text_1),
        stringResource(R.string.onboarding_text_2),
        stringResource(R.string.onboarding_text_3)
    )
    val images = listOf(
        R.drawable.first, R.drawable.second, R.drawable.third
    )

    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(page) { visible = true }

    Expense_Tracking_ProjectTheme(dynamicColor = false) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(animationSpec = tween(1000)),
                exit = fadeOut(animationSpec = tween(500))
            ) {
                Image(
                    painter = painterResource(id = images[page]),
                    contentDescription = "Onboarding Image",
                    modifier = Modifier.size(if (isTablet) 350.dp else 250.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = titles[page],
                fontSize = if (isTablet) 28.sp else 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = descriptions[page],
                fontSize = if (isTablet) 18.sp else 16.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(40.dp))

            Row(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.Top
            ) {
                repeat(3) { index ->
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .padding(4.dp)
                            .clip(CircleShape)
                            .background(
                                if (page == index)
                                    MaterialTheme.colorScheme.primary
                                else
                                    Color.Gray
                            )
                    )
                }
            }

            if (page == 2) {
                Spacer(modifier = Modifier.height(24.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Spacer(modifier = Modifier.width(12.dp))
                    Button(
                        onClick = {
                            onFinish()
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = Color.White
                        )
                    ) {
                        Text(text = stringResource(R.string.get_started))
                    }
                }
            }
        }
    }
}
