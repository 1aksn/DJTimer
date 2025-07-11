package com.example.djtimer.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.example.djtimer.HideSystemBars
import com.example.djtimer.R
import com.example.djtimer.viewModel.DJTimerViewModel
import kotlinx.coroutines.delay

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun DoneScreen(navController: NavController, backStackEntry: NavBackStackEntry) {
    val parentEntry = remember(backStackEntry) {
        navController.getBackStackEntry("input")
    }
    val viewModel: DJTimerViewModel = hiltViewModel(parentEntry)

    var isResetting by remember { mutableStateOf(false) }

    val animatedHeightFraction by animateFloatAsState(
        targetValue = if (isResetting) 1f else 0f,
        animationSpec = tween(500),
        label = "heightFraction"
    )

    val contentScaleY by animateFloatAsState(
        targetValue = if (isResetting) 0f else 1f,
        animationSpec = tween(500),
        label = "contentScaleY"
    )

    HideSystemBars()

    BoxWithConstraints(
        modifier = Modifier.fillMaxSize()

    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFFF1493))
        )

        val blueHeightPx = constraints.maxHeight * animatedHeightFraction
        val blueHeightDp = with(LocalDensity.current) { blueHeightPx.toDp() }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(blueHeightDp)
                .align(Alignment.BottomStart)
                .background(Color.Blue)
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
            ) {
            Box(
                modifier = Modifier.graphicsLayer {
                    scaleY = contentScaleY
                    transformOrigin = TransformOrigin(0.5f, 0f)  // 下端基準（アンカーポイントを下）
                }
            ) {
                Text(
                    stringResource(id = R.string.done), fontSize = 100.sp,
                    style = MaterialTheme.typography.headlineLarge,
                    color = Color.White
                )
            }
            Box( modifier = Modifier.graphicsLayer {
                scaleY = contentScaleY
                transformOrigin = TransformOrigin(0.5f, 0f)  // 下端基準（アンカーポイントを下）
            }) {
                Button(
                    onClick = {
                        isResetting = true
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFFD700),  // 背景色
                        contentColor = Color.Blue       // 文字色
                    ),
                    shape = RoundedCornerShape(0.dp),
                    modifier = Modifier.width(300.dp)
                ) {
                    Text(
                        stringResource(id = R.string.reset),
                        fontSize = 20.sp
                    )
                }
            }
            LaunchedEffect(isResetting) {
                if (isResetting) {
                    while (true) {
                        if (animatedHeightFraction == 1f && contentScaleY == 0f) {

                            viewModel.reset()
                            viewModel.setCurrentScreen("input")
                            navController.popBackStack("input", false)
                            break
                        }
                        delay(50)
                    }
                }
            }
        }
    }
}