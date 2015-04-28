package com.chengw.autocallrecorder.model;

import android.util.Log;

import com.chengw.autocallrecorder.MainActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Cheng on 3/3/2015.
 */
public class ItemCallHistoryModel {
    String recordFileName;
    String phoneNumber;
    boolean incomingCall;
    String localRecordingTime;
    boolean inCloud;

    SimpleDateFormat timeFormat = new SimpleDateFormat("yyMMddHHmmss");

    public ItemCallHistoryModel(String fileName) {
        recordFileName = fileName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    // TODO IN-5051231234-time-zone.amr
    public boolean legalRecord() {
        String[] parts = recordFileName.split("-");
        if(parts.length < 4) {
            Log.d(MainActivity.TAG, "invalid file: " + recordFileName);
            return false;
        }

        try {
            Date recordingTime = timeFormat.parse(parts[2]);
            localRecordingTime = new SimpleDateFormat("HH:mm").format(recordingTime);

        } catch (ParseException e) {
            Log.d(MainActivity.TAG, "invalid file: " + e.getMessage());
            return false;
        }
        incomingCall = parts[0].compareTo("IN") == 1;
        phoneNumber = parts[1];

        return true;
    }

    public String getCallTime() {
        return localRecordingTime;
    }

    public String getFileName() {
        return recordFileName;
    }
}
