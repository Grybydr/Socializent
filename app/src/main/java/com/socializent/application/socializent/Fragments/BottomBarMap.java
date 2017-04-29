package com.socializent.application.socializent.Fragments;

/**
 * Created by Irem on 13.3.2017.
 * Edited by Zulal :)
 */

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.socializent.application.socializent.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpCookie;
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

    private final LatLng defaultLocation = new LatLng(39.868010, 32.748823); //Bilkent's Coordinates

    //VARIABLES
    private GoogleMap myGoogleMap;
    private UiSettings myUiSettings;
    private FloatingActionButton addEventButton;
    private LocationManager locationManager;
    private View mapView;

    String city;

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
        Toast.makeText(getContext(), R.string.map_gps_warning, Toast.LENGTH_SHORT).show();

        return mapView;
    }

    private void retrieveEvents(){

        List<HttpCookie> cookieList = msCookieManager.getCookieStore().getCookies();
        String events = "";
        for (int i = 0; i < cookieList.size(); i++) {
            if (cookieList.get(i).getName().equals("allEvents")){
                events = cookieList.get(i).getValue();
                //Log.v("LOCATION_V", "ALLEVENTS: " + events);
                break;
            }
        }
        try {

            String tempLat, tempLong;
            JSONArray eventsArray = new JSONArray(events);

            for (int i = 0; i < eventsArray.length(); i++) {

                JSONObject row = eventsArray.getJSONObject(i);

                //Retrieving event details

                String eventTitle = row.getString("name"); //TODO: check database for this
                if (eventTitle == "null" || eventTitle.isEmpty() || eventTitle == "" )
                    eventTitle = "Event";

                JSONObject pl = row.getJSONObject("place");
                tempLat = pl.getString("latitude");
                tempLong = pl.getString("longitude");

                //Log.v("LOCATION_V", "tempLat" + tempLat);
                //Log.v("LOCATION_V", "tempLong" + tempLong);

                if ((tempLat != "null" && !tempLat.isEmpty()) && (tempLong != "null" && !tempLong.isEmpty())) {

                    final double lat = Double.parseDouble(tempLat);
                    final double longi = Double.parseDouble(tempLong);
                    myGoogleMap.addMarker(new MarkerOptions().position(new LatLng(lat, longi))
                            .title(eventTitle));
                }
                else {
                    Log.v("LOCATION", "Latitude or Longitude is NULL");
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
                return true;
            }
        });
        myGoogleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {

            }
        });

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

                //Retriving City
                Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
                List<Address> addresses = null;
                try {
                    addresses = geocoder.getFromLocation(place.getLatLng().latitude, place.getLatLng().longitude, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (addresses.get(0).getLocality() != null)
                    city = addresses.get(0).getLocality().toString();
                else if (addresses.get(0).getSubAdminArea() != null)
                    city = addresses.get(0).getSubAdminArea().toString();
                else
                    city = "unknown";

                //Log.d("PLACEPICKER_", "CITY: " + city);
                Address address = addresses.get(0);
               // Log.v("PLACEPICKER_", addresses.get(0).getFeatureName() + ", " + addresses.get(0).getSubAdminArea() + ", " + addresses.get(0).getPremises() + ", " + addresses.get(0).getCountryName());
                /*for(int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                    Log.d("PLACEPICKER_", address.getAddressLine(i));
                }
                String temp = address.getAddressLine(address.getMaxAddressLineIndex()-1).toString();
                Log.d("PLACEPICKER_", "TEMP : " + temp);*/

                //Create Event Dialog Initialization
                showDialog(place, requestCode);

            } else {
                Log.d("PLACEPICKER", "Error in PlacePicker ResultCode: " + resultCode);
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

        String longitude = String.valueOf(( latLng.longitude));
        longitude = longitude.replace(",",".");
        String latitude = String.valueOf((latLng.latitude));
        latitude = latitude.replace(",",".");

        String pCount = String.valueOf(participantCount);
        String feeConverted = String.valueOf(fee);

        createEventTask.execute("1", title, date, city, longitude, latitude, pCount, category, description, feeConverted);

        myGoogleMap.addMarker(new MarkerOptions().position(myPlace.getLatLng())
                .title(title));

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
            Log.v("LOCATION_V", "Permissions GIVEN for My Location");
            myGoogleMap.setMyLocationEnabled(true);
            myGoogleMap.setBuildingsEnabled(true);

            //Zoom in the known location
            Criteria criteria = new Criteria();
            Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
            zoomInLocation(location);

            retrieveEvents();

        } else {
            PermissionUtils.requestPermission((AppCompatActivity) getActivity(), LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, false);
            Log.v("LOCATION_V", "Permission conditions not satisfied for My Location");
            myGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, DEFAULT_ZOOM));
        }
    }

    /*
    *   Zooms in the given location
     */
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