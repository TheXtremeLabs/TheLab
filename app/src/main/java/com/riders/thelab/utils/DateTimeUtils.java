package com.riders.thelab.utils;

import android.annotation.SuppressLint;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DateTimeUtils {

    private DateTimeUtils() {
    }


    @SuppressLint("SimpleDateFormat")
    public static String formatMillisToTimeHoursMinutes(long millis) {
        Date date = new Date(millis);
        DateFormat formatter = new SimpleDateFormat("HH:mm");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        return formatter.format(date);
    }
}
