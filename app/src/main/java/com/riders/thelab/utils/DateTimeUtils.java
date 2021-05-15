package com.riders.thelab.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import timber.log.Timber;

public class DateTimeUtils {

    private static final String UTC = "UTC";
    private static final String GMT = "GMT";

    private DateTimeUtils() {
    }

    public static String formatMillisToTimeHoursMinutes(long millis) {
        Timber.e("mills : %d", millis);

        long time = millis * (long) 1000;
        Date date = new Date(time);
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        format.setTimeZone(
                TimeZone.getTimeZone(
                        buildUTCTimeZone(getOffsetTimeZone(millis))
                )
        );

        return format.format(date);
    }

    public static String formatMillisToTimeDayMonth(long millis) {
        Timber.e("mills : %d", millis);

        long time = millis * (long) 1000;
        Date date = new Date(time);
        SimpleDateFormat format = new SimpleDateFormat("dd/MM");
        /*format.setTimeZone(
                TimeZone.getTimeZone(
                        buildUTCTimeZone(getOffsetTimeZone(millis))
                )
        );*/

        return format.format(date);
    }

    private static int getOffsetTimeZone(long millis) {
        return (int) ((millis / 60) / 60);
    }

    private static String buildUTCTimeZone(int offset) {
        return offset > 0
                ? UTC + "+" + offset // Don't contains "+" sign
                : UTC + offset; // Already contains "-" sign
    }

    private void compareUTC(long currentOffsetMillis, long targetOffsetMillis) {


        // Step 1 : get current UTC offset
        int currentUTC = getOffsetTimeZone(TimeZone.getDefault().getRawOffset() / 1000);

        // Step 2 : calculate mills target offset
        int targetUTC = getOffsetTimeZone(targetOffsetMillis);

        // Step 3 : compare target offset with current offset,
        // this  will determine final sunrise/sunset times according to user's utc offset
        if (targetUTC < currentUTC) {
            Timber.d("target offset is lower than current, calculated final offset should be currentUTC - targetUTC");
        } else if (targetUTC > currentUTC) {
            Timber.d("target offset is greater than current, calculated final offset should be currentUTC + targetUTC");

        } else if (targetUTC == currentUTC) {
            Timber.d("target offset is equal to current, calculated final offset should be currentUTC = targetUTC");
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