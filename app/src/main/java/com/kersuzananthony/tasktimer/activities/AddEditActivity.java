package com.kersuzananthony.tasktimer.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.kersuzananthony.tasktimer.R;
import com.kersuzananthony.tasktimer.models.Task;

public class AddEditActivity extends AppCompatActivity {

    private static final String TAG = AddEditActivity.class.getSimpleName();

    public static final String EXTRA_TASK = "TASK";

    static Intent newIntent(@NonNull Context context, @Nullable Task task) {
        Intent intent = new Intent(context, AddEditActivity.class);
        if (task != null) {
            intent.putExtra(EXTRA_TASK, task);
        }

        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_TASK)) {

        }
    }

}
