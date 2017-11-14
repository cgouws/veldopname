package com.chalansoftware.veldopname;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import static android.R.layout.simple_list_item_1;

/**
 * Created by Charl Gouws on 2017/11/11.
 *
 * Dialog to allow the user to add new points.
 */

public class DialogAdd
        extends DialogFragment {
    
    List<Point> mPointsList;
    
    // Returns a new DialogAdd taking as argument the ArrayList passed in when called
    static DialogAdd newInstance(ArrayList<Point> pointList) {
        DialogAdd dialogAdd = new DialogAdd();
        Bundle args = new Bundle();
        args.putParcelableArrayList("List", pointList);
        dialogAdd.setArguments(args);
        return dialogAdd;
    }

    @NonNull @Override public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (getArguments() != null) {
            mPointsList = getArguments().getParcelableArrayList("List");
        }
        
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.add_dialog, null);
        
        Button addButton = view.findViewById(R.id.add_point_button);
        Button finishedButton = view.findViewById(R.id.finished_button);
        final EditText addEdittext = view.findViewById(R.id.add_point_edittext);
        final ListView listView = view.findViewById(R.id.added_points_listview);
        
        // For displaying the names in the dialog's list
        final List<String> nameArrayList = new ArrayList<>();
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(),
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
        finishedButton.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                dismiss();
            }
        });
        
        return new AlertDialog.Builder(getActivity()).setView(view)
                .setTitle("Nuwe Telpunt")
                .create();
    }
}
