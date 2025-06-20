package com.example.djtimer.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.djtimer.viewModel.DJTimerViewModel

@Composable
fun TimerCountScreen(navController: NavController) {
    val viewModel: DJTimerViewModel = hiltViewModel()
    val timerState by viewModel.timerState.collectAsState()
    val timeDisplay by viewModel.timeRemainingText.collectAsState()

    // タイマー表示・Stop/Playトグル・Reset
    // 終了時 viewModel から状態を検知 → navController.navigate("done")
}