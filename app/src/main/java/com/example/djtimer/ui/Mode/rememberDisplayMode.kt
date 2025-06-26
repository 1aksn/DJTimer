package com.example.djtimer.ui.Mode

import android.app.Activity
import android.util.Log
import androidx.compose.material3.DisplayMode
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import androidx.window.area.WindowAreaCapability
import androidx.window.area.WindowAreaController
import androidx.window.layout.FoldingFeature
import androidx.window.layout.WindowInfoTracker
import com.example.djtimer.model.DisplayModes
import kotlinx.coroutines.flow.first

@Composable
fun rememberDisplayMode(): DisplayModes {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val windowInfoTracker = remember { WindowInfoTracker.getOrCreate(context) }

    var displayMode by remember { mutableStateOf(DisplayModes.NORMAL) }

    val isLikelyCoverDisplay = configuration.screenWidthDp < 390 || configuration.screenHeightDp < 500

    LaunchedEffect(Unit) {
        Log.v("ろぐ", "w = "+configuration.screenWidthDp+" h = "+configuration.screenHeightDp)
        if (isLikelyCoverDisplay) {
            displayMode = DisplayModes.COVER_DISPLAY
            return@LaunchedEffect
        }

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