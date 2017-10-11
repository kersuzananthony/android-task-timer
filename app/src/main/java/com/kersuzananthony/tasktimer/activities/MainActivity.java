package com.kersuzananthony.tasktimer.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.kersuzananthony.tasktimer.AddEditActivityFragment;
import com.kersuzananthony.tasktimer.ApplicationDialogFragment;
import com.kersuzananthony.tasktimer.R;
import com.kersuzananthony.tasktimer.adapters.CursorRecyclerViewAdapter;
import com.kersuzananthony.tasktimer.data.TaskContract;
import com.kersuzananthony.tasktimer.models.Task;

public class MainActivity extends AppCompatActivity implements
        CursorRecyclerViewAdapter.OnTaskClickListener,
        AddEditActivityFragment.OnFragmentInteractionListener,
        ApplicationDialogFragment.DialogEvents{

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String ADD_EDIT_FRAGMENT = "AddEditFragment";
    private static final String TASK_ID = "task_id";
    public static final int DIALOG_ID_DELETE = 1;
    public static final int DIALOG_ID_CLOSE = 2;

    private boolean mTwoPane = false; // Activity in two pane mode

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mTwoPane = findViewById(R.id.main_activity_taskDetailContainer) != null;
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

    @Override
    public void onEditClick(@NonNull Task task) {
        Log.d(TAG, "onEditClick: " + task.getName());
        taskEditRequest(task);
    }

    @Override
    public void onDeleteClick(@NonNull Task task) {
        Log.d(TAG, "onDeleteClick: " + task.getName());

        ApplicationDialogFragment dialogFragment = new ApplicationDialogFragment();
        Bundle arguments = new Bundle();
        arguments.putInt(ApplicationDialogFragment.DIALOG_ID, DIALOG_ID_DELETE);
        arguments.putString(ApplicationDialogFragment.DIALOG_MESSAGE, getString(R.string.deldial_message, task.getId(), task.getName()));
        arguments.putInt(ApplicationDialogFragment.DIALOG_POSITIVE_RID, R.string.deldial_title);
        arguments.putLong(MainActivity.TASK_ID, task.getId());
        dialogFragment.setArguments(arguments);

        dialogFragment.show(getSupportFragmentManager(), null);
    }

    @Override
    public void onSaveClicked() {
        Log.d(TAG, "onSaveClicked: ");
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.main_activity_taskDetailContainer);

        if (fragment != null) {
            fragmentManager.beginTransaction().remove(fragment).commit();
        }
    }

    @Override
    public void onPositiveDialogResult(int dialogId, Bundle args) {
        switch (dialogId) {
            case DIALOG_ID_DELETE:
                long taskId = args.getLong(MainActivity.TASK_ID);
                if (taskId == 0L) return;

                getContentResolver().delete(TaskContract.buildTaskUri(taskId), null, null);
                break;
            case DIALOG_ID_CLOSE:
                break;
            default:
                break;
        }
    }

    @Override
    public void onNegativeDialogResult(int dialogId, Bundle args) {
        switch (dialogId) {
            case DIALOG_ID_CLOSE:
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void onCancel(int dialogId) {

    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed: ");

        FragmentManager fragmentManager = getSupportFragmentManager();
        AddEditActivityFragment fragment = (AddEditActivityFragment) fragmentManager.findFragmentById(R.id.main_activity_taskDetailContainer);
        if (fragment == null || fragment.canClose()) {
            super.onBackPressed();
        } else {
            ApplicationDialogFragment applicationDialogFragment = new ApplicationDialogFragment();
            Bundle args = new Bundle();
            args.putInt(ApplicationDialogFragment.DIALOG_ID, MainActivity.DIALOG_ID_CLOSE);
            args.putString(ApplicationDialogFragment.DIALOG_MESSAGE, getString(R.string.close_edit_dialog_message));
            args.putInt(ApplicationDialogFragment.DIALOG_POSITIVE_RID, R.string.close_edit_dialog_positiveCaption);
            args.putInt(ApplicationDialogFragment.DIALOG_NEGATIVE_RID, R.string.close_edit_dialog_negativeCaption);

            applicationDialogFragment.setArguments(args);
            applicationDialogFragment.show(fragmentManager, null);
        }
    }

    private void taskEditRequest(@Nullable Task task) {
        if (mTwoPane) {
            Log.d(TAG, "taskEditRequest: In two pane mode");
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            AddEditActivityFragment addEditActivityFragment = new AddEditActivityFragment();
            Bundle arguments = new Bundle();
            arguments.putParcelable(AddEditActivity.EXTRA_TASK, task);
            addEditActivityFragment.setArguments(arguments);

            fragmentTransaction.replace(R.id.main_activity_taskDetailContainer, addEditActivityFragment, AddEditActivityFragment.FRAGMENT_TAG);
            fragmentTransaction.commit();
        } else {
            Log.d(TAG, "taskEditRequest: In phone portrait mode");
            startActivity(AddEditActivity.newIntent(this, task));
        }
    }
}
