package com.example.bibliotheque.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public final class DateUtils {

    private static final String DEFAULT_PATTERN = "dd/MM/yyyy";

    private DateUtils() {
    }

    public static String formatDate(long timestamp) {
        if (timestamp <= 0L) {
            return "-";
        }
        return new SimpleDateFormat(DEFAULT_PATTERN, Locale.getDefault())
                .format(new Date(timestamp));
    }

    public static long addDays(long timestamp, int days) {
        return timestamp + TimeUnit.DAYS.toMillis(days);
    }

    public static long getRetardJours(long dateRetourPrevue, long dateReference) {
        if (dateRetourPrevue <= 0L || dateReference <= dateRetourPrevue) {
            return 0L;
        }

        long difference = dateReference - dateRetourPrevue;
        long jourMillis = TimeUnit.DAYS.toMillis(1);
        return Math.max(1L, (difference + jourMillis - 1L) / jourMillis);
    }
}
