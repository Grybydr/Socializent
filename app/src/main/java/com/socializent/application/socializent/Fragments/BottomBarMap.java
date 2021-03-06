package com.socializent.application.socializent.Fragments;

/**
 * Created by Irem on 13.3.2017.
 * Edited by Zulal :)
 */

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.socializent.application.socializent.Controller.EventBackgroundTask;
import com.socializent.application.socializent.Controller.EventDetailsBackgroundTask;
import com.socializent.application.socializent.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import static com.socializent.application.socializent.Controller.PersonBackgroundTask.msCookieManager;

public class BottomBarMap extends Fragment implements OnMapReadyCallback, LocationListener,  DialogInterface.OnCancelListener, DialogInterface.OnDismissListener {

    //CONSTANTS
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private static final int REQUEST_PLACE_PICKER = 1;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final int DEFAULT_ZOOM = 17;
    private static final int DEFAULT_BEARING = 90;
    private static final int DEFAULT_TILT = 0;
    private static final long WEEK_IN_MILLIS = 1296000000;
    private static final String TURKEY_TAG = "Turkey";
    final static String GET_ALL_EVENTS_OPTION = "2";
    static final float COORDINATE_OFFSET = 0.00004f;
    final int MAX_MARKER_COUNT = 3;

    private final LatLng defaultLocation = new LatLng(39.868010, 32.748823); //Bilkent's Coordinates

    //VARIABLES
    private GoogleMap myGoogleMap;
    private UiSettings myUiSettings;
    private FloatingActionButton addEventButton, refreshFab;
    private LocationManager locationManager;
    private View mapView;
    private String myAddress = "";
    private String userEvents = "";

    EventDetailsBackgroundTask s_task, marker_task;
    List<HttpCookie> cookieList;
    JSONObject user;
    String userStr = "";

