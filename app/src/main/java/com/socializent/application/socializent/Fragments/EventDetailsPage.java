package com.socializent.application.socializent.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.socializent.application.socializent.Controller.EventDetailsBackgroundTask;
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

    private EventDetailsBackgroundTask task;
    private View eventView;
    private TextView titleView, placeView, timeDateView, feeView, participantCountView, organizerView, descView, categoryView;
    private TextView commentView, photoView, addressView;
    private Button joinButton, goBackButton;

    static String eventTitle, tempDate, tempLat, tempLong, city, description, tempParticipantCount, category;
    static String tempFee, tempRate, photoURL, comments;
    static String dateStr, address, placeName, event_id;
    static String tempOrganizer, tempParticipants;
    static JSONArray participants;
    static JSONObject organizer;

    List<HttpCookie> cookieList;

    public static EventDetailsPage newInstance(String str, Event event) {
        EventDetailsPage fragment = new EventDetailsPage();

        if (str != null && !str.equals(""))
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
            JSONObject pl = row.getJSONObject("place");
            tempLat = pl.getString("latitude");
            tempLong = pl.getString("longitude");
            city = pl.getString("city");
            address = pl.getString("address");
            placeName = pl.getString("name");
            description = row.getString("description");
            tempParticipantCount = row.getString("participantCount");

            String temp = row.getString("category").toUpperCase();
            if (temp == "null" || temp.isEmpty() || temp == "" )
                temp = "CONFERENCE";
            temp = Normalizer.normalize(temp, Normalizer.Form.NFD);
            category = temp;

            tempFee = row.getString("fee");

            /*tempRate = row.getString("rate");
            photoURL = row.getString("photoUrl");
            comments = row.getString("comments");*/
            //tempOrganizer = row.getString("organizer");
            //organizer = new JSONObject(tempOrganizer);
            //organizer.getString("_id");

            tempParticipants = row.getString("participants");
            participants = new JSONArray(tempParticipants);

            Log.v("EVENT_DETAILS", "Object formed.");

            } catch (JSONException e) {
                Log.v("EVENT_DETAILS", "JSON error");
                e.printStackTrace();
        }
    }

    //TODO: Irem burası senin için
    private static void constructByObject(Event event){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        setDate(); //converts milliseconds to date string
        timeDateView.setText(dateStr);
        feeView = (TextView) eventView.findViewById(R.id.feeView_d);
        feeView.setText(tempFee);
        participantCountView = (TextView) eventView.findViewById(R.id.participantCountView_d);
        participantCountView.setText(tempParticipantCount);
        categoryView = (TextView) eventView.findViewById(R.id.categoryView_d);
        categoryView.setText(category);
        descView = (TextView) eventView.findViewById(R.id.descriptionView_d);
        descView.setText(description);

        joinButton = (Button)eventView.findViewById(R.id.joinEventButton);
        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (participantCountCheck()) {
                    joinEvent();
                    cookieList = msCookieManager.getCookieStore().getCookies();
                    Toast.makeText(getActivity(), "You have joined " + eventTitle + " ! ",
                            Toast.LENGTH_SHORT)
                            .show();
                }
                else {
                    Toast.makeText(getActivity(), "Participant limit is full for this event.",
                            Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });

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
        Log.v("LOCATION_V", "Going back to map...");

    }

    private void setDate(){
        long millis = Long.parseLong(tempDate);
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        dateStr = df.format(calendar.getTime());

    }

    private void joinEvent(){
        task = new EventDetailsBackgroundTask(getContext());
        task.execute(JOIN_EVENT_TAG, event_id);
    }

}
