package com.example.djtimer.util

import java.time.Clock
import java.time.Instant
import java.time.ZoneId

class MutableClock(
    private var _instant: Instant,
    private val _zone: ZoneId = ZoneId.systemDefault()
) : Clock() {

    override fun getZone(): ZoneId = _zone
    override fun withZone(zone: ZoneId): Clock = MutableClock(_instant, zone)
    override fun instant(): Instant = _instant

    fun set(instant: Instant) { _instant = instant }
    fun plusSeconds(sec: Long) { _instant = _instant.plusSeconds(sec) }
    fun plusMillis(ms: Long) { _instant = _instant.plusMillis(ms) }
}