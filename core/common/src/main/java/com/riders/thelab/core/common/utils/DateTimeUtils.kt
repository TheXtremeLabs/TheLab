package com.riders.thelab.core.common.utils

import android.annotation.SuppressLint
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

object DateTimeUtils {

    private const val UTC = "UTC"
    private const val GMT = "GMT"

    @Suppress("ConstPropertyName")
    private const val HH_mm = "HH:mm"

    @Suppress("ConstPropertyName")
    private const val dd_MM = "dd/MM"

    @SuppressLint("SimpleDateFormat")
    fun formatMillisToTimeHoursMinutes(millis: Long): String {
        val time = millis * 1000.toLong()
        val date = Date(time)
        val format = SimpleDateFormat(HH_mm)
        format.timeZone = TimeZone.getTimeZone(
            buildUTCTimeZone(getOffsetTimeZone(millis))
        )
        return format.format(date)
    }

    @SuppressLint("SimpleDateFormat")
    fun formatMillisToTimeHoursMinutes(timeZoneId: String, millis: Long): String {
        val time = millis * 1000.toLong()
        val date = Date(time)
        val format = SimpleDateFormat(HH_mm)
        format.timeZone = TimeZone.getTimeZone(timeZoneId)
        return format.format(date)
    }

    @SuppressLint("SimpleDateFormat")
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

    fun getDayFromTime(dateTimeUTC: Long): String {
        Timber.d("getDayFromTime() | $dateTimeUTC")

        return Calendar.getInstance(Locale.getDefault())
            .apply {
                /*
                 * source : https://stackoverflow.com/questions/64125378/why-does-calendar-getdisplayname-always-return-the-same-day-of-week-when-given-m
                 */
                time = Date(dateTimeUTC * 1000)
            }
            .run {
                this.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault())
            }!!
    }
}