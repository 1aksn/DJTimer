package com.example.djtimer.ui

import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.djtimer.model.TimerState
import com.example.djtimer.viewModel.DJTimerViewModel
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.example.djtimer.model.DisplayModes
import com.example.djtimer.ui.Mode.rememberDisplayMode
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import androidx.navigation.NavBackStackEntry
import com.example.djtimer.HideSystemBars
import com.example.djtimer.R
import kotlin.math.tan


@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun TimerCountScreen(navController: NavController, backStackEntry: NavBackStackEntry) {

    // バックボタン無効化
    BackHandler(enabled = true) {
        // 何もしない → 戻れなくする
    }
    HideSystemBars()


    val parentEntry = remember(backStackEntry) {
        navController.getBackStackEntry("input")
    }
    val viewModel: DJTimerViewModel = hiltViewModel(parentEntry)

    LaunchedEffect(Unit) {
        viewModel.resumeTimerIfNeeded()
    }
    val timerState by viewModel.timerState.collectAsState()
    val timeDisplay by viewModel.timeRemainingText.collectAsState()
    val displayMode = rememberDisplayMode()

    var isResetting by remember { mutableStateOf(false) }

    val remainingRatio = remember(timeDisplay) {
        // 残り時間（秒）を割合に変換（適宜調整）

        val totalSeconds = viewModel.totalDurationSeconds?.toFloat() ?: 1f
        val remainingSeconds = viewModel.remainingDurationSeconds.toFloat() ?: 0f
        (remainingSeconds / totalSeconds).coerceIn(0f, 1f)
    }

    val animatedHeightFraction by animateFloatAsState(
        targetValue = when {
            isResetting -> 0f  // Reset中は必ず0（下まで下げる）
            timerState == TimerState.BeforeStart -> 0f
            else -> 1f - remainingRatio
        }, // 0f → ピンク無し、1f → 全面ピンク
        animationSpec = tween(500),
        label = "heightFraction"
    )

    val isRunning = timerState == TimerState.InProgress
    // Scaleアニメーションの値を設定
    val stopBtnScaleY by animateFloatAsState(
        targetValue = if (timerState == TimerState.InProgress || timerState == TimerState.Paused || timerState == TimerState.Done) 1f else 0f,
        animationSpec = tween(durationMillis = 500),
        label = "stopBtnScaleY"
    )

    val contentScaleY by animateFloatAsState(
        targetValue = if (isResetting) 0f else 1f,
        animationSpec = tween(500),
        label = "contentScaleY"
    )

    LaunchedEffect(timerState) {
        if (timerState == TimerState.Done) {
            navController.navigate("done") {
                popUpTo("timer") { inclusive = true }
            }
        }
    }

    BoxWithConstraints(
        modifier = Modifier.fillMaxSize()

    ) {
        val boxHeight = constraints.maxHeight.toFloat()

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Blue)
        )

        val pinkHeightPx = boxHeight * animatedHeightFraction
        val pinkHeightDp = with(LocalDensity.current) { pinkHeightPx.toDp() }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(pinkHeightDp)
                .align(Alignment.BottomStart)
                .background(Color(0xFFFF1493))
        )

        val density = LocalDensity.current
        val parentHeight = with(density) { constraints.maxHeight.toDp() }

        val timerOffsetY by animateDpAsState(
            targetValue = when (displayMode) {
                DisplayModes.FLEX -> parentHeight / -8
                else -> 0.dp
            },
            animationSpec = tween(400), label = "timerOffsetY"
        )

        val buttonOffsetY by animateDpAsState(
            targetValue = when (displayMode) {
                DisplayModes.FLEX -> parentHeight / 8
                else -> 0.dp
            },
            animationSpec = tween(400), label = "buttonOffsetY"
        )

        if (displayMode != DisplayModes.COVER_DISPLAY) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(modifier = Modifier.offset(y = timerOffsetY)) {

                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.align(Alignment.Center)
                    ) {
                        Box(
                            modifier = Modifier.graphicsLayer {
                                scaleY = contentScaleY
                                transformOrigin = TransformOrigin(0.5f, 1f)  // 下端基準（アンカーポイントを下）
                            }
                        ) {
                            Text(
                                text = if (timerState == TimerState.BeforeStart) stringResource(R.string.until_turn) else if (timerState == TimerState.InProgress || timerState == TimerState.Paused) stringResource(
                                    R.string.reaming_time
                                ) else "",
                                style = MaterialTheme.typography.headlineLarge,
                                color = Color(0xFFFFD700),
                                fontSize = 50.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))


                        Box(
                            modifier = Modifier.graphicsLayer {
                                scaleY = contentScaleY
                                transformOrigin = TransformOrigin(0.5f, 1f)  // 下端基準（アンカーポイントを下）
                            }
                        ) {
                            Text(
                                text = timeDisplay,
                                style = MaterialTheme.typography.headlineLarge,
                                color = Color.White,
                                fontSize = 80.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                Box(modifier = Modifier.offset(y = buttonOffsetY)) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.align(Alignment.Center)
                    ) {
                        Box(
                            modifier = Modifier
                                .graphicsLayer {
                                    scaleY = if (!isResetting) stopBtnScaleY else contentScaleY
                                    transformOrigin = TransformOrigin(0.5f, 1f)  // 下端基準（アンカーポイントを下）
                                }
                        ) {
                            Button(
                                onClick = {
                                    if (isRunning) viewModel.stopTimer()
                                    else viewModel.startTimer()
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFFFFD700),  // 背景色
                                    contentColor = Color.Blue       // 文字色
                                ),
                                shape = RoundedCornerShape(0.dp),
                                modifier = Modifier
                                    .width(300.dp)
                                    .height(90.dp)
                            ) {
                                Text(
                                    if (isRunning) stringResource(id = R.string.stop) else stringResource(
                                        id = R.string.start
                                    ),
                                    fontSize = 50.sp
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))

                        Box(
                            modifier = Modifier
                                .graphicsLayer {
                                    scaleY = contentScaleY
                                    transformOrigin = TransformOrigin(0.5f, 1f)  // 下端基準（アンカーポイントを下）
                                }
                        ) {
                            Button(
                                onClick = {
                                    isResetting = true // まずピンクを下げる
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
                                    if (animatedHeightFraction == 0f && contentScaleY == 0f) {
                                        viewModel.reset()
                                        navController.popBackStack("input", false)
                                        break
                                    }
                                    kotlinx.coroutines.delay(50)
                                }
                            }
                        }
                    }
                }
            }
        } else {
            // COVER: タイマーのみ中央に表示
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.align(Alignment.Center)
                ) {
                    Text(
                        text = if (timerState == TimerState.BeforeStart) stringResource(R.string.until_turn) else if (timerState == TimerState.InProgress || timerState == TimerState.Paused) stringResource(
                            R.string.reaming_time
                        ) else "",
                        style = MaterialTheme.typography.headlineLarge,
                        color = Color(0xFFFFD700),
                        fontSize = 50.sp
                    )


                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = timeDisplay,
                        style = MaterialTheme.typography.headlineLarge,
                        color = Color.White,
                        fontSize = 110.sp
                    )

                }
            }
        }
    }
}