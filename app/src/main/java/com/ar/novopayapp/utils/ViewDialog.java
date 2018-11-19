package com.ar.novopayapp.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.ar.novopayapp.R;

import org.greenrobot.eventbus.EventBus;

/***
 * this class help for custom alert dialog.
 */
public class ViewDialog {

    /***
     *
     * @param context need to invoke dialog
     * @param msg need to show into dialog
     * @param callback need to pass eventBusAction here to control the listeners;
     */
    public void showDialog(Context context, String msg, final String callback) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog);

        TextView text = dialog.findViewById(R.id.text_dialog);
        text.setText(msg);

        Button dialogButton = dialog.findViewById(R.id.btn_dialog);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                EventBus.getDefault().post(callback);
            }
        });

        dialog.show();

    }

}
