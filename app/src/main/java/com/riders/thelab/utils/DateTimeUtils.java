package com.riders.thelab.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import timber.log.Timber;

public class DateTimeUtils {

    private DateTimeUtils() {
    }

    public static String formatMillisToTimeHoursMinutes(long millis) {
        long time = millis * (long) 1000;
        Date date = new Date(time);
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        format.setTimeZone(TimeZone.getTimeZone("UTC+1"));

        return format.format(date);
    }
}