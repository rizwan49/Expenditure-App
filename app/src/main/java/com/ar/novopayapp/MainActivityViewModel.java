package com.ar.novopayapp;

import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.ar.novopayapp.utils.Util;
import com.ar.novopayapp.db.AppDatabase;
import com.ar.novopayapp.db.DatabaseHelper;
import com.ar.novopayapp.modals.DebitedModal;
import com.ar.novopayapp.db.Sms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/***
 *
 * 1. store instance of DB (AppDatabase)
 * 2. help to startLoadingService
 * 3. service fetches SMS using content resolver then using Room persistence load into the database;
 * 4. smsList contains list of filtered records
 */
public class MainActivityViewModel extends ViewModel {

    private static final String TAG = MainActivityViewModel.class.getName();
    private AppDatabase db;
    private boolean serviceProceeded;
    private List<Sms> smsList;
    private List<DebitedModal> debitedModalList;

    public MainActivityViewModel() {
        db = DatabaseHelper.getInstance();
        startLoadingSms();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        Log.d(TAG, "onCleared");
    }

    DebitedModal obj;

    public void doFetchQuery() {
        if (smsList == null && debitedModalList == null) {
            debitedModalList = new ArrayList<>();
            smsList = db.daoSms().getGroupedList();
            String monthT = "";
            for (int i = 0; i < smsList.size(); i++) {
                if (!monthT.equals(smsList.get(i).getMonth())) {
                    obj = new DebitedModal();
                    obj.setMonth(smsList.get(i).getMonth());
                    monthT = smsList.get(i).getMonth();
                    obj.setHeader(smsList.get(i).getMonth() + " " + Util.getYearFromTimeStamp(smsList.get(i).getTimeStamp()));
                    obj.setTimeStamp(smsList.get(i).getTimeStamp());
                    debitedModalList.add(obj);
                }
                obj.setupData(smsList.get(i));
            }
        }

    }

    public List<Sms> getList() {
        int size = 0;
        if (smsList != null)
            size = smsList.size();

        Log.d(TAG, "totalSize:" + size);
        return smsList;
    }

    private void startLoadingSms() {
        if (!serviceProceeded) {
            DatabaseHelper.startServiceForSms();
            serviceProceeded = true;
        }
    }

    public List<DebitedModal> getAllDebitedList() {
        if (debitedModalList != null)
            Collections.sort(debitedModalList);
        return debitedModalList;
    }
}
