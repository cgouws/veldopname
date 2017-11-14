package com.chalansoftware.veldopname;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;

public class MyPointPercentageRecyclerViewAdapter
        extends RecyclerView.Adapter<MyPointPercentageRecyclerViewAdapter.ViewHolder> {
    
    private final List<Point> mPoints;
    private static final String TAG = "veld";
    
    MyPointPercentageRecyclerViewAdapter(List<Point> points) {
        mPoints = points;
    }
    
    @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_pointpercentage, parent, false);
        return new ViewHolder(view);
    }
    
    @Override public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mNameView.setText(mPoints.get(position).getName());
        
        // Formats the percentage for display
        String pattern = "#";
        double value = mPoints.get(position).getPercentage();
        DecimalFormat decimalFormat = new DecimalFormat(pattern);
        String output = decimalFormat.format(value);
        holder.mPercentageView.setText(String.format("%s %%", output));
    }
    
    @Override public int getItemCount() {
        Log.d(TAG, "MyPointPercentageRecyclerViewAdapter.getItemCount");
        return mPoints.size();
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
