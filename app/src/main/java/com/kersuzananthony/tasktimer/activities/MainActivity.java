package com.kersuzananthony.tasktimer.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.kersuzananthony.tasktimer.R;
import com.kersuzananthony.tasktimer.models.Task;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String ADD_EDIT_FRAGMENT = "AddEditFragment";

    private boolean mTwoPane = false; // Activity in two pane mode

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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

        switch (id) {
            case R.id.menu_main_addTask:
                taskEditRequest(null);
                return true;
            case R.id.menu_main_showDuration:
                break;
            case R.id.menu_main_settings:
                break;
            case R.id.menu_main_showAbout:
                break;
            case R.id.menu_main_generate:
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void taskEditRequest(@Nullable Task task) {
        if (mTwoPane) {
            Log.d(TAG, "taskEditRequest: In two pane mode");
        } else {
            Log.d(TAG, "taskEditRequest: In phone portrait mode");
            startActivity(AddEditActivity.newIntent(this, task));
        }
    }
}
