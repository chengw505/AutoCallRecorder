package com.chengw.autocallrecorder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * Created by Cheng on 3/31/2015.
 */
public class TelListenerReceiver extends BroadcastReceiver {
    public final static String serviceNameTag = "com.chengw.autocallrecorder.tellistner";
    public final static String logTag = "ServiceLog";

    public final static String incomingTag = "incomingTag";
    public final static String phoneNumberTag = "phone_number";

    private static Boolean isIncoming = true;
    private static String phoneNum = null;

    @Override
    public void onReceive(Context context, Intent intent) {

        if (null == intent || null == context) {
            return;
        }

        // outgoing: ACTION_NEW_OUTGOING_CALL -> EXTRA_STATE_OFFHOOK -> EXTRA_STATE_IDLE
        String actionString = intent.getAction();
        if (Intent.ACTION_NEW_OUTGOING_CALL.equals(actionString)) {

            isIncoming = false;
            phoneNum = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
            Log.d(logTag, "[ACTION_NEW_OUTGOING_CALL] outgoing call: " + phoneNum);

            startService(context);

            return;
        }

        // incoming: EXTRA_STATE_RINGING -> EXTRA_STATE_OFFHOOK -> EXTRA_STATE_IDLE
        final String stringExtra = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
        if (stringExtra.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
            Log.d(logTag, "[EXTRA_STATE_IDLE] outgoing call: " + phoneNum);

            if (!isIncoming) {
                // outgoing phone call stopped, restore state
                isIncoming = true;
                phoneNum = null;
            }

            startService(context);
        } else if (stringExtra.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
            Log.d(logTag, "[EXTRA_STATE_RINGING] incoming call: " + phoneNum);

            if (isIncoming) {
                phoneNum = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
            } else {
                // outgoing phone call ringing...
            }

            startService(context);
        } else if (stringExtra.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
            Log.d(logTag, "[EXTRA_STATE_OFFHOOK] outgoing call: " + phoneNum);

            // phone connected may be outgoing or incoming call
            startService(context);
        }
    }

    private void startService(Context context) {

        Intent i = new Intent(serviceNameTag);
        i.putExtra(phoneNumberTag, phoneNum);
        i.putExtra(incomingTag, isIncoming);

        context.startService(i);
    }
}
