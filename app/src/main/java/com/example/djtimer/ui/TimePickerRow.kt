package com.example.djtimer.ui

import android.app.TimePickerDialog
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

    Column( horizontalAlignment = Alignment.Start,) {
        Text(
            text = label,
            color = Color(0xFFFFD700))
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = { showDialog = true },
            enabled = enabled,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFFD700),  // 背景色
                contentColor = Color.Blue       // 文字色
            ),
            shape = RoundedCornerShape(0.dp),  // 四角
            modifier = Modifier
                .width(300.dp)   // 横幅を広げる
                .height(60.dp)
        ) {
            Text(timeStr,
                fontSize = 35.sp)
        }
    }
}