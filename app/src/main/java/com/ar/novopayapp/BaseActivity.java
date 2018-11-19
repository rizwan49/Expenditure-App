package com.ar.novopayapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.ar.novopayapp.utils.AppConstants;
import com.ar.novopayapp.utils.EventBusAction;
import com.ar.novopayapp.utils.ViewDialog;

import org.greenrobot.eventbus.EventBus;

import static com.ar.novopayapp.utils.AppConstants.REQUEST_READ_SMS_PERMISSION;

public abstract class BaseActivity extends AppCompatActivity {
    private int PERMISSION_COUNT = 0;
    private int PERMISSION_LIMIT = 3;
    private static final String TAG = BaseActivity.class.getName();
    private ViewDialog dialog;

    /***
     * use this method to invoke dialog
     * @param msg need to show into dialog
     * @param eventCallbackAction for EventBus to invoke callback;
     */
    public void showDialog(String msg, String eventCallbackAction) {
        if (dialog == null)
            dialog = new ViewDialog();
        dialog.showDialog(this, msg, eventCallbackAction);
    }

    /***
     *
     * @return based on the os return Permission is granted or not
     */
    private boolean checkReadSMSPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED) {
                return true;
            }
            return false;
        }
        return true;

    }

    /***
     * if permission is not granted then asking permission;
     */
    public void checkAndAskReadSMSPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!checkReadSMSPermission()) {
                requestPermissions(new String[]{Manifest.permission.READ_SMS}, REQUEST_READ_SMS_PERMISSION);
                return;
            }
        }
        EventBus.getDefault().post(EventBusAction.PERMISSION_GRANTED);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (permissions.length == 0) return;
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (REQUEST_READ_SMS_PERMISSION == requestCode) {
                Log.d(TAG, "Permission Granted");
                EventBus.getDefault().post(EventBusAction.PERMISSION_GRANTED);
            }
        } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
            if (REQUEST_READ_SMS_PERMISSION == requestCode) {
                if (PERMISSION_COUNT++ > PERMISSION_LIMIT) {
                    Log.d(TAG, "Permission denied");
                    showDialog(getString(R.string.permission_msg), EventBusAction.FORCE_PERMISSION);
                } else
                    EventBus.getDefault().post(EventBusAction.PERMISSION_DENIED);
            }

        }
    }

}
