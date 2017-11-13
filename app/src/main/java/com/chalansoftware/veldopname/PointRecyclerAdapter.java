package com.chalansoftware.veldopname;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Charl Gouws on 2017/11/11.
 */

public class PointRecyclerAdapter
    extends RecyclerView.Adapter<PointRecyclerAdapter.PointViewHolder>{
    
    private List<Point> mPointsList = new ArrayList<>();
    private static final String TAG = "PointRecyclerAdapter";
    
    PointRecyclerAdapter(List<Point> listName){
        mPointsList = listName;
    }
    
    @Override public PointViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout
                .recyclerview_list_item, parent, false);
        return new PointViewHolder(view);
    }
    
    @Override public void onBindViewHolder(PointViewHolder holder, int position) {
        
        if (mPointsList.size() > 0){
            holder.nameTextView.setText(mPointsList.get(position).getName());}
        
        Log.d(TAG, "onBind");
    }
    
    @Override public int getItemCount() {
        Log.d(TAG, "mPointsList.size in PointRecyclerAdapter: " + mPointsList.size());
        return mPointsList.size();
    }
    
    
    
    public class PointViewHolder
            extends ViewHolder {
        
        TextView nameTextView;
        TextView countTextView;
        
        public PointViewHolder(final View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.recyclerview_item_name_textview);
            countTextView = itemView.findViewById(R.id.recyclerview_item_count_textview);
            
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                }
            });
        }
        void pointCount(double toAdd){
            countTextView.setText((int) (toAdd + 1));
        }
        void pointName(String name){
            nameTextView.setText(name);
        }
    }
}
