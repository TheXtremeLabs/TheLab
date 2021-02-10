package com.riders.thelab.utils;

import android.annotation.SuppressLint;

import com.riders.thelab.core.utils.LabCompatibilityManager;

import org.threeten.bp.LocalTime;
import org.threeten.bp.format.DateTimeFormatter;

import java.time.Instant;
import java.time.ZoneId;
import java.util.Locale;

public class DateTimeUtils {

    private DateTimeUtils() {
    }


    @SuppressLint({"SimpleDateFormat", "NewApi"})
    public static String formatMillisToTimeHoursMinutes(long millis) {

        String formattedDate = null;

        if (LabCompatibilityManager.isOreo()) {
            formattedDate =
                    Instant
                            .ofEpochSecond(millis)
                            .atZone(ZoneId.of("UTC"))
                            .format(java.time.format.DateTimeFormatter.ofPattern("hh:mm"));
//                    .toLocalTime()
//                    .toString();//this will convert it to your system's date time
        } else {
            LocalTime datetime = LocalTime.ofNanoOfDay(1000 * millis);
            formattedDate =
                    datetime.format(
                            DateTimeFormatter
                                    .ofPattern("hh:mm", Locale.getDefault())
                    );
        }

        return formattedDate;

        /*Date date = new Date(millis);
        DateFormat formatter = new SimpleDateFormat("HH:mm");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        return formatter.format(date);*/
    }
}
