package com.ar.novopayapp.Utils;

import android.net.Uri;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Util {
    public static final DateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("dd MMM yyyy");
    public static final DateFormat SIMPLE_MONTH_FORMAT = new SimpleDateFormat("MMM");

    public static Uri getSmsUri() {
        return Uri.parse(AppConstants.SMS_URI);
    }

    public static String getDatePrefix(long dateTime) {
        return SIMPLE_DATE_FORMAT.format(dateTime);
    }

    public static String getDateInMonth(long timeStamp) {
        return SIMPLE_MONTH_FORMAT.format(timeStamp);

    }
}
