package com.chalansoftware.veldopname;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

/**
 * Created by Charl Gouws on 2018/05/14.
 * <p>
 * Shown when user presses the reset menu item.
 */

public class DialogConfirmReset
        extends DialogFragment {
    OnResetDialogListener mResetDialogListener;
    
    public interface OnResetDialogListener {
        // Interface methods used in MainActivity.
        void onResetDialogPositiveClick(DialogFragment dialogFragment);
        
        void onResetDialogNegativeClick(DialogFragment dialogFragment);
    }
    @Override public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host Activity implements the callback interface.
        try {
            mResetDialogListener = (OnResetDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(
                    context.toString() + " must implement ResetDialogListener");
        }
    }
    @NonNull @Override public Dialog onCreateDialog(Bundle savedInstanceState) {
        
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_confirm, null);
        
        final ImageButton confirmButton = view.findViewById(R.id.dialog_delete_ok_btn);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mResetDialogListener.onResetDialogPositiveClick(DialogConfirmReset
                        .this);
                Toast.makeText(getActivity(), "Reset", Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });
        final ImageButton cancelButton = view.findViewById(R.id.dialog_delete_cancel_btn);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mResetDialogListener.onResetDialogNegativeClick(DialogConfirmReset.this);
                Toast.makeText(getActivity(), "Gekanseleer", Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });
        return new AlertDialog.Builder(getActivity(), R.style.MyDialogTheme).setTitle(
                R.string.reset_question).setView(view).create();
    }
}
