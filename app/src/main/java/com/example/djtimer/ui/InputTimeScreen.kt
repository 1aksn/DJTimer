package com.example.djtimer.ui

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.djtimer.model.DisplayModes
import com.example.djtimer.model.InputMode
import com.example.djtimer.ui.Mode.rememberDisplayMode
import com.example.djtimer.viewModel.DJTimerViewModel
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import com.example.djtimer.HideSystemBars
import com.example.djtimer.R

@RequiresApi(Build.VERSION_CODES.S)
@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun InputTimeScreen(navController: NavController) {
    HideSystemBars()
    val viewModel: DJTimerViewModel = hiltViewModel()

    val canGo by viewModel.canGo.collectAsState()

    val startTime by viewModel.startTime.collectAsState()
    val endTime by viewModel.endTime.collectAsState()
    val playTime by viewModel.playTime.collectAsState()
    val inputMode by viewModel.inputMode.collectAsState()
    val displayMode = rememberDisplayMode()

    val (showDialog, setShowDialog) = remember { mutableStateOf(false) }
    val (dialogMessage, setDialogMessage) = remember { mutableStateOf("") }

    val totalTimeText by viewModel.totalTimeText.collectAsState()

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Blue)
    ) {
        val density = LocalDensity.current
        val parentHeight =  with(density) { constraints.maxHeight.toDp() }


        val offsetY by animateDpAsState(
            targetValue = if (displayMode == DisplayModes.FLEX) parentHeight / 4 else 0.dp,
            animationSpec = tween(durationMillis = 400),
            label = "offsetY"
        )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = parentHeight / 4), // 上半分の中央に
                contentAlignment = Alignment.TopCenter
            ) {
                Text(
                    text = totalTimeText,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White,
                    fontSize = 50.sp
                )
            }

        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .offset(y = offsetY),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            if (displayMode != DisplayModes.COVER_DISPLAY) {

                Spacer(modifier = Modifier.height(32.dp))


                // Start & End 時刻ピッカー
                TimePickerRow(
                    label = stringResource(id = R.string.start_time),
                    time = startTime,
                    enabled = inputMode != InputMode.PlayTime
                ) { viewModel.updateStartTime(it) }

                Spacer(modifier = Modifier.height(8.dp))

                TimePickerRow(
                    label = stringResource(id = R.string.end_time),
                    time = endTime,
                    enabled = inputMode != InputMode.PlayTime
                ) { viewModel.updateEndTime(it) }
            }

            Spacer(modifier = Modifier.height(32.dp))
            val keyboardController = LocalSoftwareKeyboardController.current

            // PlayTime 入力（分）
            OutlinedTextField(
                value = playTime,
                onValueChange = {
                    if (it.length <= 4) {
                        viewModel.updatePlayTime(it)
                    }
                },
                label = { Text(stringResource(id = R.string.input_hint)) },
                enabled = inputMode != InputMode.StartEnd,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done  // ← Doneにする
                ),
                keyboardActions = KeyboardActions(
                    onDone = { keyboardController?.hide() }  // ← キーボードを閉じる
                ),
                modifier = Modifier
                    .width(300.dp)   // 横幅を広げる
                    .height(60.dp)  ,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedLabelColor = Color(0xFFFFD700),
                    unfocusedLabelColor = Color(0xFFFFD700),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = Color(0xFFFFD700),
                    unfocusedBorderColor = Color(0xFFFFD700),// ← 入力文字の色を指定
                ),
                textStyle = TextStyle(fontSize = 20.sp),
            )

            Spacer(modifier = Modifier.height(24.dp))

            Box(
                modifier = Modifier
                    .animateContentSize() // ← アニメーションで自然に中央寄せが変化
                    .wrapContentSize(Alignment.Center)
                    .padding(horizontal = 60.dp)
                    .width(300.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.align(Alignment.Center)
                ) {
                    // Resetボタンは常に表示
                    Button(
                        onClick = { viewModel.reset() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFFFD700),  // 背景色
                            contentColor = Color.Blue       // 文字色
                        ),
                        shape = RoundedCornerShape(0.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(stringResource(id = R.string.reset))
                    }
                    Log.v("ろぐ", ""+canGo)

                    // Goボタンは状態に応じてフェード＋スライド表示
                    AnimatedVisibility(
                        visible = canGo,
                        enter = fadeIn(
                            animationSpec = tween(
                                durationMillis = 500,
                                easing = FastOutSlowInEasing
                            )
                        ) + slideInHorizontally(initialOffsetX = { +30 }),
                        exit = fadeOut() + slideOutHorizontally(targetOffsetX = { +30 })
                    ) {
                        Button(
                            onClick = {
                                val error = viewModel.validateInput()
                                if (error != null) {
                                    setDialogMessage(error)
                                    setShowDialog(true)
                                } else {
                                    viewModel.startTimer()
                                    viewModel.setCurrentScreen("timer")
                                    navController.navigate("timer") {
                                    }
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFFFD700),  // 背景色
                                contentColor = Color.Blue       // 文字色
                            ),
                            shape = RoundedCornerShape(0.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(stringResource(id = R.string.go))
                        }

                        if (showDialog) {
                            AlertDialog(
                                onDismissRequest = { setShowDialog(false) },
                                confirmButton = {
                                    Button(
                                        onClick = {
                                            viewModel.reset()
                                            setShowDialog(false)
                                        },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Color(0xFFFFD700),  // 背景色（黄色）
                                            contentColor = Color.Blue           // 文字色（青）
                                        ),
                                        shape = RoundedCornerShape(15.dp),
                                        modifier = Modifier.width(100.dp)
                                            .border(2.dp, Color(0xFFFF1493), RoundedCornerShape(10.dp))
                                    ) {
                                        Text("OK")
                                    }
                                },
                                title = { Text("ERROR") },
                                text = { Text(dialogMessage) },
                                containerColor = Color(0xFFFFD700),
                                shape = RoundedCornerShape(5.dp),
                                tonalElevation = 0.dp,
                                modifier = Modifier.border(4.dp, Color(0xFFFF1493), RoundedCornerShape(5.dp)).width(350.dp) // 赤い枠線
                            )
                        }
                    }
                }
            }
        }
    }
}