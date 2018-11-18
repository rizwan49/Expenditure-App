package com.ar.novopayapp.db;

import android.app.IntentService;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.util.Log;

import com.ar.novopayapp.MyApplication;
import com.ar.novopayapp.Utils.AppConstants;
import com.ar.novopayapp.Utils.EventBusAction;
import com.ar.novopayapp.Utils.Util;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DbService extends IntentService {

    private static final String TAG = DbService.class.getName();


    public DbService() {
        super("DbService");
    }

    public DbService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        fetchAndLoadIntoDb();
        EventBus.getDefault().post(EventBusAction.SYNC_SUCCESS);
    }

    public static void fetchAndLoadIntoDb() {
        AppDatabase mDb = DatabaseHelper.getInstance();
        Sms lastRecord = mDb.daoSms().getLastRecord();

        Uri mSmsQueryUri = Util.getSmsUri();
        List<Sms> smsList = new ArrayList<>();

        Cursor cursor = null;
        try {
            String _DATE = "date";
            if (lastRecord == null) {
                cursor = MyApplication.getContext().getContentResolver().query(mSmsQueryUri, null,
                        null, null, null);
            } else
                cursor = MyApplication.getContext().getContentResolver().query(mSmsQueryUri, null,
                        _DATE + " > ? ", new String[]{String.valueOf(lastRecord.getTimeStamp())}, null);

            if (cursor == null) {
                Log.d(TAG, "cursor is null. uri: " + mSmsQueryUri);
                return;
            }
            int i = 0;

            for (boolean hasData = cursor.moveToFirst(); hasData; hasData = cursor.moveToNext()) {
                final int id = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
                final String body = cursor.getString(cursor.getColumnIndexOrThrow("body")).toString();
                final String sender_no = cursor.getString(cursor.getColumnIndexOrThrow("address")).toString();
                final String date = cursor.getString(cursor.getColumnIndexOrThrow("date"));
                final String readState = cursor.getString(cursor.getColumnIndexOrThrow("read"));

                if (body.contains(AppConstants.DEBITED_BY) || body.contains(AppConstants.DEBITED_FOR)) {
                    //sms.add(body);
                    Pattern p = Pattern.compile(AppConstants.REG_EXPR_FOR_AMOUNT);
                    Matcher mr = p.matcher(body);
                    if (mr.find()) {
                        Sms smsObj = new Sms();
                        smsObj.setTimeStamp(Long.parseLong(date));
                        smsObj.setAddress(sender_no);
                        smsObj.setDebited(mr.group(2));
                        smsObj.setId(id);
                        smsObj.setReadState(readState);
                        smsObj.setDate(Util.getDatePrefix(Long.parseLong(date)));
                        smsObj.setMonth(Util.getDateInMonth(smsObj.getTimeStamp()));

                        i++;
                        System.out.println(mr.group(1) + " :: " + mr.group(2));
                        mDb.daoSms().insertAll(smsObj);
                        //smsList.add(smsObj);
                    }

                }
            }
            System.out.println("Total items are:" + i + " And size:" + smsList.size());
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        } finally {
            cursor.close();
        }

    }
}
