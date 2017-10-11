package com.kersuzananthony.tasktimer;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

public class ApplicationDialogFragment extends DialogFragment {

    private static final String TAG = "ApplicationDialogFragme";

    public static final String DIALOG_ID = "id";
    public static final String DIALOG_MESSAGE = "message";
    public static final String DIALOG_POSITIVE_RID = "positive_rid";
    public static final String DIALOG_NEGATIVE_RID = "negative_rid";

    public interface DialogEvents {
        void onPositiveDialogResult(int dialogId, Bundle args);

        void onNegativeDialogResult(int dialogId, Bundle args);

        void onCancel(int dialogId);
    }

    private DialogEvents mDialogEvents;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof DialogEvents) {
            mDialogEvents = (DialogEvents) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement DialogEvents");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mDialogEvents = null;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Bundle arguments = getArguments();
        final int dialogId;
        String message;
        int positiveRid;
        int negativeRid;

        if (arguments == null)
            throw new IllegalArgumentException("Must call " + ApplicationDialogFragment.class.getSimpleName() + " with bundle arguments");

        dialogId = arguments.getInt(DIALOG_ID);
        message = arguments.getString(DIALOG_MESSAGE);

        if (dialogId == 0 || message == null)
            throw new IllegalArgumentException("Must call " + ApplicationDialogFragment.class.getSimpleName() + " with dialog id and message");

        positiveRid = arguments.getInt(DIALOG_POSITIVE_RID);
        negativeRid = arguments.getInt(DIALOG_NEGATIVE_RID);

        if (positiveRid == 0) {
            positiveRid = R.string.ok;
        }

        if (negativeRid == 0) {
            negativeRid = R.string.cancel;
        }

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        dialogBuilder.setMessage(message)
                .setPositiveButton(positiveRid, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (mDialogEvents != null)
                            mDialogEvents.onPositiveDialogResult(dialogId, arguments);
                    }
                })
                .setNegativeButton(negativeRid, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (mDialogEvents != null)
                            mDialogEvents.onNegativeDialogResult(dialogId, arguments);
                    }
                });

        return dialogBuilder.create();
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        if (mDialogEvents != null) mDialogEvents.onCancel(getArguments().getInt(DIALOG_ID));
    }
}
