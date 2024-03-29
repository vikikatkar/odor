package com.mga.vikram.odor;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class YesNoDialog extends DialogFragment {

    String title;
    String message;
    DialogInterface.OnClickListener listener;

    public YesNoDialog(String title, String message,DialogInterface.OnClickListener listener) {
        this.title = title;
        this.message = message;
        this.listener = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setTitle(title)
                .setMessage(message)
                .setNegativeButton("NO", new Dialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing (will close dialog)
                    }
                })
                .setPositiveButton("Yes! I am Ready !",  listener)
                .create();
    }
}