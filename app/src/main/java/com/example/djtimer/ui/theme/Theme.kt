package com.example.djtimer.ui.theme


import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable

@Composable
fun DJTimerTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        typography = AppTypography,      // ← ここでLuckiest Guy適用
        content = content
    )
}