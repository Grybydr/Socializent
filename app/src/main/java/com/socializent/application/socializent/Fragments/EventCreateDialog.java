package com.socializent.application.socializent.Fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.location.places.Place;
import com.socializent.application.socializent.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Zülal Bingöl on 9.04.2017.
 */

public class EventCreateDialog extends DialogFragment implements DialogInterface.OnCancelListener, DialogInterface.OnDismissListener{

    private EditText timeView, titleView, feeView, tagsView, participantCountView, descriptionView;
    private BottomBarMap callerActivity;

    private String dateStr = "";
    private String myTitle, myFee, myTags, myDescription;
    private int myParticipantCount;
    private static Date myDate;
    private static Place myPlace;

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

        titleView = (EditText)view.findViewById(R.id.titleView);
        feeView = (EditText)view.findViewById(R.id.feeView);
        participantCountView = (EditText)view.findViewById(R.id.participantCountView);
        tagsView = (EditText)view.findViewById(R.id.tagsView);
        descriptionView = (EditText)view.findViewById(R.id.descriptionView);

        timeView = (EditText) view.findViewById(R.id.timeView);
        timeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFields();
                Log.d("EVENT_CREATE_DIALOG", "timeView clicked");
                showDialog(getTargetRequestCode());
            }
        });
        updateFields();

        Button createEventButton = (Button)view.findViewById(R.id.createEventButton);
        createEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myTitle = titleView.getText().toString();
                myFee = feeView.getText().toString();
                myParticipantCount = Integer.parseInt(participantCountView.getText().toString());
                myTags = tagsView.getText().toString();
                myDescription = descriptionView.getText().toString();

                callerActivity.addEvent(myPlace, myTitle, myDate, myFee, myParticipantCount, myTags, myDescription);
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
        Log.d("EVENT_CREATE_DIALOG", "Date/Time Pickers shown");

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
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        //String dateStr = df.format(myDate);
        dateStr = df.format(myDate);
    }

    /*
    * Updates the last values of fields after time/date picked
    */
    private void updateFields(){
        titleView.setText(myTitle);
        feeView.setText(myFee);
        participantCountView.setText(String.valueOf(myParticipantCount));
        tagsView.setText(myTags);
        descriptionView.setText(myDescription);
        timeView.setText(dateStr);
    }

    /*
    * Retrieves the last values of fields before time/date picked
    */
    private void getFields(){
        myTitle = titleView.getText().toString();
        myFee = feeView.getText().toString();
        try {
            myParticipantCount = Integer.parseInt(participantCountView.getText().toString());
        } catch(NumberFormatException nfe) {
            Log.d("EVENT_CREATE_DIALOG", "Error in parsing: " + nfe);
        }
        myTags = tagsView.getText().toString();
        myDescription = descriptionView.getText().toString();
    }
}
