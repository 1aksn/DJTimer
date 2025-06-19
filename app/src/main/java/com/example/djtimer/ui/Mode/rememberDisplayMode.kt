package com.example.djtimer.ui.Mode

import android.app.Activity
import androidx.compose.material3.DisplayMode
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import androidx.window.layout.FoldingFeature
import androidx.window.layout.WindowInfoTracker
import com.example.djtimer.model.DisplayModes

@Composable
fun rememberDisplayMode(): DisplayModes {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val windowInfoTracker = remember { WindowInfoTracker.getOrCreate(context) }

    var displayMode by remember { mutableStateOf(DisplayModes.NORMAL) }

    LaunchedEffect(Unit) {
        windowInfoTracker
            .windowLayoutInfo(context as Activity)
            .flowWithLifecycle(lifecycleOwner.lifecycle)
            .collect { info ->
                displayMode = when {
                    info.displayFeatures.any { it is FoldingFeature && it.state == FoldingFeature.State.HALF_OPENED } -> DisplayModes.FLEX
                    // 将来: カバーディスプレイ用条件
                    else -> DisplayModes.NORMAL
                }
            }
    }

    return displayMode
}