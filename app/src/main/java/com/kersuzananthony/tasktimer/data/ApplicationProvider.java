package com.kersuzananthony.tasktimer.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

public class ApplicationProvider extends ContentProvider {

    private static final String TAG = "ApplicationProvider";
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    // The authority, which is how your code knows which Content Provider to access
    public static final String CONTENT_AUTHORITY = "com.kersuzananthony.tasktimer.provider";

    // The base content URI = "content://" + <authority>
    public static final Uri CONTENT_AUTHORITY_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final int TASKS = 100;
    public static final int TASK_ID = 101;
    public static final int TIMINGS = 200;
    public static final int TIMINGS_ID = 201;
    public static final int DURATIONS = 300;
    public static final int DURATIONS_ID = 301;

    private ApplicationDbHelper mOpenHelper;

    private static UriMatcher buildUriMatcher() {
        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

        matcher.addURI(CONTENT_AUTHORITY, TaskContract.PATH_TASKS, TASKS);
        matcher.addURI(CONTENT_AUTHORITY, TaskContract.PATH_TASKS + "/#", TASK_ID);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = ApplicationDbHelper.getInstance(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Log.d(TAG, "queryUri: " + uri);
        final int match = sUriMatcher.match(uri);

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        switch (match) {
            case TASKS:
                queryBuilder.setTables(TaskContract.TaskEntry.TABLE_NAME);
                break;

            case TASK_ID:
                queryBuilder.setTables(TaskContract.TaskEntry.TABLE_NAME);
                long taskId = TaskContract.getTaskId(uri);
                queryBuilder.appendWhere(TaskContract.TaskEntry._ID + "=" + taskId);
                break;

            default:
                throw new UnsupportedOperationException("Unknown Uri  " + uri);
        }

        SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);

        Context context = getContext();
        if (context != null) {
            cursor.setNotificationUri(context.getContentResolver(), uri);
        }

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = sUriMatcher.match(uri);
        String type;

        switch (match) {
            case TASKS:
                type = TaskContract.CONTENT_TYPE;
                break;
            case TASK_ID:
                type = TaskContract.CONTENT_ITEM_TYPE;
                break;
            default:
                throw new UnsupportedOperationException("No Type found for the provided uri " + uri);
        }

        return type;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);

        final SQLiteDatabase db;
        Uri returnUri = null;
        long recordId = -1;

        switch (match) {
            case TASKS:
                db = mOpenHelper.getWritableDatabase();
                recordId = db.insert(TaskContract.TaskEntry.TABLE_NAME, null, contentValues);
                if (recordId >= 0) {
                    returnUri = TaskContract.buildTaskUri(recordId);
                } else {
                    throw new android.database.SQLException("Failed to insert data into " + uri.toString());
                }
                break;
            case TIMINGS:
                break;
            case DURATIONS:
                break;
            case TASK_ID:
                break;
            case TIMINGS_ID:
                db = mOpenHelper.getWritableDatabase();
                break;
            case DURATIONS_ID:
                db = mOpenHelper.getWritableDatabase();
                break;
            default:
                throw new UnsupportedOperationException("Cannot insert data for the provided Uri");
        }

        if (recordId > 0 && getContext() != null) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);

        final SQLiteDatabase db;
        int count;
        String selectionCriteria;

        switch (match) {
            case TASKS:
                db = mOpenHelper.getWritableDatabase();
                count = db.delete(TaskContract.TaskEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case TASK_ID:
                db = mOpenHelper.getWritableDatabase();
                long taskId = TaskContract.getTaskId(uri);
                selectionCriteria = TaskContract.TaskEntry._ID + " = " + taskId;

                if (selection != null && selection.length() > 0) {
                    selectionCriteria += " AND (" + selection + ")";
                }

                count = db.delete(TaskContract.TaskEntry.TABLE_NAME, selectionCriteria, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Cannot insert data for the provided Uri");
        }

        if (count > 0 && getContext() != null) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return count;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection, @Nullable String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);

        final SQLiteDatabase db;
        int count;
        String selectionCriteria;

        switch (match) {
            case TASKS:
                db = mOpenHelper.getWritableDatabase();
                count = db.update(TaskContract.TaskEntry.TABLE_NAME, contentValues, selection, selectionArgs);
                break;
            case TASK_ID:
                db = mOpenHelper.getWritableDatabase();
                long taskId = TaskContract.getTaskId(uri);
                selectionCriteria = TaskContract.TaskEntry._ID + " = " + taskId;

                if (selection != null && selection.length() > 0) {
                    selectionCriteria += " AND (" + selection + ")";
                }

                count = db.update(TaskContract.TaskEntry.TABLE_NAME, contentValues, selectionCriteria, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Cannot insert data for the provided Uri");
        }

        if (count > 0 && getContext() != null) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return count;
    }
}
