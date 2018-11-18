package com.ar.novopayapp.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface DaoSms {
    String SELECT_PREFIX = "SELECT * FROM ";

    @Query(SELECT_PREFIX + DatabaseConstant.Tables.SMS + " ORDER BY " + DatabaseConstant.Fields.Sms.TIME_STAMP + " DESC")
    List<Sms> getAll();

    @Query("SELECT " + DatabaseConstant.Fields.Sms.DATE +
            " ," + DatabaseConstant.Fields.Sms.TIME_STAMP +
            " ," + DatabaseConstant.Fields.Sms.MONTH +
            " ," + DatabaseConstant.Fields.Sms.ID +
            " ," + " SUM(" + DatabaseConstant.Fields.Sms.DEBITED + ") AS " + DatabaseConstant.Fields.Sms.DEBITED + " FROM " + DatabaseConstant.Tables.SMS +
            " GROUP BY " + DatabaseConstant.Fields.Sms.DATE +
            " ORDER BY " + DatabaseConstant.Fields.Sms.TIME_STAMP )
    List<Sms> getGroupedList();

    @Query(" SELECT * FROM " + DatabaseConstant.Tables.SMS + " ORDER BY " + DatabaseConstant.Fields.Sms.TIME_STAMP + " DESC Limit 1")
    Sms getLastRecord();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Sms... sms);

    @Delete
    void delete(Sms sms);
}
