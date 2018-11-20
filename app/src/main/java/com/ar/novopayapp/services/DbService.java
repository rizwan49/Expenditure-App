package com.ar.novopayapp.services;

import android.app.IntentService;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import com.ar.novopayapp.MyApplication;
import com.ar.novopayapp.utils.AppConstants;
import com.ar.novopayapp.utils.EventBusAction;
import com.ar.novopayapp.utils.Util;
import com.ar.novopayapp.db.AppDatabase;
import com.ar.novopayapp.db.DatabaseHelper;
import com.ar.novopayapp.db.Sms;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/***
 * steps :
 * 1. first fetch lastTimeStamp from existing table
 * 2. if return null
 *    a) 1st timeStamp into local db then passing null and fetching all sms
 *    b) filter all the sms based on requirement
 *    c) insert it into local db using RoomPersistence
 *
 *
 * 3. if return lastTimeStamp
 *    a) using timeStamp to get newly added sms
 *    b) doing same steps from 2(a) to 2(c);
 *
 * 4. after processing sending event using EventBus to MainActivity to load data;
 */
public class DbService extends IntentService {

    private static final String TAG = DbService.class.getName();
    private static final String ID = "_id";
    private static final String BODY = "body";
    private static final String ADDRESS = "address";
    private static final String DATE = "date";
    private static final String READ = "read";

    public DbService() {
        super("DbService");
    }

    public DbService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        fetchAndLoadIntoDb();
        EventBus.getDefault().postSticky(EventBusAction.SYNC_SUCCESS);
    }

    public static void fetchAndLoadIntoDb() {
        AppDatabase mDb = DatabaseHelper.getInstance();
        Sms lastRecord = mDb.daoSms().getLastRecord();

        Uri mSmsQueryUri = Util.getSmsUri();
        List<Sms> smsList = new ArrayList<>();

        Cursor cursor = null;
        try {

            if (lastRecord == null) {
                cursor = MyApplication.getContext().getContentResolver().query(mSmsQueryUri, null,
                        null, null, DATE);
            } else
                cursor = MyApplication.getContext().getContentResolver().query(mSmsQueryUri, null,
                        DATE + " > ? ", new String[]{String.valueOf(lastRecord.getTimeStamp())}, DATE);

            if (cursor == null) {
                Log.d(TAG, "cursor is null. uri: " + mSmsQueryUri);
                return;
            }
            int i = 0;
            int progress = 0;
            int max = cursor.getCount();
            int percentage;
            for (boolean hasData = cursor.moveToFirst(); hasData; hasData = cursor.moveToNext()) {
                percentage = (++progress * 100) / max;
                EventBus.getDefault().postSticky(percentage + "");
                final int id = cursor.getInt(cursor.getColumnIndexOrThrow(ID));
                final String body = cursor.getString(cursor.getColumnIndexOrThrow(BODY));
                final String sender_no = cursor.getString(cursor.getColumnIndexOrThrow(ADDRESS));
                final String date = cursor.getString(cursor.getColumnIndexOrThrow(DATE));
                final String readState = cursor.getString(cursor.getColumnIndexOrThrow(READ));

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
            if (cursor != null)
                cursor.close();
        }

    }
}
