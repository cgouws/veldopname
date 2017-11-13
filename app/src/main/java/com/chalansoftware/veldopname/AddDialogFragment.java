package com.chalansoftware.veldopname;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import static android.R.layout.simple_list_item_1;

/**
 * Created by Charl Gouws on 2017/11/11.
 */

public class AddDialogFragment
        extends DialogFragment {
    List<Point> mPointsList;
    
    static AddDialogFragment newInstance(List<Point> pointList){
        AddDialogFragment addDialogFragment = new AddDialogFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList("List", (ArrayList<? extends Parcelable>) pointList);
        addDialogFragment.setArguments(args);
        return addDialogFragment;
    }

    @NonNull @Override public Dialog onCreateDialog(Bundle savedInstanceState) {
        mPointsList = getArguments().getParcelableArrayList("List");
        
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.add_dialog, null);
        
        Button addButton = view.findViewById(R.id.add_point_button);
        final EditText addEdittext = view.findViewById(R.id.add_point_edittext);
        final ListView listView = view.findViewById(R.id.added_points_listview);
        
        
        final List<String> nameArrayList = new ArrayList<>();
        
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(),
                simple_list_item_1, nameArrayList);
        listView.setAdapter(arrayAdapter);
        
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                mPointsList.add(new Point(addEdittext.getText().toString()));
                nameArrayList.add(addEdittext.getText().toString());
                arrayAdapter.notifyDataSetChanged();
                addEdittext.setText("");
            }
        });
        
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int pos, long l) {
                mPointsList.remove(pos);
                nameArrayList.remove(pos);
                arrayAdapter.notifyDataSetChanged();
                return true;
            }
        });
        
        return new AlertDialog.Builder(getActivity()).setView(view)
                .setTitle("Nuwe Telpunt")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                    }
                })
                .create();
    }
}
