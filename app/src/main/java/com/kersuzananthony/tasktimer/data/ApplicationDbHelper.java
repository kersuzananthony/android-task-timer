package com.kersuzananthony.tasktimer.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ApplicationDbHelper extends SQLiteOpenHelper {

    private static final String TAG = "ApplicationDbHelper";
    private static final String DATABASE_NAME = "TaskTimer.db";
    private static final int DATABASE_VERSION = 1;

    // ApplicationDbHelper implemented as a singleton.
    private static ApplicationDbHelper instance = null;

    public static ApplicationDbHelper getInstance(Context context) {
        if (instance == null) {
            instance = new ApplicationDbHelper(context);
        }

        return instance;
    }

    private ApplicationDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String CREATE_TABLE = "CREATE TABLE "         + TaskContract.TaskEntry.TABLE_NAME + " (" +
                TaskContract.TaskEntry._ID                  + " INTEGER PRIMARY KEY NOT NULL, " +
                TaskContract.TaskEntry.COLUMN_NAME          + " TEXT NOT NULL, " +
                TaskContract.TaskEntry.COLUMN_DESCRIPTION   + " TEXT, " +
                TaskContract.TaskEntry.COLUMN_SORT_ORDER    + " INTEGER);";

        Log.d(TAG, "onCreate: CREATE TABLE -> " + CREATE_TABLE);
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TaskContract.TaskEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
