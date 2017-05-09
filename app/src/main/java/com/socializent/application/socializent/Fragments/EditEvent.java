package com.socializent.application.socializent.Fragments;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.socializent.application.socializent.Controller.EventBackgroundTask;
import com.socializent.application.socializent.Controller.EventDetailsBackgroundTask;
import com.socializent.application.socializent.Controller.PersonBackgroundTask;
import com.socializent.application.socializent.Modal.Event;
import com.socializent.application.socializent.Modal.EventTypes;
import com.socializent.application.socializent.R;
import com.socializent.application.socializent.Template;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.net.HttpCookie;
import java.text.DateFormat;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import static com.socializent.application.socializent.Controller.PersonBackgroundTask.msCookieManager;

/**
 * Created by Zülal Bingöl on 8.05.2017.
 */

public class EditEvent extends Fragment {

    final static String GET_EVENT_DETAILS = "getEventDetails";
    final double NO_FEE_TAG = 0.0;
    final String NO_DESC_TAG = "-";
    private static final int REQUEST_PLACE_PICKER = 1;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TURKEY_TAG = "Turkey";
    final static String GET_ALL_EVENTS_OPTION = "2";
    final static String EDIT_EVENT = "editEvent";


    private View eventView;
    private EditText titleView, timeDateView, feeView, participantCountView, descView;
    private TextView commentView, photoView, placeView;
    private Button cancelButton, saveChangesButton, changePlaceButton;
    private Spinner categoryChooser;
    ArrayList<String> choices;

    private static String event_id;
    String eventTitle, tempDate, tempLat, tempLong, city, description, tempParticipantCount, category;
    int participantCount;
    double fee;
    String tempFee, tempRate, photoURL, comments;
    String dateStr, myAddress, placeName, organizer;
    private Place myPlace;
    String tempParticipants;
    static JSONArray participants;
    JSONObject currentUser;
    private String userEvents = "", currentUserID = "";
    List<HttpCookie> cookieList;
    PersonBackgroundTask personTask;
    EventBackgroundTask eventBTask;
    private EventDetailsBackgroundTask editTask, task;

    public static EditEvent newInstance(String id) {
        EditEvent fragment = new EditEvent();
        event_id = id;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //cookieList = msCookieManager.getCookieStore().getCookies();
        task = new EventDetailsBackgroundTask(getContext());
        String eventStr = "";
        try {
            eventStr = task.execute(GET_EVENT_DETAILS, event_id).get();
            try {
                JSONObject eventObject = new JSONObject(eventStr);

                eventTitle = eventObject.getString("name");
                tempDate = eventObject.getString("date");
                updateDate(); //converts milliseconds to date string
                JSONObject pl = eventObject.getJSONObject("place");
                tempLat = pl.getString("latitude");
                tempLong = pl.getString("longitude");
                city = pl.getString("city");
                myAddress = pl.getString("address");
                placeName = pl.getString("name");
                description = eventObject.getString("description");
                tempParticipantCount = eventObject.getString("participantCount");
                organizer = eventObject.getString("organizer");

                String temp = eventObject.getString("category").toUpperCase();
                if (temp == "null" || temp.isEmpty() || temp == "" )
                    temp = "CONFERENCE";
                temp = Normalizer.normalize(temp, Normalizer.Form.NFD);
                category = temp;

                tempFee = eventObject.getString("fee");

                /*tempRate = eventObject.getString("rate");
                photoURL = eventObject.getString("photoUrl");
                comments = eventObject.getString("comments");*/

                Log.v("EVENT_EDIT", "Object formed.");

            } catch (JSONException e) {
                e.printStackTrace();
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        eventView = inflater.inflate(R.layout.edit_event, container, false);

        titleView = (EditText) eventView.findViewById(R.id.titleView);
        titleView.setText(eventTitle);
        placeView = (TextView) eventView.findViewById(R.id.placeView);
        placeView.setText(placeName);
        timeDateView = (EditText) eventView.findViewById(R.id.timeView);
        timeDateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(getTargetRequestCode());
            }
        });
        timeDateView.setText(dateStr);

        feeView = (EditText) eventView.findViewById(R.id.feeView);
        feeView.setText(tempFee);
        participantCountView = (EditText) eventView.findViewById(R.id.participantCountView);
        participantCountView.setText(tempParticipantCount);

        categoryChooser = (Spinner) eventView.findViewById(R.id.category_spinner);
        arrangeSpinner();

