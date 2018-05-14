package com.chalansoftware.veldopname;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

/**
 * Created by Charl Gouws on 2018/05/08.
 */

public class DialogSelectSaveName
        extends DialogFragment {
    DialogSelectSaveName.OnSelectSaveNameDialogDismissedListener
            mOnSelectSaveNameDialogDismissedListener;
    
    interface OnSelectSaveNameDialogDismissedListener {
        void onSelectSaveNameDialogDismissed(String name);
    }
    @Override public void onAttach(Context context) {
        super.onAttach(context);
        // Method called automatically by the Android system when fragment has been initialized
        // and associated with an activity, storing a local reference to this activity and
        // verifying that it implements the correct interface.
        try {
            mOnSelectSaveNameDialogDismissedListener
                    = (DialogSelectSaveName.OnSelectSaveNameDialogDismissedListener) context;
        } catch (ClassCastException cce) {
            throw new ClassCastException(context.toString()
                    + " must implement "
                    + "OnSelectSaveNameDialogDismissedListener");
        }
    }
    @NonNull @Override public Dialog onCreateDialog(final Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity())
                .inflate(R.layout.dialog_selectsavename, null);
        
        // EditText to enter a name used for the filename
        // in the method MainActivity.backupDatabaseToCsv(String).
        final EditText saveNameEditText = view.findViewById(R.id.edittext_savename);
        
        ImageButton finishedButtonSave = view.findViewById(R.id.finished_button_save);
        
        finishedButtonSave.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                // When the finished button is clicked the following method in MainActivity is
                // called, changing the string used for a savename.
                dismiss();
                mOnSelectSaveNameDialogDismissedListener.onSelectSaveNameDialogDismissed(
                        String.valueOf(saveNameEditText.getText()));
            }
        });
        
        return new AlertDialog.Builder(getActivity(), R.style.MyDialogTheme).setView(view)
                .setTitle("Kies 'n naam")
                .create();
    }
}
