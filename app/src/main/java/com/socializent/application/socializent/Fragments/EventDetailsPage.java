package com.socializent.application.socializent.Fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.socializent.application.socializent.Controller.EventDetailsBackgroundTask;
import com.socializent.application.socializent.Controller.PersonBackgroundTask;
import com.socializent.application.socializent.Modal.Event;
import com.socializent.application.socializent.R;
import com.socializent.application.socializent.Template;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpCookie;
import java.text.DateFormat;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.socializent.application.socializent.Controller.PersonBackgroundTask.msCookieManager;

/**
 * Created by Zülal Bingöl on 28.04.2017.
 */

public class EventDetailsPage extends Fragment {

    final static String JOIN_EVENT_TAG = "joinEvent";
    final static String GET_USER = "3";
    final static String LEAVE_EVENT = "leaveEvent";
    final static String DELETE_EVENT = "deleteEvent";
    final static String GET_ALL_EVENTS_OPTION = "2";

    private EventDetailsBackgroundTask task;
    private View eventView;
    private TextView titleView, placeView, timeDateView, feeView, participantCountView, organizerView, descView, categoryView;
    private TextView commentView, photoView, addressView;
    private Button joinButton, goBackButton, editButton, deleteButton,leaveButton;

    static String eventTitle, tempDate, tempLat, tempLong, city, description, tempParticipantCount, category;
    static String tempFee, tempRate, photoURL, comments;
    static String dateStr, address, placeName, event_id;
    static String tempParticipants;
    static String organizer = "";
    static JSONArray participants;
    JSONObject currentUser;
    private String userEvents = "", currentUserID = "";
    List<HttpCookie> cookieList;
    PersonBackgroundTask personTask;

    public static EventDetailsPage newInstance(String str, Event event) {
        EventDetailsPage fragment = new EventDetailsPage();

        if ((str != null || !str.equals("")) && event == null)
            constructByString(str);
        else if ((str.equals("") || str == null) && event != null)
            constructByObject(event);
        else {
            Log.e("EVENT_DETAILS", "EventsDetailPage cannot be constructed.");
        }

        return fragment;
    }

    private static void constructByString(String eventStr){

        //TODO: modify these for multiple events

        try {

            JSONObject row = new JSONObject(eventStr);

            event_id = row.getString("_id");
            eventTitle = row.getString("name");
            tempDate = row.getString("date");
            setDate(); //converts milliseconds to date string
            JSONObject pl = row.getJSONObject("place");
            tempLat = pl.getString("latitude");
            tempLong = pl.getString("longitude");
            city = pl.getString("city");
            address = pl.getString("address");
            placeName = pl.getString("name");
            description = row.getString("description");
            tempParticipantCount = row.getString("participantCount");
            organizer = row.getString("organizer");

            String temp = row.getString("category").toUpperCase();
            if (temp == "null" || temp.isEmpty() || temp == "" )
                temp = "CONFERENCE";
            temp = Normalizer.normalize(temp, Normalizer.Form.NFD);
            category = temp;

            tempFee = row.getString("fee");

            /*tempRate = row.getString("rate");
            photoURL = row.getString("photoUrl");
            comments = row.getString("comments");*/

            tempParticipants = row.getString("participants");
            participants = new JSONArray(tempParticipants);

            Log.v("EVENT_DETAILS", "Object formed.");

            } catch (JSONException e) {
                Log.v("EVENT_DETAILS", "JSON error");
                e.printStackTrace();
        }
    }


