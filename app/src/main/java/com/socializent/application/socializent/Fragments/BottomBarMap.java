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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.socializent.application.socializent.Controller.PersonBackgroundTask.msCookieManager;

public class BottomBarMap extends Fragment implements OnMapReadyCallback, LocationListener,  DialogInterface.OnCancelListener, DialogInterface.OnDismissListener {

    //CONSTANTS
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private static final int REQUEST_PLACE_PICKER = 1;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final int DEFAULT_ZOOM = 17;
    private static final int DEFAULT_BEARING = 90;
    private static final int DEFAULT_TILT = 0;
    private static final long WEEK_IN_MILLIS = 604800000;
    private static final String TURKEY_TAG = "Turkey";

    private final LatLng defaultLocation = new LatLng(39.868010, 32.748823); //Bilkent's Coordinates

    //VARIABLES
    private GoogleMap myGoogleMap;
    private UiSettings myUiSettings;
    private FloatingActionButton addEventButton, refreshFab;
    private LocationManager locationManager;
    private View mapView;
    private String myAddress = "";

    EventDetailsBackgroundTask s_task;
    List<HttpCookie> cookieList;

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

        //cookieList = msCookieManager.getCookieStore().getCookies();
        String events = "";
        for (int i = 0; i < cookieList.size(); i++) {
            if (cookieList.get(i).getName().equals("allEvents")){
                events = cookieList.get(i).getValue();
                //Log.v("LOCATION_V", "ALLEVENTS: " + events);
                break;
            }
        }
        try {

            String tempLat, tempLong, eventType, eventTitle;
            JSONArray eventsArray = new JSONArray(events);

            for (int i = 0; i < eventsArray.length(); i++) {

                JSONObject row = eventsArray.getJSONObject(i);

                //Retrieving event details
                eventTitle = row.getString("name");
                eventType = row.getString("category").toLowerCase();
                String pin = "@drawable/" + eventType;

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

                    //TODO: custom image for pins

                    /*Drawable hold = ContextCompat.getDrawable(getActivity(), R.drawable.concert);
                    BitmapDrawable hold2 = (BitmapDrawable) hold;
                    Bitmap bitPhoto = hold2.getBitmap();

                    Bitmap icon = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.concert);

                    myGoogleMap.addMarker(new MarkerOptions().position(new LatLng(t_lat, t_longi))
                            .title(eventTitle)
                            .icon(BitmapDescriptorFactory.fromBitmap(icon)));*/

                    myGoogleMap.addMarker(new MarkerOptions().position(new LatLng(t_lat, t_longi))
                            .title(eventTitle));

                }
                else {
                    Log.v("LOCATION_V", "Latitude or Longitude is NULL");
                }
            }
        } catch (JSONException e) {
            Log.v("LOCATION_V", "Events cannot be retrieved from cookie: JSON error");
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
    public void addEvent(Place myPlace, String title, Date myDate, double fee, int participantCount, String category, String description) {

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

        createEventTask.execute("1", title, date, city, longitude, latitude, myAddress, placeName, pCount, category, description, feeConverted);

        myGoogleMap.addMarker(new MarkerOptions().position(myPlace.getLatLng())
                .title(title));

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