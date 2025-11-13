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
        val time1 = ZonedDateTime.now(ZoneId.of(region1)).toLocalTime()
        val time2 = ZonedDateTime.now(ZoneId.of(region2)).toLocalTime()

        var diff = Duration.between(time1, time2).toHours()

        if (diff > 12) diff -= 24
        if (diff < -12) diff += 24

        return diff
    }

}
