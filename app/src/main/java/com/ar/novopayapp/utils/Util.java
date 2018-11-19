package com.ar.novopayapp.utils;

import android.net.Uri;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class Util {
    private static final DateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("dd MMM yyyy");
    private static final DateFormat SIMPLE_MONTH_FORMAT = new SimpleDateFormat("MMM");
    private static final DateFormat SIMPLE_DAY_FORMAT = new SimpleDateFormat("dd");
    private static final DateFormat SIMPLE_YEAR_FORMAT = new SimpleDateFormat("yyyy");


    public static Uri getSmsUri() {
        return Uri.parse(AppConstants.SMS_URI);
    }

    public static String getDatePrefix(long timeStamp) {
        return SIMPLE_DATE_FORMAT.format(timeStamp);
    }

    public static String getDateInMonth(long timeStamp) {
        return SIMPLE_MONTH_FORMAT.format(timeStamp);

    }

    public static String getDayFromTimeStamp(long timeStamp) {
        return SIMPLE_DAY_FORMAT.format(timeStamp);
    }

    public static String getYearFromTimeStamp(long timeStamp) {
        return SIMPLE_YEAR_FORMAT.format(timeStamp);
    }
}
