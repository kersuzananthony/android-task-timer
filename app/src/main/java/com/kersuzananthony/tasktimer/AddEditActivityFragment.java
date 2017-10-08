package com.kersuzananthony.tasktimer;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.kersuzananthony.tasktimer.activities.AddEditActivity;
import com.kersuzananthony.tasktimer.data.TaskContract;
import com.kersuzananthony.tasktimer.models.Task;

/**
 * A placeholder fragment containing a simple view.
 */
public class AddEditActivityFragment extends Fragment {

    public enum FragmentEditMode { ADD, EDIT };

    private static final String TAG = AddEditActivityFragment.class.getSimpleName();
    public static final String FRAGMENT_TAG = AddEditActivity.class.getName();

    private FragmentEditMode mEditMode;
    private EditText mTaskNameEditText;
    private EditText mTaskDescriptionEditText;
    private EditText mTaskSortOrderEditText;
    private Button mSubmitButton;
    private Task mTask;

    public AddEditActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_edit, container, false);

        mTaskNameEditText = (EditText) view.findViewById(R.id.add_edit_taskNameEditText);
        mTaskDescriptionEditText = (EditText) view.findViewById(R.id.add_edit_taskDescriptionEditText);
        mTaskSortOrderEditText = (EditText) view.findViewById(R.id.add_edit_taskSortEditText);
        mSubmitButton = (Button) view.findViewById(R.id.add_edit_submitButton);

        Bundle arguments = getArguments();

        final Task task;
        if (arguments != null) {
            task = arguments.getParcelable(AddEditActivity.EXTRA_TASK);
            if (task != null) {
                mTaskNameEditText.setText(task.getName());
                mTaskDescriptionEditText.setText(task.getDescription());
                mTaskSortOrderEditText.setText(Integer.toString(task.getSortOrder()));
                mEditMode = FragmentEditMode.EDIT;
                mTask = task;
            } else {
                mEditMode = FragmentEditMode.ADD;
            }
        } else {
            mEditMode = FragmentEditMode.ADD;
        }

        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData();
            }
        });

        return view;
    }

    private void saveData() {
        int sortOrder;

        if (mTaskSortOrderEditText.length() > 0) {
            sortOrder = Integer.parseInt(mTaskSortOrderEditText.getText().toString());
        } else {
            sortOrder = 0;
        }

        ContentResolver contentResolver = getActivity().getContentResolver();
        ContentValues contentValues = new ContentValues();

        switch (mEditMode) {
            case EDIT:
                if (mTask == null) return;
                if (!mTaskNameEditText.getText().toString().equals(mTask.getName())) {
                    contentValues.put(TaskContract.TaskEntry.COLUMN_NAME, mTaskNameEditText.getText().toString());
                }

                if (!mTaskDescriptionEditText.getText().toString().equals(mTask.getDescription())) {
                    contentValues.put(TaskContract.TaskEntry.COLUMN_DESCRIPTION, mTaskDescriptionEditText.getText().toString());
                }

                if (sortOrder != mTask.getSortOrder()) {
                    contentValues.put(TaskContract.TaskEntry.COLUMN_SORT_ORDER, sortOrder);
                }

                if (contentValues.size() != 0) {
                    Log.d(TAG, "saveData: Begin update");
                    contentResolver.update(TaskContract.buildTaskUri(mTask.getId()), contentValues, null, null);
                }
                break;
            case ADD:
                if (mTaskNameEditText.length() > 0) {
                    contentValues.put(TaskContract.TaskEntry.COLUMN_NAME, mTaskNameEditText.getText().toString());
                    contentValues.put(TaskContract.TaskEntry.COLUMN_DESCRIPTION, mTaskDescriptionEditText.getText().toString());
                    contentValues.put(TaskContract.TaskEntry.COLUMN_SORT_ORDER, sortOrder);

                    contentResolver.insert(TaskContract.CONTENT_URI, contentValues);
                }
                break;
            default:
                break;
        }
    }
}
