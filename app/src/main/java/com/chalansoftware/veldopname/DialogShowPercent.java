package com.chalansoftware.veldopname;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Charl Gouws on 2017/11/14.
 * <p>
 * Dialog to display Points with their percentages
 */

public class DialogShowPercent
        extends DialogFragment {
    
    private List<Point> mPointsList;
    public static final String BUNDLE_KEY = "List";
    
    static DialogShowPercent newInstance(List<Point> points) {
        DialogShowPercent dialogShowPercent = new DialogShowPercent();
        Bundle args = new Bundle();
        args.putParcelableArrayList(BUNDLE_KEY, (ArrayList<? extends Parcelable>) points);
        dialogShowPercent.setArguments(args);
        return dialogShowPercent;
    }
    @NonNull @Override public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (getArguments() != null) {
            mPointsList = getArguments().getParcelableArrayList(BUNDLE_KEY);
        }
        View view = LayoutInflater.from(getActivity())
                .inflate(R.layout.fragment_pointpercentage_list, null);
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(new MyPointPercentageRecyclerViewAdapter());
        }
        return new AlertDialog.Builder(getActivity(), R.style.MyDialogTheme).setView(view)
                .setTitle(R.string.persentasie_resultaat)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                    }
                })
                .create();
    }
    
    public class MyPointPercentageRecyclerViewAdapter
            extends RecyclerView.Adapter<MyPointPercentageRecyclerViewAdapter.ViewHolder> {
        
        private static final String TAG = "veld";
        
        @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fragment_pointpercentage, parent, false);
            return new ViewHolder(view);
        }
        
        @Override public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mNameView.setText(mPointsList.get(position).getName());
            
            // Formats the percentage for display
            String pattern = "#";
            double value = mPointsList.get(position).getPercentage();
            DecimalFormat decimalFormat = new DecimalFormat(pattern);
            String output = decimalFormat.format(value);
            holder.mPercentageView.setText(String.format("%s %%", output));
        }
        
        @Override public int getItemCount() {
            return mPointsList.size();
        }
        
        public class ViewHolder
                extends RecyclerView.ViewHolder {
            final View mView;
            final TextView mNameView;
            final TextView mPercentageView;
            
            ViewHolder(View view) {
                super(view);
                mView = view;
                mNameView = view.findViewById(R.id.percentage_name_textview);
                mPercentageView = view.findViewById(R.id.percentage_display_textview);
            }
            
            @Override public String toString() {
                return super.toString() + " '" + mPercentageView.getText() + "'";
            }
        }
    }
}
