package com.example.djtimer.viewModel

import androidx.lifecycle.ViewModel
import com.example.djtimer.model.InputMode
import com.example.djtimer.model.TimerState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class DJTimerViewModel @Inject constructor() : ViewModel() {
    val timerState = MutableStateFlow<TimerState>(TimerState.BeforeStart)
    val timeRemainingText = MutableStateFlow("")


    val startTime = MutableStateFlow<LocalTime?>(null)
    val endTime = MutableStateFlow<LocalTime?>(null)
    val playTime = MutableStateFlow("")
    val inputMode = MutableStateFlow(InputMode.None)

    fun updateStartTime(time: LocalTime) {
        startTime.value = time
        inputMode.value = InputMode.StartEnd
        playTime.value = "" // 相互排他
    }

    fun updateEndTime(time: LocalTime) {
        endTime.value = time
        inputMode.value = InputMode.StartEnd
        playTime.value = ""
    }

    fun updatePlayTime(value: String) {
        playTime.value = value
        if (value.isNotBlank()) {
            startTime.value = null
            endTime.value = null
            inputMode.value = InputMode.PlayTime
        }
    }

    fun reset() {
        startTime.value = null
        endTime.value = null
        playTime.value = ""
        inputMode.value = InputMode.None
    }

    fun canStartTimer(): Boolean {
        return (inputMode.value == InputMode.PlayTime && playTime.value.toIntOrNull() != null)
                || (inputMode.value == InputMode.StartEnd && startTime.value != null && endTime.value != null)
    }


    fun setStartEnd(start: LocalTime, end: LocalTime) {  }
    fun setPlayTime(playTime: Long) {  }

    fun startTimer() {  } // CountDownTimer or coroutineで処理
    fun stopTimer() {  }
}