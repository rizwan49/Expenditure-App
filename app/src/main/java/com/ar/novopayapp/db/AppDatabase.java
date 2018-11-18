package com.ar.novopayapp.db;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {Sms.class}, version = DatabaseConstant.DB_VERSION, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract DaoSms daoSms();
}
