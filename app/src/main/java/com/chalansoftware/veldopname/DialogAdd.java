package com.chalansoftware.veldopname;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
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
    
        final ImageButton addButton = view.findViewById(R.id.add_point_button);
        disableButton(addButton);
        ImageButton finishedButton = view.findViewById(R.id.finished_button);
        final EditText addEdittext = view.findViewById(R.id.add_point_edittext);
        addEdittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count > 0) {
                    enableButton(addButton);
                } else {
                    disableButton(addButton);
                }
            
            }
            @Override public void afterTextChanged(Editable s) {
            }
        });
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
                disableButton(addButton);
            }
        });
        finishedButton.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                dismiss();
            }
        });
    
        return new AlertDialog.Builder(getActivity(), R.style.MyDialogTheme).setView(view)
                .setTitle("Nuwe Telpunt")
                .create();
    }
    private void disableButton(ImageButton imageButton) {
        imageButton.setEnabled(false);
        DrawableCompat.setTint(imageButton.getDrawable(),
                ContextCompat.getColor(getContext(), R.color.disabledImage));
    }
    private void enableButton(ImageButton imageButton) {
        imageButton.setEnabled(true);
        DrawableCompat.setTint(imageButton.getDrawable(),
                ContextCompat.getColor(getContext(), R.color.enabledImage));
    }
}
