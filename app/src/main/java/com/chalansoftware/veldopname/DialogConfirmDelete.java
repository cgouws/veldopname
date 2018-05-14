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
 * Created by Charl Gouws on 2017/12/14.
 *
 * Shown when user wants to delete an entry.
 */

public class DialogConfirmDelete
        extends DialogFragment {
    OnDeleteDialogListener mDeleteDialogListener;
    
    public static final String DELETE_POSITION_ARGS = "delete_position";
    
    public interface OnDeleteDialogListener {
        // Interface methods used in MainActivity.
        void onDeleteDialogPositiveClick(DialogFragment dialogFragment, int position);
        
        void onDeleteDialogNegativeClick(DialogFragment dialogFragment);
    }
    @Override public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host Activity implements the callback interface.
        try {
            mDeleteDialogListener = (OnDeleteDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                                                 + " must implement DeleteDialogListener");
        }
    }
    public static DialogConfirmDelete newInstance(int position) {
        Bundle args = new Bundle();
        args.putInt(DELETE_POSITION_ARGS, position);
        DialogConfirmDelete dialogConfirmDelete = new DialogConfirmDelete();
        dialogConfirmDelete.setArguments(args);
        return dialogConfirmDelete;
    }
    @NonNull @Override public Dialog onCreateDialog(Bundle savedInstanceState) {
        
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_confirm, null);
        
        final int position = getArguments().getInt(DELETE_POSITION_ARGS);
        
        final ImageButton confirmButton = view.findViewById(R.id.dialog_delete_ok_btn);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mDeleteDialogListener.onDeleteDialogPositiveClick(DialogConfirmDelete
                                                                          .this, position);
                Toast.makeText(getActivity(), "Uitgevee", Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });
        final ImageButton cancelButton = view.findViewById(R.id.dialog_delete_cancel_btn);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mDeleteDialogListener.onDeleteDialogNegativeClick(DialogConfirmDelete.this);
                Toast.makeText(getActivity(), "Gekanseleer", Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });
        return new AlertDialog.Builder(getActivity(), R.style.MyDialogTheme).setTitle("Vee uit?")
                .setView(view)
                .create();
    }
}
