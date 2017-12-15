package com.chalansoftware.veldopname;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Charl Gouws on 2017/11/11.
 * <p>
 * RecyclerView to display the points and add the ability to increment and decrement point counts.
 */

public class PointRecyclerAdapter
        extends RecyclerView.Adapter<PointRecyclerAdapter.PointViewHolder> {
    
    private List<Point> mPointsList;
    
    PointRecyclerAdapter(List<Point> listName) {
        mPointsList = listName;
    }
    
    @Override public PointViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_list_item, parent, false);
        return new PointViewHolder(view);
    }
    
    @Override public void onBindViewHolder(PointViewHolder holder, int position) {
    
        if (mPointsList.size() > 0) {
            holder.pointName(mPointsList.get(position).getName());
            holder.countSetText((mPointsList.get(position).getPointCount()));
        }
    }
    
    @Override public int getItemCount() {
        return mPointsList.size();
    }
    
    class PointViewHolder
            extends ViewHolder {
        
        TextView nameTextView;
        TextView countTextView;
        
        PointViewHolder(final View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.recyclerview_item_name_textview);
            countTextView = itemView.findViewById(R.id.recyclerview_item_count_textview);
            
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    incrementValue();
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override public boolean onLongClick(View v) {
                    decrementValue();
                    return true;
                }
            });
        }
        private void incrementValue() {
            // This method takes the adapter position, inserts it as the id of the relevant Point
            // object in mPointsList, retrieves the value inserted in it's countPoint, increments
            // that value and replaces that value with the new one.
            int position = getAdapterPosition();
            double countValue = mPointsList.get(position).getPointCount();
            if (countValue >= 0d) {
                countValue++;
            } else {
                countValue = 0d;
            }
            mPointsList.get(position).setPointCount(countValue);
            countSetText(countValue);//updates the text in the textview with the new value
            notifyItemChanged(position);//notifies the adapter of the change
        }
        private void decrementValue() {
            int position = getAdapterPosition();
            double countValue = mPointsList.get(position).getPointCount();
            if (countValue > 0d) {
                countValue--;
            } else {
                countValue = 0d;
            }
            mPointsList.get(position).setPointCount(countValue);
            countSetText(countValue);
            notifyItemChanged(position);
        }
    
        void pointName(String name) {
            nameTextView.setText(name);//keeping the working code in it's encapsulating class
        }
        void countSetText(double newValue) {
            countTextView.setText(String.valueOf(newValue));
        }
    }
}
