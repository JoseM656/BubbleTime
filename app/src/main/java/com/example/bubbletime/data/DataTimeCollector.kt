package com.example.bubbletime.data

import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.Duration

object DateTimeCollector {

    fun getLocalTime(region: String): String {
        val zone = ZoneId.of(region)
        val time = ZonedDateTime.now(zone)
        return time.toLocalTime().toString().substring(0, 5)
    }

    fun getTimeDifference(region1: String, region2: String): Long {
        val t1 = ZonedDateTime.now(ZoneId.of(region1))
        val t2 = ZonedDateTime.now(ZoneId.of(region2))
        return Duration.between(t1, t2).toHours()
    }
}
