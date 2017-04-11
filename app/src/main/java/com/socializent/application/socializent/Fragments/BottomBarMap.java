package com.socializent.application.socializent.Fragments;

/**
 * Created by Irem on 13.3.2017.
 * Edited by Zulal :)
 */

import android.Manifest;
import android.support.v4.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
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
import com.google.android.gms.maps.model.MarkerOptions;
import com.socializent.application.socializent.R;

public class BottomBarMap extends Fragment implements OnMapReadyCallback, LocationListener,  DialogInterface.OnCancelListener, DialogInterface.OnDismissListener {

    //CONSTANTS
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private static final int REQUEST_PLACE_PICKER = 1;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final int DEFAULT_ZOOM = 17;
    private static final int DEFAULT_BEARING = 90;
    private static final int DEFAULT_TILT = 0;
    private static final long MIN_TIME = 400;
    private static final float MIN_DISTANCE = 1000;

    private final LatLng defaultLocation = new LatLng(39.868010, 32.748823); //Bilkent's Coordinates

    //VARIABLES
    private GoogleMap myGoogleMap;
    private UiSettings myUiSettings;
    FloatingActionButton myFabButton;
    private LocationManager locationManager;
    private boolean showDialog = false;

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
        View mapView = inflater.inflate(R.layout.bottom_bar_map_fragment, container, false);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapView);

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            PermissionUtils.requestPermission((AppCompatActivity) getActivity(), LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, false);
            Log.v("LOCATION", "Permission conditions not satisfied for My Location");

            Toast.makeText(getContext(), R.string.map_loc_warning, Toast.LENGTH_LONG).show();
            myGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, DEFAULT_ZOOM));
        }
        //locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME, MIN_DISTANCE, this);
        mapFragment.getMapAsync(this);

        myFabButton = (FloatingActionButton) mapView.findViewById(R.id.addEventFab);
        myFabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initializePlacePicker();
            }
        });
        Toast.makeText(getContext(), R.string.map_gps_warning, Toast.LENGTH_LONG).show();
        return mapView;
    }

    /*
    *   Adds some features on the map once the map becomes ready
    */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        myGoogleMap = googleMap;
        myUiSettings = myGoogleMap.getUiSettings();
        myUiSettings.setZoomControlsEnabled(true);
        myUiSettings.setCompassEnabled(true);
        myUiSettings.setZoomGesturesEnabled(true);
        myUiSettings.setScrollGesturesEnabled(true);

        updateMyLocation();
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
                final CharSequence address = place.getAddress();
                final CharSequence phone = place.getPhoneNumber();
                final String placeId = place.getId();

                //Create Event Dialog Initialization
                showDialog(place, requestCode);

                //addEvent(place);
                Log.d("PLACEPICKER", "Place selected: " + placeId + " (" + place_name.toString() + ")");

            } else {
                Log.d("PLACEPICKER", "Error in PlacePicker ResultCode: " + resultCode);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void showDialog(Place place, int requestCode) {

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        // Create the fragment and show it as a dialog.
        DialogFragment newDialog = EventCreateFragment.newInstance(place);
        newDialog.setTargetFragment(this, requestCode);
        newDialog.show(ft, "dialog");
    }

    public void addEvent(Place place) {

        myGoogleMap.addMarker(new MarkerOptions().position(place.getLatLng())
                .title("Event"));

    }

    private boolean checkMapReady() {
        if (myGoogleMap == null) {
            Toast.makeText(getContext(), R.string.map_not_ready, Toast.LENGTH_SHORT).show(); //------CHECK
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
            Log.v("LOCATION", "Permissions GIVEN for My Location");
            myGoogleMap.setMyLocationEnabled(true);
            myGoogleMap.setBuildingsEnabled(true);

            //Zoom in the known location
            Criteria criteria = new Criteria();
            Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
            zoomInLocation(location);

            return;

        } else {
            PermissionUtils.requestPermission((AppCompatActivity) getActivity(), LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, false);
            Log.v("LOCATION", "Permission conditions not satisfied for My Location");
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
            Log.v("LOCATION", "Location is null");
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