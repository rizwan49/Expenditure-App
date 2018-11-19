package com.ar.novopayapp.modals;

import android.support.annotation.NonNull;

import com.ar.novopayapp.utils.Util;
import com.ar.novopayapp.db.Sms;

import java.util.ArrayList;
import java.util.List;

public class DebitedModal implements Comparable<DebitedModal> {
    private String header;
    private String month;
    private List<DebitedAmount> debitedAmountList;
    private long timeStamp;

    public DebitedModal() {
        if (debitedAmountList == null)
            debitedAmountList = new ArrayList<>();
    }


    public void setupData(Sms obj) {
        if (obj == null) return;
        debitedAmountList.add(new DebitedAmount(Util.getDayFromTimeStamp(obj.getTimeStamp()), Float.valueOf(obj.getDebited())));
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public List<DebitedAmount> getDebitedAmountList() {
        return debitedAmountList;
    }

    public void setDebitedAmountList(List<DebitedAmount> debitedAmountList) {
        this.debitedAmountList = debitedAmountList;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    @Override
    public int compareTo(@NonNull DebitedModal debitedModal) {
        if (this.timeStamp < debitedModal.timeStamp)
            return 1;
        return -1;
    }
}
