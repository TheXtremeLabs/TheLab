package com.riders.thelab.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
object DateTimeUtils {

    private const val UTC = "UTC"
    private const val GMT = "GMT"
    private const val HH_mm = "HH:mm"
    private const val dd_MM = "dd/MM"

    fun formatMillisToTimeHoursMinutes(millis: Long): String {
        val time = millis * 1000.toLong()
        val date = Date(time)
        val format = SimpleDateFormat(HH_mm)
        format.timeZone = TimeZone.getTimeZone(
            buildUTCTimeZone(getOffsetTimeZone(millis))
        )
        return format.format(date)
    }

    fun formatMillisToTimeHoursMinutes(timeZoneId: String, millis: Long): String {
        val time = millis * 1000.toLong()
        val date = Date(time)
        val format = SimpleDateFormat(HH_mm)
        format.timeZone = TimeZone.getTimeZone(timeZoneId)
        return format.format(date)
    }

    fun formatMillisToTimeDayMonth(millis: Long): String {
        return SimpleDateFormat(dd_MM).format(Date(millis * 1000.toLong()))
    }

    private fun getOffsetTimeZone(millis: Long): Int {
        return (millis / 60 / 60).toInt()
    }

    private fun buildUTCTimeZone(offset: Int): String {
        return if (offset > 0) "$UTC+$offset" // Don't contains "+" sign
        else UTC + offset // Already contains "-" sign
    }
}