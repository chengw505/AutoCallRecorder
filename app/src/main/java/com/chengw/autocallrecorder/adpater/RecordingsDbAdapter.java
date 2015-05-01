package com.chengw.autocallrecorder.adpater;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.widget.Toast;

import com.chengw.autocallrecorder.MainActivity;
import com.chengw.autocallrecorder.RecordingItem;

/**
 * Created by Cheng on 4/30/2015.
 */
public class RecordingsDbAdapter {
    private SQLiteDatabase mDb;
    private Context mCtx;

    public static final String TABLE_RECORDINGS = "tbl_recordings";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_FILENAME = "filename";
    public static final String COLUMN_IN_CLOUD = "in_cloud";
    public static final String COLUMN_COMMENT = "comment";
    private String[] ALL_COLUMNS = { COLUMN_ID,
            COLUMN_FILENAME,
            COLUMN_IN_CLOUD,
            COLUMN_COMMENT };

    private static final String DATABASE_NAME = "recordings.db";
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_CREATE_TABLE = "create table if not exists "
            + TABLE_RECORDINGS + "(" + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_FILENAME
            + " text not null, " + COLUMN_IN_CLOUD
            + " smallint, " + COLUMN_COMMENT
            + " text);";
    private Cursor mLocalRecordingCursor;

    public RecordingsDbAdapter(Context ctx) {
        mCtx = ctx;
    }

    public RecordingsDbAdapter open() {
        try {
            // create content table
            String fullPathName = Environment.getExternalStorageDirectory() + "/" + MainActivity.recordingDir + "/" + DATABASE_NAME;
            mDb = SQLiteDatabase.openOrCreateDatabase(fullPathName, null);

            // create main resource table
            mDb.execSQL(DATABASE_CREATE_TABLE);

        } catch (Exception e) {
            Toast.makeText(mCtx, e.getMessage(), Toast.LENGTH_LONG).show();
            return null;
        }

        return this;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();

        mDb.close();
    }

    public void close() {
        mDb.close();
    }

    public long createRecordingItem(String filename, int in_cloud, String comment) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_FILENAME, filename);
        values.put(COLUMN_IN_CLOUD, in_cloud);
        values.put(COLUMN_COMMENT, comment);

        if(!mDb.isOpen()) {
            return -1;
        }
        long newRowId = mDb.insert(TABLE_RECORDINGS, null,
                values);

        return newRowId;
    }

    public RecordingItem getRecordingItem(long rowId) {

        Cursor cursor = mDb.query(TABLE_RECORDINGS,
                ALL_COLUMNS, COLUMN_ID + " = " + rowId, null,
                null, null, null);
        cursor.moveToFirst();
        RecordingItem recordingItem = cursorToRecordingItem(cursor);
        cursor.close();

        return recordingItem;
    }

    public RecordingItem getRecordingItem(String recordingFileName) {
        RecordingItem recordingItem = null;

        Cursor cursor = mDb.query(TABLE_RECORDINGS,
                ALL_COLUMNS, COLUMN_FILENAME + " = \"" + recordingFileName + "\"", null,
                null, null, null);
        if(cursor.getCount() >= 1) {
            cursor.moveToFirst();
            recordingItem = cursorToRecordingItem(cursor);
        }

        cursor.close();

        return recordingItem;
    }

    public long updateComment(long rowId, String comment) {
        // New value for one column
        ContentValues values = new ContentValues();
        values.put(COLUMN_COMMENT, comment);

        // Which row to update, based on the ID
        String selection = COLUMN_ID + " =";
        String[] selectionArgs = { String.valueOf(rowId) };

        int count = mDb.update(
                TABLE_RECORDINGS,
                values,
                selection,
                selectionArgs);

        return count;
    }

    public long updateCloudState(long rowId, int in_cloud) {
        // New value for one column
        ContentValues values = new ContentValues();
        values.put(COLUMN_IN_CLOUD, in_cloud);

        // Which row to update, based on the ID
        String selection = COLUMN_ID + " =";
        String[] selectionArgs = { String.valueOf(rowId) };

        int count = mDb.update(
                TABLE_RECORDINGS,
                values,
                selection,
                selectionArgs);

        return count;
    }

    private RecordingItem cursorToRecordingItem(Cursor cursor) {
        RecordingItem recording = new RecordingItem();
        recording.setId(cursor.getLong(0));
        recording.setFilename(cursor.getString(1));
        recording.setIncloud(cursor.getInt(2));
        recording.setComment(cursor.getString(3));

        return recording;
    }

    public String getFirstLocalRecording() {
        String fileName = null;

        mLocalRecordingCursor = mDb.query(TABLE_RECORDINGS,
                ALL_COLUMNS, COLUMN_IN_CLOUD + " = 0", null,
                null, null, null);
        if(mLocalRecordingCursor.getCount() >= 1) {
            mLocalRecordingCursor.moveToFirst();
            fileName = cursorToRecordingItem(mLocalRecordingCursor).getFilename();
        }

        return fileName;
    }

    public String getNextLocalRecording() {
        String fileName = null;

        if(mLocalRecordingCursor != null) {
            if(mLocalRecordingCursor.moveToNext()) {
                fileName = cursorToRecordingItem(mLocalRecordingCursor).getFilename();
            } else {
                mLocalRecordingCursor.close();
            }
        }

        return fileName;
    }
}
