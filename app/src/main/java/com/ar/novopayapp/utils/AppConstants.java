package com.ar.novopayapp.utils;

public interface AppConstants {
    String SMS_URI = "content://sms/inbox";
    String DEBITED_BY = "debited by";
    String DEBITED_FOR = "debited for";
    String REG_EXPR_FOR_AMOUNT = "(\\d+)\\D+(\\d+(?:.\\d+)?)";

    int REQUEST_READ_SMS_PERMISSION = 3004;
}
