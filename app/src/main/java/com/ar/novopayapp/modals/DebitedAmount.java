package com.ar.novopayapp.modals;

public class DebitedAmount {
    private String day;
    private float totalAmountInDay;

    public DebitedAmount(String day, float totalAmountInDay) {
        this.day = day;
        this.totalAmountInDay = totalAmountInDay;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public float getTotalAmountInDay() {
        return totalAmountInDay;
    }

    public void setTotalAmountInDay(int totalAmountInDay) {
        this.totalAmountInDay = totalAmountInDay;
    }
}
