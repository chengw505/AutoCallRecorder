package com.chengw.autocallrecorder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by Cheng on 3/31/2015.
 */
public class TelListenerReceiver extends BroadcastReceiver {
    public final String tag = "com.chengw.autocallrecorder.tellistner";

    final String phoneStateAction = "android.intent.action.PHONE_STATE";

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent != null) {
            String action = intent.getAction();

            if (action.equals(phoneStateAction)) {
                Intent i = new Intent(tag);
                context.startService(i);
            }
        }
    }
}