        descView = (EditText) eventView.findViewById(R.id.descriptionView);
        descView.setText(description);

        changePlaceButton = (Button)eventView.findViewById(R.id.changePlaceButton);
        changePlaceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initializePlacePicker();
            }
        });

        cancelButton = (Button)eventView.findViewById(R.id.calcelButton_edit);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goBackToMap();
            }
        });

        saveChangesButton = (Button)eventView.findViewById(R.id.saveChangesButton_edit);
        saveChangesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getFieldsWithCheck()){
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case DialogInterface.BUTTON_POSITIVE:
                                    editEvent();
                                    eventBTask = new EventBackgroundTask();
                                    eventBTask.execute(GET_ALL_EVENTS_OPTION);
                                    Toast.makeText(getActivity(), "You have edited \"" + eventTitle + "\" ",
                                            Toast.LENGTH_SHORT)
                                            .show();
                                    goBackToMap();
                                    break;
                                case DialogInterface.BUTTON_NEGATIVE:
                                    break;
                            }
                        }
                    };
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage("Are you sure to save changes on \"" + eventTitle + "\" ?").setPositiveButton("Yes", dialogClickListener)
                            .setNegativeButton("No", dialogClickListener).show();
                }
                else {
                    Toast.makeText(getActivity(), "Please fill in the required areas.",
                            Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });

        return eventView;
    }

    private void showDialog(int requestCode) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        // Create the fragment and show it as a dialog.
        DialogFragment newDialog = EditPickers.newInstance();
        newDialog.setTargetFragment(this, requestCode);
        newDialog.show(ft, "dialog");
    }

    /*
    *   Opens PlacePicker
    */
    private void initializePlacePicker() {

        try {
            PlacePicker.IntentBuilder intentBuilder = new PlacePicker.IntentBuilder();
            Intent intent = intentBuilder.build(getActivity());
            // Start the Intent by requesting a result, identified by a request code.
            startActivityForResult(intent, REQUEST_PLACE_PICKER);
        } catch (GooglePlayServicesRepairableException e) {
            GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
            apiAvailability.getErrorDialog(getActivity(), e.getConnectionStatusCode(), PLAY_SERVICES_RESOLUTION_REQUEST).show();
        } catch (GooglePlayServicesNotAvailableException e) {
            Toast.makeText(getActivity(), "Google Play Services is not available.",
                    Toast.LENGTH_LONG)
                    .show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_PLACE_PICKER) {

            if (resultCode == getActivity().RESULT_OK) {
                /* User has picked a place, extract data.
                   Data is extracted from the returned intent by retrieving a Place object from
                   the PlacePicker.
                 */
                final Place place = PlacePicker.getPlace(getContext(), data);

                /* A Place object contains details about that place, such as its name, address
                and phone number. Extract the name, address, phone number, place ID and place types.
                 */
                placeName = place.getName().toString();
                final String placeId = place.getId();

                myPlace = place;
                city = setCity(myPlace);
                placeView.setText(placeName);

            } else {
                Log.v("EDIT_EVENT", "Error in PlacePicker ResultCode: " + resultCode);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    /*
    *   Retrieves city
    */
    private String setCity(Place place){
        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
        List<Address> addresses = null;
        String city = "";
        try {
            addresses = geocoder.getFromLocation(place.getLatLng().latitude, place.getLatLng().longitude, 1);
            Address address = addresses.get(0);
            for (int i = 0; i <= address.getMaxAddressLineIndex(); i++){
                myAddress = myAddress + " " + address.getAddressLine(i);
            }

            if ((address.getAddressLine(address.getMaxAddressLineIndex()).toString()).equals(TURKEY_TAG)){
                String str = (address.getAddressLine(address.getMaxAddressLineIndex()-1).toString());
                int index = str.indexOf('/');
                city = str.substring(index+1, str.length());
            }
            else {
                if (addresses.get(0).getLocality() != null)
                    city = addresses.get(0).getLocality().toString();
                else if (addresses.get(0).getSubAdminArea() != null)
                    city = addresses.get(0).getSubAdminArea().toString();
                else
                    city = "";
            }

        } catch (IOException e) {
            Log.v("EDIT_EVENT", "Cannot run Geocoder");
            e.printStackTrace();
        }

        if(city.equals("") || city.equals("null") || city == null){
            city = place.getName().toString();
        }
        return city;
    }


    private void goBackToMap(){
        Intent intent = new Intent(getActivity(), Template.class);
        startActivity(intent);
        Log.v("EDIT_EVENT", "Going back to map...");
    }


    private void editEvent(){
        editTask = new EventDetailsBackgroundTask(getContext());

        if (myPlace != null){
            LatLng latLng = myPlace.getLatLng();

            String longitude = String.valueOf(latLng.longitude);
            String latitude = String.valueOf(latLng.latitude);
            //TODO: uncomment these for device usage:
            tempLong = longitude.replace(",", ".");
            tempLat = latitude.replace(",", ".");
            city = setCity(myPlace);
        }

        String pCount = String.valueOf(participantCount);
        String feeConverted = String.valueOf(fee);

        editTask.execute(EDIT_EVENT, eventTitle, dateStr, city, tempLong, tempLat, myAddress, placeName, pCount, category, description, feeConverted, organizer, event_id);

    }

    /*
    * Retrieves the last values of fields before create button is hit
    */
    private boolean getFieldsWithCheck(){

        if(titleView.getText().toString().trim().equals("")){
            titleView.setError( "Title is required!" );
            return false;
        }else{
            eventTitle = titleView.getText().toString();
        }
        if( timeDateView.getText().toString().trim().equals("")){
            timeDateView.setError( "Time/Date is required!" );
            return false;
        }else{
            dateStr = timeDateView.getText().toString();
        }
        try {
            if( participantCountView.getText().toString().trim().equals("") || participantCountView.getText().toString().trim() == "0"){
                participantCountView.setError( "Participant Count is required!" );
                return false;
            }else{
                participantCount = Integer.parseInt(participantCountView.getText().toString());
            }
        } catch(NumberFormatException nfe) {
            Log.v("EVENT_EDIT", "Error in parsing: " + nfe);
            Toast.makeText(getActivity(), "Please enter participant count again.",
                    Toast.LENGTH_LONG)
                    .show();
            return false;
        }
        try {
            if (!feeView.getText().toString().trim().equals("")) {
                fee = Double.parseDouble(feeView.getText().toString());
            }
            else
                fee = NO_FEE_TAG;
        } catch(NumberFormatException nfe) {
            Log.v("EVENT_EDIT", "Error in parsing: " + nfe);
        }
        if(!category.equals("Category")){
            category = categoryChooser.getSelectedItem().toString().toUpperCase();
        }
        else {
            TextView errorText = (TextView)categoryChooser.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);
            errorText.setText("Category is required!");
            return false;
        }
        if(descView.getText().toString().trim().equals("")){
            description = NO_DESC_TAG;
        }else{
            description = descView.getText().toString();
        }
        return true;
    }

    public void updateDate(){
        long millis = Long.parseLong(tempDate);
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        dateStr = df.format(calendar.getTime());
    }

    public void setDate(Date date){
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        dateStr = df.format(date);
        timeDateView.setText(dateStr);
    }

    private void arrangeSpinner(){

        choices = new ArrayList<String>();
        choices.add("Category");
        choices.add(String.valueOf(EventTypes.BIRTHDAY).toLowerCase());
        choices.add(String.valueOf(EventTypes.CELEBRATION).toLowerCase());
        choices.add(String.valueOf(EventTypes.CONCERT).toLowerCase());
        choices.add(String.valueOf(EventTypes.CONFERENCE).toLowerCase());
        choices.add(String.valueOf(EventTypes.LECTURE).toLowerCase());
        choices.add(String.valueOf(EventTypes.MEETING).toLowerCase());
        choices.add(String.valueOf(EventTypes.MOVIE).toLowerCase());
        choices.add(String.valueOf(EventTypes.PARTY).toLowerCase());
        choices.add(String.valueOf(EventTypes.SPORTS).toLowerCase());
        choices.add(String.valueOf(EventTypes.STUDY).toLowerCase());
        choices.add(String.valueOf(EventTypes.TRAVEL).toLowerCase());

        categoryChooser.setAdapter(
                new ArrayAdapter<>(
                        getActivity(),
                        android.R.layout.simple_spinner_dropdown_item,
                        choices));
        categoryChooser.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category = (String) categoryChooser.getAdapter().getItem(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        // Select initial choice if neither preferred.
        if (category == null) {
            categoryChooser.setSelection(choices.indexOf("Category"));
        } else {
            categoryChooser.setSelection(choices.indexOf(category));
        }
    }
}
