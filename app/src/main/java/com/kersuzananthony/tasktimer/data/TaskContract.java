package com.kersuzananthony.tasktimer.data;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

import static com.kersuzananthony.tasktimer.data.ApplicationProvider.CONTENT_AUTHORITY;
import static com.kersuzananthony.tasktimer.data.ApplicationProvider.CONTENT_AUTHORITY_URI;

public class TaskContract {

    // Define the possible paths for accessing data in this contract
    // This is the path for the "tasks" directory
    public static final String PATH_TASKS = "tasks";

    public static final class TaskEntry implements BaseColumns {
        public static final String TABLE_NAME = "tasks";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_SORT_ORDER = "sortOrder";

        private TaskEntry() {}
    }

    public static final Uri CONTENT_URI = Uri.withAppendedPath(CONTENT_AUTHORITY_URI, PATH_TASKS);
    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd." + CONTENT_AUTHORITY + "." + PATH_TASKS;
    public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "." + PATH_TASKS;

    public static Uri buildTaskUri(long taskId) {
        return ContentUris.withAppendedId(CONTENT_URI, taskId);
    }

    static long getTaskId(Uri uri) {
        return ContentUris.parseId(uri);
    }
}
