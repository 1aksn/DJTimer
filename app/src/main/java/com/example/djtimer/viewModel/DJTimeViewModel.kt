package com.example.djtimer.viewModel

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.util.TimeUtils.formatDuration
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.djtimer.model.InputMode
import com.example.djtimer.model.TimerState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.LocalDateTime
import java.time.LocalTime
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.S)
@HiltViewModel
class DJTimerViewModel @Inject constructor() : ViewModel() {
    private val _timerState = MutableStateFlow<TimerState>(TimerState.BeforeStart)
    val timerState = _timerState.asStateFlow()
    private val _timeRemainingText = MutableStateFlow("")
    val timeRemainingText = _timeRemainingText.asStateFlow()

    val totalTimeText = MutableStateFlow("")

    val startTime = MutableStateFlow<LocalTime?>(null)
    val endTime = MutableStateFlow<LocalTime?>(null)
    val playTime = MutableStateFlow("")
    val inputMode = MutableStateFlow(InputMode.None)

    private var countdownJob: Job? = null
    //タイマー開始後の終了時刻
    private var endTimeReference: LocalDateTime? = null

    //STOP時のタイマー保存
    private var pausedRemainingDuration: Duration? = null

    private val _canGo = MutableStateFlow(false)
    val canGo: StateFlow<Boolean> = _canGo

    private var _initialDurationSeconds: Int? = null

    val remainingDurationSeconds: Int
        get() = endTimeReference?.let {
            val now = LocalDateTime.now()
            Duration.between(now, it).seconds.toInt().coerceAtLeast(0)
        } ?: 0

    val totalDurationSeconds: Int?
        get() = _initialDurationSeconds

    init {
        viewModelScope.launch {
            combine(startTime, endTime, playTime, inputMode) { start, end, play, mode ->
                (mode == InputMode.PlayTime && play.toIntOrNull() != null)
                        || (mode == InputMode.StartEnd && start != null && end != null)
            }.collect {
                _canGo.value = it
            }
        }
    }

    fun updateStartTime(time: LocalTime) {
        Log.v("ろぐ", "updateStartTime time = $time")

        startTime.value = time
        inputMode.value = InputMode.StartEnd
        playTime.value = "" // 相互排他
        updateTotalTimeDisplay()
    }

    fun updateEndTime(time: LocalTime) {
        Log.v("ろぐ", "updateEndTime time = $time")

        endTime.value = time
        inputMode.value = InputMode.StartEnd
        playTime.value = ""
        updateTotalTimeDisplay()
    }

    fun updatePlayTime(value: String) {
        playTime.value = value
        if (value.isNotBlank()) {
            Log.v("ろぐ", "updatePlayTime time = $value")
            startTime.value = null
            endTime.value = null
            inputMode.value = InputMode.PlayTime
            updateTotalTimeDisplay()
        }
    }

    private fun updateTotalTimeDisplay() {
        totalTimeText.value = when (inputMode.value) {
            InputMode.PlayTime -> {
                playTime.value.toIntOrNull()?.let { "$it min" } ?: ""
            }
            InputMode.StartEnd -> {
                if (startTime.value != null && endTime.value != null) {
                    val duration = Duration.between(startTime.value, endTime.value)
                    "${duration.toMinutes()} min"
                } else ""
            }
            else -> ""
        }
    }

    fun reset() {
        countdownJob?.cancel()
        _timerState.value = TimerState.BeforeStart
        _timeRemainingText.value = ""
        startTime.value = null
        endTime.value = null
        playTime.value = ""
        inputMode.value = InputMode.None
        endTimeReference = null
        totalTimeText.value = ""
        _initialDurationSeconds = null
    }

    fun canStartTimer(): Boolean {
        return (inputMode.value == InputMode.PlayTime && playTime.value.toIntOrNull() != null)
                || (inputMode.value == InputMode.StartEnd && startTime.value != null && endTime.value != null)
    }

    fun startTimer() {
        if (_timerState.value == TimerState.InProgress) return
        countdownJob?.cancel()
        val now = LocalDateTime.now()

        val duration = when {
            pausedRemainingDuration != null -> {
                pausedRemainingDuration.also { pausedRemainingDuration = null } ?: return
            }

            inputMode.value == InputMode.PlayTime -> {
                val minutes = playTime.value.toLongOrNull() ?: return
                Duration.ofMinutes(minutes)
            }
            inputMode.value == InputMode.StartEnd -> {
                val start = startTime.value ?: return
                val end = endTime.value ?: return
                val todayStart = now.withHour(start.hour).withMinute(start.minute).withSecond(0)
                val todayEnd = now.withHour(end.hour).withMinute(end.minute).withSecond(0)

                when {
                    now.isBefore(todayStart) -> {
                        viewModelScope.launch {
                            _timerState.value = TimerState.BeforeStart
                            updateTimeLoop(Duration.between(now, todayStart))
                            startTimer() // 自動で本番開始へ
                        }
                        return
                    }
                    now.isBefore(todayEnd) -> {
                        Duration.between(now, todayEnd)
                    }
                    else -> return
                }
            }
            else -> return
        }

        endTimeReference = now.plus(duration)
        if (_initialDurationSeconds == null) {
            _initialDurationSeconds = duration.seconds.toInt()
        }
        _timerState.value = TimerState.InProgress
        countdownJob?.cancel()
        countdownJob = viewModelScope.launch {
            updateTimeLoop(duration)
        }
    }

    fun stopTimer() {
        countdownJob?.cancel()
        pausedRemainingDuration = endTimeReference?.let { Duration.between(LocalDateTime.now(), it) }
        _timerState.value = TimerState.BeforeStart
    }

    // アプリ再起動や画面復帰時に残り時間を再計算して再開
    fun resumeTimerIfNeeded() {
        val endRef = endTimeReference ?: return
        val now = LocalDateTime.now()
        val remaining = Duration.between(now, endRef)
        if (remaining.isNegative || remaining.isZero) {
            _timerState.value = TimerState.Done
            _timeRemainingText.value = "DONE"
        } else {
            _timerState.value = TimerState.InProgress
            countdownJob?.cancel()
            countdownJob = viewModelScope.launch {
                updateTimeLoop(remaining)
            }
        }
    }

    private suspend fun updateTimeLoop(duration: Duration) {
        var remaining = duration
        while (!remaining.isZero && !remaining.isNegative) {
            _timeRemainingText.value = formatDuration(remaining)
            delay(1000)
            remaining = Duration.between(LocalDateTime.now(), endTimeReference ?: return)
        }
        _timerState.value = TimerState.Done
        _timeRemainingText.value = "DONE"
    }

    private fun formatDuration(duration: Duration): String {
        val minutes = duration.toMinutesPart()
        val seconds = duration.toSecondsPart()
        return String.format("%02d:%02d", minutes, seconds)
    }
}