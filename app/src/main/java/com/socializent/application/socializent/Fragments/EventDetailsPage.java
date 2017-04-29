package com.socializent.application.socializent.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.socializent.application.socializent.R;

/**
 * Created by Zülal Bingöl on 28.04.2017.
 */

public class EventDetailsPage extends Fragment {
    View eventDetails;

    public EventDetailsPage() {
        // Required empty public constructor
    }

    public static EventDetailsPage newInstance() {
        return new EventDetailsPage();
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        eventDetails = inflater.inflate(R.layout.event_details_page, container, false);

        return eventDetails;
    }


}
