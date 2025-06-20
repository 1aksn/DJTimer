package com.example.djtimer.util

import java.time.Duration
import java.time.LocalTime

fun getRemainingTime(start: LocalTime, end: LocalTime): String {
    val now = LocalTime.now()
    val duration = Duration.between(now, end)
    val minutes = duration.toMinutes()
    val seconds = duration.minusMinutes(minutes).seconds
    return "${minutes}:${seconds}"
}