package com.kersuzananthony.tasktimer.adapters;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.kersuzananthony.tasktimer.R;
import com.kersuzananthony.tasktimer.data.TaskContract;

public class CursorRecyclerViewAdapter extends RecyclerView.Adapter<CursorRecyclerViewAdapter.TaskViewHolder> {

    private static final String TAG = "CursorRecyclerViewAdapt";

    private Cursor mCursor;

    public CursorRecyclerViewAdapter(Cursor cursor) {
        mCursor = cursor;
    }

    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.task_list_item, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TaskViewHolder holder, int position) {
        if (mCursor == null || mCursor.getCount() == 0) {
            holder.bindEmpty();
        } else {
            if (!mCursor.moveToPosition(position)) {
                throw new IllegalStateException("Could not move cursor to " + position);
            } else {
                String name = mCursor.getString(mCursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_NAME));
                String description = mCursor.getString(mCursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_DESCRIPTION));

                holder.bindView(name, description);
            }
        }
    }

    @Override
    public int getItemCount() {
        if (mCursor == null || mCursor.getCount() == 0) return 1;
        return mCursor.getCount();
    }

    public Cursor swapCursor(Cursor newCursor) {
        if (newCursor == mCursor) return null;

        final Cursor oldCursor = mCursor;
        mCursor = newCursor;

        if (newCursor != null) {
            notifyDataSetChanged();
        } else {
            notifyItemRangeRemoved(0, getItemCount());
        }

        return oldCursor;
    }

    static class TaskViewHolder extends RecyclerView.ViewHolder {

        TextView nameTextView = null;
        TextView descriptionTextView = null;
        ImageButton editButton = null;
        ImageButton deleteButton = null;

        TaskViewHolder(View itemView) {
            super(itemView);

            nameTextView = (TextView) itemView.findViewById(R.id.task_list_item_nameTextView);
            descriptionTextView = (TextView) itemView.findViewById(R.id.task_list_item_descriptionTextView);
            editButton = (ImageButton) itemView.findViewById(R.id.task_list_item_editButton);
            deleteButton = (ImageButton) itemView.findViewById(R.id.task_list_item_deleteButton);
        }

        void bindView(String name, String description) {
            nameTextView.setText(name);
            descriptionTextView.setText(description);
            editButton.setVisibility(View.VISIBLE);
            deleteButton.setVisibility(View.VISIBLE);
        }

        void bindEmpty() {
            nameTextView.setText(R.string.instructions_heading);
            descriptionTextView.setText(R.string.instructions_description);
            editButton.setVisibility(View.GONE);
            deleteButton.setVisibility(View.GONE);
        }
    }
}
