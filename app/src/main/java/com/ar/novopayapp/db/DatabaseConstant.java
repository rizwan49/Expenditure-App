package com.ar.novopayapp.db;

public interface DatabaseConstant {
    String DATABASE_NAME = "novo_db";
    int DB_VERSION = 1;

    interface Tables {
        String SMS = "sms";
    }

    interface Fields {
        interface Sms {
            String ID = "_id";
            String ADDRESS = "_address";
            String DEBITED = "_debited";
            String READ_STATE = "_readState";
            String TIME_STAMP = "_timeStamp";
            String DATE = "_date";
            String MONTH = "_month";
        }
    }
}
