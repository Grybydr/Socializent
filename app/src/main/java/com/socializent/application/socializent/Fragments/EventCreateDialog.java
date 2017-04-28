package com.socializent.application.socializent.Fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.places.Place;
import com.socializent.application.socializent.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Created by ZÃ¼lal BingÃ¶l on 9.04.2017.
 */

public class EventCreateDialog extends DialogFragment implements DialogInterface.OnCancelListener, DialogInterface.OnDismissListener{

    final double NO_FEE_TAG = 0.0;
    final String NO_DESC_TAG = "-";

    private EditText timeView, titleView, feeView, participantCountView, descriptionView;
    private BottomBarMap callerActivity;
    private String dateStr = "";
    private String myTitle, myDescription;
    private double myFee;
    private String myCategory = "";
    private int myParticipantCount;
    private static Date myDate;
    private static Place myPlace;
    private Spinner categoryChooser;
    ArrayList<String> choices;

    static EventCreateDialog newInstance(Place place) {
        myPlace = place;
        return new EventCreateDialog();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            callerActivity = (BottomBarMap) getTargetFragment();
        } catch (Exception e) {
            Log.d("EVENT_CREATE_DIALOG", "Class cast error ");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_create_event_form, new RelativeLayout(getActivity()), false);

        TextView eventsDetailTitle = (TextView)view.findViewById(R.id.eventDetailForm);
        TextView placeTag = (TextView)view.findViewById(R.id.placeTag);
        TextView addressView = (TextView)view.findViewById(R.id.addressView);
        addressView.setText(myPlace.getName());

        TextView titleTag = (TextView)view.findViewById(R.id.titleTag);
        titleView = (EditText)view.findViewById(R.id.titleView);
        TextView feeTag = (TextView)view.findViewById(R.id.feeTag);
        feeView = (EditText)view.findViewById(R.id.feeView);
        TextView pCountTag = (TextView)view.findViewById(R.id.pCountTag);
        participantCountView = (EditText)view.findViewById(R.id.participantCountView);

        categoryChooser = (Spinner) view.findViewById(R.id.category_spinner);
        arrangeSpinner();

        descriptionView = (EditText)view.findViewById(R.id.descriptionView);

        TextView timeDateTag = (TextView)view.findViewById(R.id.timeDateTag);
        timeView = (EditText) view.findViewById(R.id.timeView);
        timeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFields();
                showDialog(getTargetRequestCode());
            }
        });
        updateFields();

        Button createEventButton = (Button)view.findViewById(R.id.createEventButton);
        createEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getFieldsWithCheck()) {
                    callerActivity.addEvent(myPlace, myTitle, myDate, myFee, myParticipantCount, myCategory, myDescription);
                    EventCreateDialog.this.dismiss();
                }
                else {
                    Toast.makeText(getActivity(), "Please fill in the required areas.",
                            Toast.LENGTH_LONG)
                            .show();
                }
            }
        });

        Button cancelButton = (Button)view.findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventCreateDialog.this.dismiss();
            }
        });

        // Build dialog
        Dialog builder = new Dialog(getActivity());
        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        builder.setContentView(view);
        return builder;
    }

    private void showDialog(int requestCode) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        // Create the fragment and show it as a dialog.
        DialogFragment newDialog = DateTimeDialog.newInstance();
        newDialog.setTargetFragment(this, requestCode);
        newDialog.show(ft, "dialog");
    }

    /*
    *   Sets the date with DateTime Picker
    */
    public void setDate(Date date){
        myDate = date;
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        dateStr = df.format(myDate);
    }

    /*
    * Updates the last values of fields after time/date picked
    */
    private void updateFields(){
        titleView.setText(myTitle);
        timeView.setText(dateStr);
        feeView.setText(String.valueOf(myFee));
        participantCountView.setText(String.valueOf(myParticipantCount));
        categoryChooser.setSelection(choices.indexOf(myCategory));
        descriptionView.setText(myDescription);
    }

    /*
    * Retrieves the last values of fields before time/date picked
    */
    private void getFields(){
        myTitle = titleView.getText().toString();
        try {
            if (!feeView.getText().toString().trim().equals("")) {
                myFee = Double.parseDouble(feeView.getText().toString());
            }
            else
                myFee = NO_FEE_TAG;
        } catch(NumberFormatException nfe) {
            Log.d("EVENT_CREATE_DIALOG", "Error in parsing: " + nfe);
        }
        try {
            if (!participantCountView.getText().toString().trim().equals("")) {
                myParticipantCount = Integer.parseInt(participantCountView.getText().toString());
            }
            else
                myParticipantCount = 0;
        } catch(NumberFormatException nfe) {
            Log.d("EVENT_CREATE_DIALOG", "Error in parsing: " + nfe);
        }
        myCategory = categoryChooser.getSelectedItem().toString();
        myDescription = descriptionView.getText().toString();
    }

    /*
    * Retrieves the last values of fields before create button is hit
    */
    private boolean getFieldsWithCheck(){

        if(titleView.getText().toString().trim().equals("")){
            titleView.setError( "Title is required!" );
            return false;
        }else{
            myTitle = titleView.getText().toString();
        }
        if( timeView.getText().toString().trim().equals("")){
            timeView.setError( "Time/Date is required!" );
            return false;
        }else{
            dateStr = timeView.getText().toString();
        }
        try {
            if( participantCountView.getText().toString().trim().equals("") || participantCountView.getText().toString().trim() == "0"){
                participantCountView.setError( "Participant Count is required!" );
                return false;
            }else{
                myParticipantCount = Integer.parseInt(participantCountView.getText().toString());
            }
        } catch(NumberFormatException nfe) {
            Log.d("EVENT_CREATE_DIALOG", "Error in parsing: " + nfe);
            Toast.makeText(getActivity(), "Please enter participant count again.",
                    Toast.LENGTH_LONG)
                    .show();
            return false;
        }
        try {
            if (!feeView.getText().toString().trim().equals("")) {
                myFee = Double.parseDouble(feeView.getText().toString());
            }
            else
                myFee = NO_FEE_TAG;
        } catch(NumberFormatException nfe) {
            Log.d("EVENT_CREATE_DIALOG", "Error in parsing: " + nfe);
        }
        if(!myCategory.equals("Category")){
            myCategory = categoryChooser.getSelectedItem().toString();
        }
        else {
            TextView errorText = (TextView)categoryChooser.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);
            errorText.setText("Category is required!");
            return false;
        }

        if(descriptionView.getText().toString().trim().equals("")){
            myDescription = NO_DESC_TAG;
        }else{
            myDescription = descriptionView.getText().toString();
        }
        return true;
    }

    private void arrangeSpinner(){

        choices = new ArrayList<String>();
        choices.add("Category");
        choices.add("Celebration");
        choices.add("Conference");
        choices.add("Lecture");
        choices.add("Movie Screening");
        choices.add("Concert");
        choices.add("Travel");
        choices.add("Party");
        choices.add("Study");
        choices.add("Sports");

        categoryChooser.setAdapter(
                new ArrayAdapter<>(
                        getActivity(),
                        android.R.layout.simple_spinner_dropdown_item,
                        choices));
        categoryChooser.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                myCategory = (String) categoryChooser.getAdapter().getItem(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        // Select initial choice if neither preferred.
        if (myCategory == null) {
            categoryChooser.setSelection(choices.indexOf("Category"));
        } else {
            categoryChooser.setSelection(choices.indexOf(myCategory));
        }
    }
}
