package com.example.djtimer.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerDropdown(
    label: String,
    selectedTime: Pair<Int, Int>?, // (hour, minute)
    onTimeSelected: (Int, Int) -> Unit
) {
    val timeOptions = remember {
        val hours = 0..23
        val minutes = listOf(0, 10, 20, 30, 40, 50)
        hours.flatMap { h -> minutes.map { m -> h to m } }
    }

    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        TextField(
            value = selectedTime?.let { "%02d:%02d".format(it.first, it.second) } ?: "",
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
            modifier = Modifier.menuAnchor().width(160.dp)
        )

        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            timeOptions.forEach { (hour, minute) ->
                DropdownMenuItem(
                    text = { Text("%02d:%02d".format(hour, minute)) },
                    onClick = {
                        onTimeSelected(hour, minute)
                        expanded = false
                    }
                )
            }
        }
    }
}
