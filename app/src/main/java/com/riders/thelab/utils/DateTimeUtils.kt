package com.riders.thelab.utils

import android.annotation.SuppressLint
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
class DateTimeUtils {

    companion object {

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

        private fun compareUTC(currentOffsetMillis: Long, targetOffsetMillis: Long) {


            // Step 1 : get current UTC offset
            val currentUTC = getOffsetTimeZone((TimeZone.getDefault().rawOffset / 1000).toLong())

            // Step 2 : calculate mills target offset
            val targetUTC = getOffsetTimeZone(targetOffsetMillis)

            // Step 3 : compare target offset with current offset,
            // this  will determine final sunrise/sunset times according to user's utc offset
            if (targetUTC < currentUTC) {
                Timber.d("target offset is lower than current, calculated final offset should be currentUTC - targetUTC")
            } else if (targetUTC > currentUTC) {
                Timber.d("target offset is greater than current, calculated final offset should be currentUTC + targetUTC")
            } else if (targetUTC == currentUTC) {
                Timber.d("target offset is equal to current, calculated final offset should be currentUTC = targetUTC")
            }

            // if target offset is lower than user's utc offset -> target offset minus user's offset
            // e.g. :
            //  - Los Angeles = UTC -8 (18h) --> UTC (0) = (3h)
            //  - Paris = UTC +1 (17h)  --> UTC (0) = (16h)
            //
            // Calculate Los Angeles UTC(-8) with Paris base UTC (+1),
            // result should be a 9 hours gap between these two
        }
    }
}