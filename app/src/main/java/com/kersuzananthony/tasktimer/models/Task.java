package com.kersuzananthony.tasktimer.models;


import android.os.Parcel;
import android.os.Parcelable;

public class Task implements Parcelable {

    private long mId;
    private final String mName;
    private final String mDescription;
    private final int mSortOrder;

    public Task(long id, String name, String description, int sortOrder) {
        mId = id;
        mName = name;
        mDescription = description;
        mSortOrder = sortOrder;
    }

    public Task(Parcel in) {
        mId = in.readLong();
        mName = in.readString();
        mDescription = in.readString();
        mSortOrder = in.readInt();
    }

    public long getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public String getDescription() {
        return mDescription;
    }

    public int getSortOrder() {
        return mSortOrder;
    }

    public void setId(long id) {
        mId = id;
    }

    @Override
    public String toString() {
        return "Task{" +
                "mId=" + mId +
                ", mName='" + mName + '\'' +
                ", mDescription='" + mDescription + '\'' +
                ", mSortOrder=" + mSortOrder +
                '}';
    }

    public static final Creator<Task> CREATOR = new Creator<Task>() {
        @Override
        public Task createFromParcel(Parcel in) {
            return new Task(in);
        }

        @Override
        public Task[] newArray(int size) {
            return new Task[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(mId);
        dest.writeString(mName);
        dest.writeString(mDescription);
        dest.writeInt(mSortOrder);
    }
}
