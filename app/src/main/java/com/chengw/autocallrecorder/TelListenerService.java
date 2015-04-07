package com.chengw.autocallrecorder;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * Created by Cheng on 3/31/2015.
 */
public class TelListenerService extends Service {

    @Override
    public void onCreate() {
        TelephonyManager telManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        TelListener listener = new TelListener();
        telManager.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);

        super.onCreate();
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
    }

    class TelListener extends PhoneStateListener {
        private String mIncomingNumber;
        private boolean mIsRecording;
        private MediaRecorder mRecorder;

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            switch (state) {
                case TelephonyManager.CALL_STATE_IDLE:
                    mIncomingNumber = null;
                    if (mRecorder != null && mIsRecording) {
                        Log.e("msg", "record ok");
                        mRecorder.stop();
                        mRecorder.reset();
                        mRecorder.release();
                        mIsRecording = false;
                    }
                    break;

                case TelephonyManager.CALL_STATE_RINGING:
                    Log.e("msg", "coming");
                    mIncomingNumber = incomingNumber;
                    getContactByNumber(incomingNumber);
                    break;

                case TelephonyManager.CALL_STATE_OFFHOOK:
                    mIncomingNumber = incomingNumber;
                    Log.e("msg", "recording");
                    mRecorder = new MediaRecorder();
                    mRecorder.setAudioSource(MediaRecorder.AudioSource.VOICE_CALL);
                    mRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
                    mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
                    SimpleDateFormat format = new SimpleDateFormat("yy-MM-dd-HH:mm:ss");
                    String fileName = mIncomingNumber + "_" + format.format(new Date());
                    File file = new File(Environment.getExternalStorageDirectory(), "callrecord");
                    if (!file.exists()) {
                        file.mkdirs();
                    }
                    mRecorder.setOutputFile(file.getAbsolutePath() + File.separator + fileName + ".amr");
                    try {
                        mRecorder.prepare();
                        mRecorder.start();
                        mIsRecording = true;
                    } catch (IllegalStateException e) {
                        Log.e("msg", e.getMessage());
                        e.printStackTrace();
                    } catch (IOException e) {
                        Log.e("msg", e.getMessage());
                        e.printStackTrace();
                    }
                    break;

                default:
                    break;
            }
        }

    }

    /**
     * @param incomingNumber
     */
    private void getContactByNumber(String incomingNumber) {

        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = null;

        String[] projection = new String[]{ContactsContract.Contacts._ID, ContactsContract.Contacts.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER};

        cursor = contentResolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI, projection,
                ContactsContract.CommonDataKinds.Phone.NUMBER + "=?", new String[]
                        {incomingNumber}, null);

        if (cursor.getCount() == 0) {
            Log.e("msg", "unknown Number:" + incomingNumber);
        } else if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            String name = cursor.getString(1);
            Log.e("msg", name + " :" + incomingNumber);
        }
        cursor.close();
        cursor = null;
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }
}