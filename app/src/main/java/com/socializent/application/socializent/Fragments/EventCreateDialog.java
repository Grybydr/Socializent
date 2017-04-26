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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.places.Place;
import com.socializent.application.socializent.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by ZÃ¼lal BingÃ¶l on 9.04.2017.
 */

public class EventCreateDialog extends DialogFragment implements DialogInterface.OnCancelListener, DialogInterface.OnDismissListener{

    public int DEFAULT_CHECKED = 0;

    private EditText timeView, titleView, feeView, participantCountView, descriptionView;
    private BottomBarMap callerActivity;
    private String dateStr = "";
    private String myTitle, myFee, myTags, myDescription;
    private String myCategory = "";
    private int myParticipantCount;
    private static Date myDate;
    private static Place myPlace;
    private RadioGroup radioCategoryGroup;
    private RadioButton radioCategoryButton;
    private int selectedButtonId;
    private TextView categoryText;

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

        categoryText = (TextView)view.findViewById(R.id.categoryView);
        radioCategoryGroup = (RadioGroup) view.findViewById(R.id.radioGroup);
        radioCategoryGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected
                setMyCategory(checkedId);
                selectedButtonId = checkedId;
            }
        });

        descriptionView = (EditText)view.findViewById(R.id.descriptionView);

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
                    callerActivity.addEvent(myPlace, myTitle, myDate, myFee, myParticipantCount, myTags, myDescription);
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
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        dateStr = df.format(myDate);
    }

    /*
    * Updates the last values of fields after time/date picked
    */
    private void updateFields(){
        titleView.setText(myTitle);
        timeView.setText(dateStr);
        feeView.setText(myFee);
        participantCountView.setText(String.valueOf(myParticipantCount));
        radioCategoryGroup.check(selectedButtonId);
        descriptionView.setText(myDescription);
    }

    /*
    * Retrieves the last values of fields before time/date picked
    */
    private void getFields(){
        myTitle = titleView.getText().toString();
        myFee = feeView.getText().toString();
        try {
            if (!participantCountView.getText().toString().trim().equals("")) {
                myParticipantCount = Integer.parseInt(participantCountView.getText().toString());
            }
            else
                myParticipantCount = 0;
        } catch(NumberFormatException nfe) {
            Log.d("EVENT_CREATE_DIALOG", "Error in parsing: " + nfe);
        }
        setMyCategory(selectedButtonId);
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
        myFee = feeView.getText().toString();

        if (radioCategoryGroup.getCheckedRadioButtonId() != 0) {
            Log.d("EVENT_CREATE_DIALOG", "BUTTON FOUND" + radioCategoryGroup.getCheckedRadioButtonId());
            setMyCategory(selectedButtonId);
        }
        else {
            selectedButtonId = radioCategoryGroup.getCheckedRadioButtonId();
            setMyCategory(selectedButtonId);
        }

        myDescription = descriptionView.getText().toString();
        return true;
    }

    private void setMyCategory(int checkedId){
        switch (checkedId){
            case R.id.r_celebration:
                myCategory = "celebration";
                break;
            case R.id.r_conference:
                myCategory = "conference";
                break;
            case R.id.r_lecture:
                myCategory = "lecture";
                break;
            case R.id.r_movieScreen:
                myCategory = "movieScreen";
                break;
            case R.id.r_concert:
                myCategory = "concert";
                break;
            case R.id.r_travel:
                myCategory = "travel";
                break;
            case R.id.r_party:
                myCategory = "party";
                break;
            case R.id.r_study:
                myCategory = "study";
                break;
            case R.id.r_sports:
                myCategory = "sports";
                break;
            case R.id.r_other:
                myCategory = "other";
                break;
            default: myCategory = "";
        }
    }
}
