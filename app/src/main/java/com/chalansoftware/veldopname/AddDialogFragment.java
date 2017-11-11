package com.chalansoftware.veldopname;


import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by Charl Gouws on 2017/11/11.
 */

public class AddDialogFragment
        extends DialogFragment {
    
    @NonNull @Override public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.add_dialog, null);
        
        
        
        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle("Nuwe Telpunt")
                .setPositiveButton("OK", null)
                .create();
    }
}
