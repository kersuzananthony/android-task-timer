package com.kersuzananthony.tasktimer;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.kersuzananthony.tasktimer.data.TaskContract;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        String[] projection = {
                TaskContract.TaskEntry._ID,
                TaskContract.TaskEntry.COLUMN_NAME,
                TaskContract.TaskEntry.COLUMN_DESCRIPTION,
                TaskContract.TaskEntry.COLUMN_SORT_ORDER
        };
        ContentResolver resolver = getContentResolver();
        Cursor cursor = resolver.query(TaskContract.CONTENT_URI, projection, null, null, TaskContract.TaskEntry.COLUMN_NAME);

        ContentValues contentValues = new ContentValues();
        contentValues.put(TaskContract.TaskEntry.COLUMN_NAME, "First task");
        contentValues.put(TaskContract.TaskEntry.COLUMN_DESCRIPTION, "First task description");
        contentValues.put(TaskContract.TaskEntry.COLUMN_SORT_ORDER, 1);
        Uri uri = resolver.insert(TaskContract.CONTENT_URI, contentValues);
        Log.d(TAG, "onCreate: uri inserted is: " + uri);

        if (cursor != null) {
            Log.d(TAG, "onCreate: " + cursor.getCount());

            while (cursor.moveToNext()) {
                Log.d(TAG, "onCreate: hasNextLine()");
            }

            cursor.close();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_main_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
