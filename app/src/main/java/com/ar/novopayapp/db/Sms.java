package com.ar.novopayapp.db;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Sms {
    @PrimaryKey
    @ColumnInfo(name = DatabaseConstant.Fields.Sms.ID)
    private int _id;

    @Ignore
    @ColumnInfo(name = DatabaseConstant.Fields.Sms.ADDRESS)
    private String _address;

    @ColumnInfo(name = DatabaseConstant.Fields.Sms.DEBITED)
    private String _debited;

    @Ignore
    @ColumnInfo(name = DatabaseConstant.Fields.Sms.READ_STATE)
    private String _readState;

    @ColumnInfo(name = DatabaseConstant.Fields.Sms.TIME_STAMP)
    private long _timeStamp;

    @ColumnInfo(name = DatabaseConstant.Fields.Sms.DATE)
    private String _date;

    @ColumnInfo(name = DatabaseConstant.Fields.Sms.MONTH)
    private String _month;

    public int getId() {
        return _id;
    }

    public String getAddress() {
        return _address;
    }

    public String getDebited() {
        return _debited;
    }

    public String getReadState() {
        return _readState;
    }



    public void setId(int id) {
        _id = id;
    }

    public void setAddress(String address) {
        _address = address;
    }

    public void setDebited(String debitedAmount) {
        _debited = debitedAmount;
    }

    public void setReadState(String readState) {
        _readState = readState;
    }

    public long getTimeStamp() {
        return _timeStamp;
    }

    public void setTimeStamp(long _timeStamp) {
        this._timeStamp = _timeStamp;
    }

    public String getDate() {
        return _date;
    }

    public void setDate(String _date) {
        this._date = _date;
    }

    public String getMonth() {
        return _month;
    }

    public void setMonth(String _month) {
        this._month = _month;
    }

    @Override
    public String toString() {
        return "Sms{" +
                "_mid=" + _id +
                ", _address='" + _address + '\'' +
                ", _debited='" + _debited + '\'' +
                ", _readState='" + _readState + '\'' +
                ", _timeStamp=" + _timeStamp +
                ", _date='" + _date + '\'' +
                ", _month='" + _month + '\'' +
                '}';
    }
}