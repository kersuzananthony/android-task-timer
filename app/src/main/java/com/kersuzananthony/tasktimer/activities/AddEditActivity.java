package com.kersuzananthony.tasktimer.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.kersuzananthony.tasktimer.AddEditActivityFragment;
import com.kersuzananthony.tasktimer.ApplicationDialogFragment;
import com.kersuzananthony.tasktimer.R;
import com.kersuzananthony.tasktimer.models.Task;

public class AddEditActivity extends AppCompatActivity implements AddEditActivityFragment.OnFragmentInteractionListener,
        ApplicationDialogFragment.DialogEvents {

    private static final String TAG = AddEditActivity.class.getSimpleName();

    public static final String EXTRA_TASK = "TASK";
    public static final int DIALOG_ID_ADD_EDIT = 1;

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

        setTaskToFragment();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy: ");

        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                AddEditActivityFragment fragment = (AddEditActivityFragment) getSupportFragmentManager().findFragmentById(R.id.add_edit_activity_detailContainer);
                if (fragment != null && fragment.canClose()) {
                    return super.onOptionsItemSelected(item);
                } else {
                    showConfirmationDialog();
                    return true;
                }
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setTaskToFragment() {
        Bundle arguments = getIntent().getExtras();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        AddEditActivityFragment addEditActivityFragment = new AddEditActivityFragment();
        addEditActivityFragment.setArguments(arguments);
        fragmentTransaction.replace(R.id.add_edit_activity_detailContainer, addEditActivityFragment, AddEditActivityFragment.FRAGMENT_TAG);
        fragmentTransaction.commit();
    }

    @Override
    public void onSaveClicked() {
        finish();
    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed: ");

        FragmentManager fragmentManager = getSupportFragmentManager();
        AddEditActivityFragment fragment = (AddEditActivityFragment) fragmentManager.findFragmentById(R.id.add_edit_activity_detailContainer);
        if (fragment == null || fragment.canClose()) {
            super.onBackPressed();
        } else {
            showConfirmationDialog();
        }
    }

    private void showConfirmationDialog() {
        ApplicationDialogFragment applicationDialogFragment = new ApplicationDialogFragment();
        Bundle args = new Bundle();
        args.putInt(ApplicationDialogFragment.DIALOG_ID, AddEditActivity.DIALOG_ID_ADD_EDIT);
        args.putString(ApplicationDialogFragment.DIALOG_MESSAGE, getString(R.string.close_edit_dialog_message));
        args.putInt(ApplicationDialogFragment.DIALOG_POSITIVE_RID, R.string.close_edit_dialog_positiveCaption);
        args.putInt(ApplicationDialogFragment.DIALOG_NEGATIVE_RID, R.string.close_edit_dialog_negativeCaption);

        applicationDialogFragment.setArguments(args);
        applicationDialogFragment.show(getSupportFragmentManager(), null);
    }

    @Override
    public void onPositiveDialogResult(int dialogId, Bundle args) {

    }

    @Override
    public void onNegativeDialogResult(int dialogId, Bundle args) {
        finish();
    }

    @Override
    public void onCancel(int dialogId) {

    }
}
