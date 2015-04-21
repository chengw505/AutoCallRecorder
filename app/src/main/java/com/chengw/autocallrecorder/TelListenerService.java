package com.chengw.autocallrecorder;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Cheng on 3/31/2015.
 */
public class TelListenerService extends Service {
    private Boolean isIncoming;
    private String mIncomingNumber;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        isIncoming = intent.getBooleanExtra(TelListenerReceiver.incomingTag, true);
        mIncomingNumber = intent.getStringExtra(TelListenerReceiver.phoneNumberTag);

        return super.onStartCommand(intent, flags, startId);
    }

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
        private boolean mIsRecording;
        private MediaRecorder mRecorder;

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            switch (state) {
                case TelephonyManager.CALL_STATE_IDLE:

                    if (mRecorder != null && mIsRecording) {
                        Log.e(TelListenerReceiver.logTag, "CALL IDLE: " + mIncomingNumber);

                        mRecorder.stop();
                        mRecorder.reset();
                        mRecorder.release();
                        mIsRecording = false;
                        mIncomingNumber = null;
                    }
                    break;

                case TelephonyManager.CALL_STATE_RINGING:
                    Log.e(TelListenerReceiver.logTag, "CALL RINGING: " + mIncomingNumber);

                    break;

                case TelephonyManager.CALL_STATE_OFFHOOK:
                    Log.e(TelListenerReceiver.logTag, "CALL OFFHOOK: " + mIncomingNumber);

                    mRecorder = new MediaRecorder();
                    mRecorder.setAudioSource(MediaRecorder.AudioSource.VOICE_CALL);
                    mRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
                    mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);

                    File file = new File(Environment.getExternalStorageDirectory(), MainActivity.recordingDir);
                    if (!file.exists()) {
                        file.mkdirs();
                    }

                    SimpleDateFormat format = new SimpleDateFormat("yyMMddHHmmssZ");
                    String fileName = isIncoming ? "IN" : "OUT" + "-" + mIncomingNumber + "-" + format.format(new Date());
                    mRecorder.setOutputFile(file.getAbsolutePath() + File.separator + fileName + ".amr");
                    try {
                        mRecorder.prepare();
                        mRecorder.start();
                        mIsRecording = true;
                    } catch (IllegalStateException e) {
                        Log.e(TelListenerReceiver.logTag, e.getMessage());
                        e.printStackTrace();
                    } catch (IOException e) {
                        Log.e(TelListenerReceiver.logTag, e.getMessage());
                        e.printStackTrace();
                    }
                    break;

                default:
                    break;
            }
        }

    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }
}