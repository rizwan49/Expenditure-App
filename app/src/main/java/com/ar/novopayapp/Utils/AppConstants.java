package com.ar.novopayapp.Utils;

public interface AppConstants {
    String SMS_URI = "content://sms/inbox";
    String DEBITED_BY = "debited by";
    String DEBITED_FOR = "debited for";
    String REG_EXPR_FOR_AMOUNT = "(\\d+)\\D+(\\d+(?:.\\d+)?)";
}