    private static void constructByObject(Event event){
        eventTitle = event.getName();
        placeName = event.getPlaceName();
        address = event.getAddress();
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(event.getDate());
        dateStr = df.format(calendar.getTime());
        tempFee = String.valueOf(event.getFee());
        tempParticipantCount =  String.valueOf(event.getParticipantCount());
        category = event.getEventType().toString();
        description = event.getDescription();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cookieList = msCookieManager.getCookieStore().getCookies();
        task = new EventDetailsBackgroundTask(getContext());
        personTask = new PersonBackgroundTask(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        eventView = inflater.inflate(R.layout.event_details_page, container, false);
        titleView = (TextView) eventView.findViewById(R.id.titleView_d);
        titleView.setText(eventTitle);
        placeView = (TextView) eventView.findViewById(R.id.placeView);
        placeView.setText(placeName);
        addressView = (TextView) eventView.findViewById(R.id.addressView_d);
        addressView.setText(address);
        timeDateView = (TextView) eventView.findViewById(R.id.timeDateView);

        timeDateView.setText(dateStr);
        feeView = (TextView) eventView.findViewById(R.id.feeView_d);
        feeView.setText(tempFee);
        participantCountView = (TextView) eventView.findViewById(R.id.participantCountView_d);
        participantCountView.setText(tempParticipantCount);
        categoryView = (TextView) eventView.findViewById(R.id.categoryView_d);
        categoryView.setText(category);
        descView = (TextView) eventView.findViewById(R.id.descriptionView_d);
        descView.setText(description);

        currentUserID = getUserId();

        joinButton = (Button)eventView.findViewById(R.id.joinEventButton);
        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (participantCountCheck()) {
                    joinEvent();
                    Toast.makeText(getActivity(), "You have joined " + eventTitle + " ! ",
                            Toast.LENGTH_SHORT)
                            .show();
                    joinButton.setVisibility(View.GONE);
                    leaveButton.setVisibility(View.VISIBLE);
                    personTask.execute(GET_USER);
                }
                else {
                    Toast.makeText(getActivity(), "Participant limit is full for this event.",
                            Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });
        leaveButton = (Button)eventView.findViewById(R.id.leaveEventButton);
        leaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                leaveEvent();
                Toast.makeText(getActivity(), "You have left " + eventTitle + " ! ",
                        Toast.LENGTH_SHORT)
                        .show();
                joinButton.setVisibility(View.VISIBLE);
                leaveButton.setVisibility(View.GONE);
                personTask.execute(GET_USER);
            }
        });
        editButton = (Button)eventView.findViewById(R.id.editEventButton);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goBackToMap();
            }
        });

        deleteButton = (Button)eventView.findViewById(R.id.deleteEventButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                deleteEvent();
                                Toast.makeText(getActivity(), "You have deleted \"" + eventTitle + "\" :( ",
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
                builder.setMessage("Are you sure to delete \"" + eventTitle + "\" ?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
            }
        });

        if(isOrganizer()){
            editButton.setVisibility(View.VISIBLE);
            deleteButton.setVisibility(View.VISIBLE);
            joinButton.setVisibility(View.GONE);
            leaveButton.setVisibility(View.GONE);
        }
        else {
            editButton.setVisibility(View.GONE);
            deleteButton.setVisibility(View.GONE);
            if(isJoined()){
                joinButton.setVisibility(View.GONE);
                leaveButton.setVisibility(View.VISIBLE);
            }
            else {
                joinButton.setVisibility(View.VISIBLE);
                leaveButton.setVisibility(View.GONE);
            }
        }

        goBackButton = (Button)eventView.findViewById(R.id.goBackButton);
        goBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goBackToMap();
            }
        });

        return eventView;
    }

    private boolean participantCountCheck(){
        int num = participants.length();
        int participantCount = Integer.parseInt(tempParticipantCount);

        if(num == participantCount){
            return false;
        }
        else {
            return true;
        }
    }

    private void goBackToMap(){

        Intent intent = new Intent(getActivity(), Template.class);
        startActivity(intent);
        Log.v("EVENT_DETAILS", "Going back to map...");

    }

    private static void setDate(){
        long millis = Long.parseLong(tempDate);
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        dateStr = df.format(calendar.getTime());
    }

    private void joinEvent(){
        task.execute(JOIN_EVENT_TAG, event_id);
    }

    private boolean isOrganizer(){
        if(!organizer.trim().equals(currentUserID.trim()) || currentUserID.equals("") || organizer.equals(""))
            return false;
        else
            return true;
    }

    private String getUserId(){
        String result = "";
        String str = "";
        if(cookieList.size() != 0){
            for (int i = 0; i < cookieList.size(); i++) {
                if (cookieList.get(i).getName().equals("userProfile")){
                    str = cookieList.get(i).getValue();
                }
                else if(cookieList.get(i).getName().equals("userEvents")){
                    userEvents = cookieList.get(i).getValue();
                    break;
                }
            }
            if(!str.equals("")){
                try {
                    currentUser = new JSONObject(str);
                    result = currentUser.getString("_id");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else{
                Log.e("EVENT_DETAILS", "JSON organizerId extraction error");
            }
        }
        else {
            Log.v("EVENT_DETAILS", "User Profile cannot be extracted");
        }
        return result;
    }

    private boolean isJoined(){
        if(!userEvents.equals("")){
            try {
                JSONArray userEventsA = new JSONArray(userEvents);

                for(int i = 0; i < userEventsA.length(); i++){
                    JSONObject row = userEventsA.getJSONObject(i);

                    //Retrieving event details
                    String temp_id = row.getString("_id");

                    if(event_id.trim().equals(temp_id.trim()))
                        return true;
                }

            } catch (JSONException e) {
                Log.v("EVENT_DETAILS", "User Events cannot be extracted");
                e.printStackTrace();
            }
        }
        else{
            Log.d("EVENT_DETAILS", "UserEvents null error");
        }
        return false;
    }

    private void leaveEvent(){
        task.execute(LEAVE_EVENT, event_id);
    }

    public void deleteEvent(){
        task.execute(DELETE_EVENT, event_id);
    }

}
