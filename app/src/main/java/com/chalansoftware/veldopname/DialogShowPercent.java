package com.chalansoftware.veldopname;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Charl Gouws on 2017/11/14.
 */

public class DialogShowPercent
        extends DialogFragment {
    
    private List<Point> mPointsList;
    public static final String BUNDLE_KEY = "List";
    
    static DialogShowPercent newInstance(ArrayList<Point> points){
        DialogShowPercent dialogShowPercent = new DialogShowPercent();
        Bundle args = new Bundle();
        args.putParcelableArrayList(BUNDLE_KEY, points);
        dialogShowPercent.setArguments(args);
        return dialogShowPercent;
    }
    @NonNull @Override public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (getArguments() != null){
            mPointsList = getArguments().getParcelableArrayList(BUNDLE_KEY);
        }
        View view = LayoutInflater.from(getActivity()).inflate(R.layout
                .fragment_pointpercentage_list, null);
        if (view instanceof RecyclerView){
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(new MyPointPercentageRecyclerViewAdapter(mPointsList));
        }
        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle(R.string.persentasie_resultaat)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                    }
                })
                .create();
    }
}
