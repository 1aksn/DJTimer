package com.example.djtimer.ui.screen

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.example.djtimer.model.DisplayModes
import com.example.djtimer.ui.Mode.rememberDisplayMode

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun InputScreen(onStart: (startHour: Int, startMinute: Int, endHour: Int, endMinute: Int) -> Unit) {
    var startTime by remember { mutableStateOf<Pair<Int, Int>?>(null) }
    var endTime by remember { mutableStateOf<Pair<Int, Int>?>(null) }

    val displayMode = rememberDisplayMode()

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {

        val density = LocalDensity.current

        val parentHeight = with(density) { constraints.maxHeight.toDp() }

        // FLEXのときは画面の高さの1/4だけ下にずらす（上→中央 → 下中央）
        val targetOffsetY = if (displayMode == DisplayModes.FLEX) parentHeight / 4 else 0.dp

        val offsetY by animateDpAsState(
            targetValue = targetOffsetY,
            animationSpec = tween(durationMillis = 400),
            label = "offsetY"
        )

        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .offset(y = offsetY),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Start Time")
            TimePickerDropdown("Start", selectedTime = startTime) { h, m -> startTime = h to m }

            Text("End Time")
            TimePickerDropdown("End", selectedTime = endTime) { h, m -> endTime = h to m }

            Button(
                onClick = {
                    onStart(startTime!!.first, startTime!!.second, endTime!!.first, endTime!!.second)
                }
            ) {
                Text("Start Timer")
            }
        }
    }
}