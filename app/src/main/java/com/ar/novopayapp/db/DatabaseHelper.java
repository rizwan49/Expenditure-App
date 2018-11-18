package com.ar.novopayapp.db;

import android.arch.persistence.room.Room;
import android.content.Intent;

import com.ar.novopayapp.MyApplication;


public class DatabaseHelper {

    private static AppDatabase db;
    private final static String TAG = DatabaseHelper.class.getName();

    private DatabaseHelper() {
    }

    public static AppDatabase getInstance() {
        if (db == null) {
            // To make thread safe
            synchronized (DatabaseHelper.class) {
                // check again as multiple threads
                if (db == null) {
                    db = Room.databaseBuilder(MyApplication.getContext(),
                            AppDatabase.class, DatabaseConstant.DATABASE_NAME).build();

                }
            }
        }
        return db;
    }

    public static void startServiceForSms(){
        Intent intent = new Intent(MyApplication.getContext(),DbService.class);
        MyApplication.getContext().startService(intent);
    }

}
