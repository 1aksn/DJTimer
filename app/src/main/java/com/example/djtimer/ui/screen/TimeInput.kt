package com.example.djtimer.ui.screen

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun TimeInputs(
    startTime: Pair<Int, Int>?,
    endTime: Pair<Int, Int>?,
    onStart: (Int, Int, Int, Int) -> Unit,
    onChange: (Pair<Int, Int>) -> Unit
) {
    var start by remember { mutableStateOf(startTime ?: 0 to 0) }
    var end by remember { mutableStateOf(endTime ?: 0 to 0) }

    Text("Start Time")
    TimePickerDropdown("Start", selectedTime = start) { h, m -> start = h to m }

    Text("End Time")
    TimePickerDropdown("End", selectedTime = end) { h, m -> end = h to m }

    Button(
        onClick = {
            onStart(start.first, start.second, end.first, end.second)
            onChange(start)
        },
        enabled = true
    ) {
        Text("Start Timer")
    }
}