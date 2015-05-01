package com.chengw.autocallrecorder.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.chengw.autocallrecorder.RecordingItem;

import java.sql.SQLException;

/**
 * Created by Cheng on 4/30/2015.
 */
public class RecordingsSQLiteHelper extends SQLiteOpenHelper {

    public static final String TABLE_RECORDINGS = "tbl_recordings";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_FILENAME = "filename";
    public static final String COLUMN_INCLOUD = "in_cloud";
    public static final String COLUMN_COMMENT = "comment";

    private static final String DATABASE_NAME = "recordings.db";
    private static final int DATABASE_VERSION = 1;

    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_RECORDINGS + "(" + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_FILENAME
            + " text not null, " + COLUMN_INCLOUD
            + " smallint, " + COLUMN_COMMENT
            + " text);";

    public RecordingsSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(RecordingsSQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECORDINGS);
        onCreate(db);
    }

    /**
     * Created by Cheng on 4/30/2015.
     */
    public static class RecordingsDataSource {
        // Database fields
        private SQLiteDatabase database;
        private RecordingsSQLiteHelper dbHelper;
        private String[] allColumns = {COLUMN_ID,
                COLUMN_FILENAME,
                COLUMN_INCLOUD,
                COLUMN_COMMENT};

        public RecordingsDataSource(Context context) {
            dbHelper = new RecordingsSQLiteHelper(context);
        }

        public void open() throws SQLException {
            database = dbHelper.getWritableDatabase();
        }

        public void close() {
            dbHelper.close();
        }

        public long createRecordingItem(String filename, int in_cloud, String comment) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_FILENAME, filename);
            values.put(COLUMN_INCLOUD, in_cloud);
            values.put(COLUMN_COMMENT, comment);

            long newRowId = database.insert(TABLE_RECORDINGS, null,
                    values);

            return newRowId;
        }

        public RecordingItem getRecordingItem(long rowId) {

            Cursor cursor = database.query(TABLE_RECORDINGS,
                    allColumns, COLUMN_ID + " = " + rowId, null,
                    null, null, null);
            cursor.moveToFirst();
            RecordingItem recordingItem = cursorToRecordingItem(cursor);
            cursor.close();

            return recordingItem;
        }

        public RecordingItem getRecordingItem(String recordingFileName) {
            RecordingItem recordingItem = null;

            Cursor cursor = database.query(TABLE_RECORDINGS,
                    allColumns, COLUMN_FILENAME + " = \"" + recordingFileName + "\"", null,
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

            int count = database.update(
                    TABLE_RECORDINGS,
                    values,
                    selection,
                    selectionArgs);

            return count;
        }

        public long updateCloudState(long rowId, int in_cloud) {
            // New value for one column
            ContentValues values = new ContentValues();
            values.put(COLUMN_INCLOUD, in_cloud);

            // Which row to update, based on the ID
            String selection = COLUMN_ID + " =";
            String[] selectionArgs = { String.valueOf(rowId) };

            int count = database.update(
                    TABLE_RECORDINGS,
                    values,
                    selection,
                    selectionArgs);

            return count;
        }

        private RecordingItem cursorToRecordingItem(Cursor cursor) {
            RecordingItem comment = new RecordingItem();
            comment.setId(cursor.getLong(0));
            comment.setComment(cursor.getString(1));

            return comment;
        }
    }
}
