package com.example.djtimer.ui

import android.app.TimePickerDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import java.time.LocalTime

@Composable
fun TimePickerRow(
    label: String,
    time: LocalTime?,
    enabled: Boolean,
    onTimeSelected: (LocalTime) -> Unit
) {
    val context = LocalContext.current
    val timeStr = time?.let { "%02d:%02d".format(it.hour, it.minute) } ?: "--:--"

    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        TimePickerDialog(
            context,
            { _, hour: Int, minute: Int ->
                onTimeSelected(LocalTime.of(hour, minute))
                showDialog = false
            },
            time?.hour ?: LocalTime.now().hour,
            time?.minute ?: LocalTime.now().minute,
            true // ← 24時間表示
        ).show()
        showDialog = false // ← 表示直後に再リセット
    }

    Button(
        onClick = { showDialog = true },
        enabled = enabled
    ) {
        Text("$label: $timeStr")
    }
}