package com.ar.novopayapp;

import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.ar.novopayapp.db.AppDatabase;
import com.ar.novopayapp.db.DatabaseHelper;
import com.ar.novopayapp.db.Sms;

import java.util.List;

public class SmsViewModel extends ViewModel {

    private static final String TAG = SmsViewModel.class.getName();
    private AppDatabase db;
    private boolean serviceProceeded;
    private List<Sms> smsList;

    SmsViewModel() {
        db = DatabaseHelper.getInstance();
        startLoadingSms();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        Log.d(TAG, "onCleared");
    }

    public void doFetchQuery() {
        if (smsList == null)
            smsList = db.daoSms().getGroupedList();
    }

    public List<Sms> getList() {
        int size = 0;
        if (smsList != null)
            size = smsList.size();

        Log.d(TAG, "totalSize:" + size);
        return smsList;
    }

    public void startLoadingSms() {
        if (!serviceProceeded) {
            DatabaseHelper.startServiceForSms();
            serviceProceeded = true;
        }
    }
}
