package com.ar.novopayapp;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.ar.novopayapp.Utils.EventBusAction;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public abstract class BaseActivity extends AppCompatActivity {

    private static final String TAG = BaseActivity.class.getName();

    public void showAppDialog(int please_wait_loading) {

    }
}