    public static BottomBarMap newInstance() {
        BottomBarMap fragment = new BottomBarMap();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        locationManager = (LocationManager) getActivity().getSystemService(getContext().LOCATION_SERVICE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mapView = inflater.inflate(R.layout.bottom_bar_map_fragment, container, false);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapView);

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            PermissionUtils.requestPermission((AppCompatActivity) getActivity(), LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, false);
            Log.v("LOCATION_V", "Permission conditions not satisfied for My Location");

            Toast.makeText(getContext(), R.string.map_loc_warning, Toast.LENGTH_LONG).show();
            myGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, DEFAULT_ZOOM));

        }

        //locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME, MIN_DISTANCE, this);
        mapFragment.getMapAsync(this);

        addEventButton = (FloatingActionButton) mapView.findViewById(R.id.addEventFab);
        addEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initializePlacePicker();
            }
        });

        refreshFab = (FloatingActionButton) mapView.findViewById(R.id.refreshFab);
        refreshFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                updateMyLocation();
            }
        });

        Toast.makeText(getContext(), R.string.map_gps_warning, Toast.LENGTH_SHORT).show();

        return mapView;
    }

    /*
    * Gets current events from database
    */
    private void retrieveEvents(){

        String events = "";
        for (int i = cookieList.size() - 1; i >= 0; i--) {
            if (cookieList.get(i).getName().equals("allEvents")){
                events = cookieList.get(i).getValue();
            }
            else if (cookieList.get(i).getName().equals("userEvents")){
                userEvents = cookieList.get(i).getValue();
            }
            else if (cookieList.get(i).getName().equals("user")){
                userStr = cookieList.get(i).getValue();
            }
        }
        try {
            String tempLat, tempLong, eventID, eventTitle, dateStr;
            JSONArray eventsArray = new JSONArray(events);

            for (int i = 0; i < eventsArray.length(); i++) {

                JSONObject row = eventsArray.getJSONObject(i);

                //Retrieving event details
                eventTitle = row.getString("name");
                dateStr = row.getString("date");
                eventID = row.getString("_id");
                long event_millis = Long.parseLong(dateStr);
                Date currentDate = new Date();
                long c_dateInMiliseconds = currentDate.getTime();
                if (event_millis > c_dateInMiliseconds + WEEK_IN_MILLIS)
                    continue;
                else {

                    DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(event_millis);
                    dateStr = df.format(calendar.getTime());

                    if (eventTitle == "null" || eventTitle.isEmpty() || eventTitle == "" )
                        eventTitle = "Event";

                    JSONObject pl = row.getJSONObject("place");
                    tempLat = pl.getString("latitude");
                    tempLong = pl.getString("longitude");

                    if ((tempLat != "null" && !tempLat.isEmpty()) && (tempLong != "null" && !tempLong.isEmpty())) {

                        //TODO: filtering: if(Double.parseDouble(tempTime) + )
                        double t_lat = Double.parseDouble(tempLat);
                        double t_longi = Double.parseDouble(tempLong);

                        //TODO: comment these for device usage
                    /*DecimalFormat df = new DecimalFormat("##,######", new DecimalFormatSymbols(Locale.FRANCE));
                    Double lat = Double.valueOf(df.format(t_lat));
                    Double longi = Double.valueOf(df.format(t_longi));
                    Log.v("LOCATION_V", "lat / long" + lat + " " + longi +"");*/

                        if(isJoined(eventID)) {
                            myGoogleMap.addMarker(new MarkerOptions().position(new LatLng(t_lat, t_longi))
                                    .title(eventTitle)
                                    .snippet(dateStr)
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                        }
                        else if (isOrganizer(eventID)){
                            myGoogleMap.addMarker(new MarkerOptions().position(new LatLng(t_lat, t_longi))
                                    .title(eventTitle)
                                    .snippet(dateStr)
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
                        }
                        else {
                            myGoogleMap.addMarker(new MarkerOptions().position(new LatLng(t_lat, t_longi))
                                    .title(eventTitle)
                                    .snippet(dateStr)
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                        }
                    }
                    else {
                        Log.v("LOCATION_V", "Latitude or Longitude is NULL");
                    }
                }
                }
        } catch (JSONException e) {
            Log.e("LOCATION_V", "JSON Error in retrieving events");
            e.printStackTrace();
        }

        return;
    }
    /*
    *   Adds some features on the map once the map becomes ready
    */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        myGoogleMap = googleMap;
        updateMyLocation();
        myUiSettings = myGoogleMap.getUiSettings();
        myUiSettings.setZoomControlsEnabled(true);
        myUiSettings.setCompassEnabled(true);
        myUiSettings.setZoomGesturesEnabled(true);
        myUiSettings.setScrollGesturesEnabled(true);

        myGoogleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                marker.showInfoWindow();
                loadTargetEvent(marker);
                return true;
            }
        });
        myGoogleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                String eventStr = "";
                eventStr = getTargetEvent();
                if(!eventStr.equals("")){
                    //Log.v("LOCATION_V", "got the str: " + eventStr);
                    Log.v("LOCATION_V", "Event Details formed");
                    goToEventDetails(eventStr);
                }
                else
                    Log.v("LOCATION_V", "EMPTY eventstr");
            }
        });
    }

    private boolean isJoined(String event_id){
        if(!userEvents.equals("") || !userEvents.equals("[]")){
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
                Log.v("LOCATION_V", "User Events cannot be extracted");
                e.printStackTrace();
            }
        }
        else{
            Log.d("LOCATION_V", "UserEvents null error");
        }
        return false;
    }

    private boolean isOrganizer(String event_id){
        String organized_events = "";
        if(!userStr.equals("") || !userStr.equals("[]")){
            try {
                user = new JSONObject(userStr);
                organized_events = user.getString("organizedEvents");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if(!organized_events.equals("") || !organized_events.equals("[]")) {
                try {
                    JSONArray orgEvents = new JSONArray(organized_events);

                    for (int i = 0; i < orgEvents.length(); i++) {
                       // JSONObject row = orgEvents.getString(i);

                        //Retrieving event details
                        String temp_id = orgEvents.getString(i);

                        if (event_id.trim().equals(temp_id.trim()))
                            return true;
                    }

                } catch (JSONException e) {
                    Log.v("LOCATION_V", "Organized Events cannot be extracted");
                    e.printStackTrace();
                }
            }
        }
        else{
            Log.d("LOCATION_V", "Organized events null error");
        }
        return false;
    }

    /*
    * Loads the cookie of the target Event
    */
    private void loadTargetEvent(Marker marker) {

        s_task = new EventDetailsBackgroundTask(getContext());

        double latitude = marker.getPosition().latitude;
        double longitude = marker.getPosition().longitude;

        String lat_str = String.valueOf(latitude).replace(",", ".");
        String long_str = String.valueOf(longitude).replace(",", ".");

        s_task.execute("loadTargetEvent", lat_str, long_str);
    }

    private String getTargetEvent(){

        cookieList = msCookieManager.getCookieStore().getCookies();
        String str = "";
        if(cookieList.size() != 0){
            for (int i = cookieList.size()-1; i >= 0; i--) {
                if (cookieList.get(i).getName().equals("targetEvent")){
                    str = cookieList.get(i).getValue();
                    break;
                }
            }
        }
        else {
            Log.v("LOCATION_V", "Error getting the target event");
        }
        return str;
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
                final CharSequence place_name = place.getName();
                final CharSequence phone = place.getPhoneNumber();
                final String placeId = place.getId();

                //Create Event Dialog Initialization
                showDialog(place, requestCode);

            } else {
                Log.v("PLACEPICKER", "Error in PlacePicker ResultCode: " + resultCode);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    /*
    * Opens a dialog for Event Details -> EventDialogFragment
    */
    private void showDialog(Place place, int requestCode) {

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        // Create the fragment and show it as a dialog.
        DialogFragment newDialog = EventCreateDialog.newInstance(place);
        newDialog.setTargetFragment(this, requestCode);
        newDialog.show(ft, "dialog");
    }

    /*
    /* Sends the event record to database
    */
    public void addEvent(Place myPlace, String title, Date myDate, double fee, int participantCount, String category, String description, String userId) throws JSONException {

        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        String date = formatter.format(myDate);

        EventBackgroundTask createEventTask = new EventBackgroundTask();
        LatLng latLng = myPlace.getLatLng();

        String longitude = String.valueOf(latLng.longitude);
        String latitude = String.valueOf(latLng.latitude);
        //TODO: uncomment these for device usage:
        longitude = longitude.replace(",", ".");
        latitude = latitude.replace(",", ".");

        String pCount = String.valueOf(participantCount);
        String feeConverted = String.valueOf(fee);
        String city = setCity(myPlace);
        String placeName = myPlace.getName().toString();
        String organizerId = userId;

        String response = "";
        marker_task = new EventDetailsBackgroundTask(getContext());
        try {
            response = marker_task.execute("loadTargetEvent" ,latitude, longitude).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        if(response.equals("null") || response.equals("")){

            createEventTask.execute("1", title, date, city, longitude, latitude, myAddress, placeName, pCount, category, description, feeConverted, organizerId);

            myGoogleMap.addMarker(new MarkerOptions().position(myPlace.getLatLng())
                    .title(title)
                    .snippet(date));
        }
        else {
            float newLat, newLong;
            boolean added = false;
            for(int i = 1; i <= MAX_MARKER_COUNT; i++) {
                newLat = (float) (myPlace.getLatLng().latitude + i * COORDINATE_OFFSET);
                newLong = (float) (myPlace.getLatLng().longitude + i * COORDINATE_OFFSET);

                String newLatStr = String.valueOf(newLat);
                String newLongStr = String.valueOf(newLong);
                newLongStr = newLongStr.replace(",", ".");
                newLatStr = newLatStr.replace(",", ".");

                marker_task = new EventDetailsBackgroundTask(getContext());
                try {
                    response = marker_task.execute("loadTargetEvent", newLatStr, newLongStr).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

                if (response.equals("null") || response.equals("")) {

                    createEventTask.execute("1", title, date, city, newLongStr, newLatStr, myAddress, placeName, pCount, category, description, feeConverted, organizerId);

                    myGoogleMap.addMarker(new MarkerOptions().position(new LatLng(newLat, newLong))
                            .title(title)
                            .snippet(date)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
                    added = true;
                    break;
                }
            }
            if (added == false){
                Toast.makeText(getContext(), "More than " + MAX_MARKER_COUNT + " events at the same venue within a week is not allowed.", Toast.LENGTH_LONG).show();
            }
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
            myAddress = "";
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

            /*Log.d("PLACEPICKER_", "CITY: " + city);
            //Log.d("PLACEPICKER_", addresses.get(0).getFeatureName() + ", " + addresses.get(0).getSubAdminArea() + ", " + addresses.get(0).getPremises() + ", " + addresses.get(0).getCountryName());
            for(int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                //Log.d("PLACEPICKER_", address.getAddressLine(i));
            }*/

        } catch (IOException e) {
            Log.v("PLACEPICKER_", "Cannot run Geocoder");
            e.printStackTrace();
        }

        if(city.equals("") || city.equals("null") || city == null){
            city = place.getName().toString();
        }
        return city;
    }

    private boolean checkMapReady() {
        if (myGoogleMap == null) {
            Toast.makeText(getContext(), R.string.map_not_ready, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /*
    *   Updates the user's location and zooms in current location on startup
    */
    private void updateMyLocation() {

        if (!checkMapReady()) {
            return;
        }
        if (ActivityCompat.checkSelfPermission(getActivity().getBaseContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity().getBaseContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //Log.v("LOCATION_V", "Permissions GIVEN for My Location");
            myGoogleMap.setMyLocationEnabled(true);
            myGoogleMap.setBuildingsEnabled(true);

            //Zoom in the known location
            Criteria criteria = new Criteria();
            Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
            zoomInLocation(location);

            cookieList = msCookieManager.getCookieStore().getCookies(); //for showing all events
            retrieveEvents();

        } else {
            PermissionUtils.requestPermission((AppCompatActivity) getActivity(), LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, false);
            Log.v("LOCATION_V", "Permission conditions not satisfied for My Location");
            myGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, DEFAULT_ZOOM));
        }
    }

    private void zoomInLocation(Location location){
        if (location != null) {
            myGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(location.getLatitude(), location.getLongitude()), 13));

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(location.getLatitude(), location.getLongitude()))      // Sets the center of the map to location user
                    .zoom(DEFAULT_ZOOM)         // Sets the zoom
                    .bearing(DEFAULT_BEARING)   // Sets the orientation of the camera to east
                    .tilt(DEFAULT_TILT)         // Sets the tilt of the camera to 30 degrees
                    .build();                   // Creates a CameraPosition from the builder
            myGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
        else{
            Log.v("LOCATION_V", "Location is null");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] results) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        myGoogleMap.setMyLocationEnabled(true);
    }


    public void goToEventDetails(String eventStr) {

        Fragment eventDetails = EventDetailsPage.newInstance(eventStr, null);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.content_frame, eventDetails);
        transaction.commit();

    }

    /*
    * This method must be implemented if locationUpdates are necessary on regular basis.
    */
    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onCancel(DialogInterface dialog) {

    }

    @Override
    public void onDismiss(DialogInterface dialog) {

    }



}