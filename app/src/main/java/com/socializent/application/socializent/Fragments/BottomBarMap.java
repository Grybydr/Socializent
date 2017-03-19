package com.socializent.application.socializent.Fragments;

/**
 * Created by Irem on 13.3.2017.
 */

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.socializent.application.socializent.R;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;
import static junit.framework.Assert.assertEquals;

public class BottomBarMap extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapClickListener {

    //CONSTANTS
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private static final int REQUEST_PLACE_PICKER = 1;

    //VARIABLES
    private GoogleMap myGoogleMap;
    private UiSettings myUiSettings;
    FloatingActionButton myFabButton;
    private LatLng eventPoint;
    private Geocoder geocoder;
    List<Address> addresses;

    public static BottomBarMap newInstance() {
        BottomBarMap fragment = new BottomBarMap();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mapView = inflater.inflate(R.layout.bottom_bar_map_fragment, container, false);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapView);  //---------CHECK!

        mapFragment.getMapAsync(this);

        myFabButton = (FloatingActionButton) mapView.findViewById(R.id.addEventFab);
        myFabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUpMap();
            }
        });

        Toast.makeText(getContext(), R.string.map_gps_warning, Toast.LENGTH_LONG).show();

        return mapView;
    }

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

    private void setUpMap() {
        Toast.makeText(getContext(), R.string.choose_location, Toast.LENGTH_LONG).show();
        myGoogleMap.setOnMapClickListener(this);
    }

    @Override
    public void onMapClick(LatLng point) {
        eventPoint = point;
        addEvent(eventPoint);
    }

    private void addEvent(LatLng point) {
        //myGoogleMap.addMarker(new MarkerOptions().position(point).title("Event"));

        try {
            geocoder = new Geocoder(getContext(), Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(point.latitude, point.longitude, 1);
            if (addresses.isEmpty()) {
                Log.v("ADDRESS", "Waiting for Location");
            }
            else {
                if (addresses.size() > 0) {
                    String city = "unknown", knownName = "unknown", state = "unknown", country = "unknown", county = "unknown";

                     // Gives whole address
                    for (int i = 0; i < addresses.get(0).getMaxAddressLineIndex(); i++) {
                        Log.v("ADDRESS",addresses.get(0).getAddressLine(i));
                    }

                    //extracting address info
                    if (addresses.get(0).getLocality() != null)  city = addresses.get(0).getLocality();
                    else
                        if (addresses.get(0).getSubAdminArea() != null)  city = addresses.get(0).getSubAdminArea();

                    if (addresses.get(0).getFeatureName() != null) knownName = addresses.get(0).getFeatureName();
                    if (addresses.get(0).getAdminArea() != null) state = addresses.get(0).getAdminArea();
                    if (addresses.get(0).getCountryName() != null) country = addresses.get(0).getCountryName();
                    if (addresses.get(0).getSubAdminArea() != null)  county = addresses.get(0).getSubAdminArea();

                    Log.v("ADDRESS", knownName + ", " + county + ", " + city + ", " + state + ", " + country);

                    Log.v("ADDRESS", addresses.get(0).getFeatureName() + ", " + addresses.get(0).getSubAdminArea() +", " + addresses.get(0).getPremises() + ", " + addresses.get(0).getCountryName());
                    Toast.makeText(getContext(), "Address:- " + addresses.get(0).getFeatureName() + addresses.get(0).getSubAdminArea() + addresses.get(0).getLocality(), Toast.LENGTH_LONG).show();
                }
            }

           /* PlacePicker.IntentBuilder ibuilder = new PlacePicker.IntentBuilder();
            Intent intent = ibuilder.build(getActivity());
            startActivityForResult(intent, REQUEST_PLACE_PICKER);*/
        }
        /*catch (GooglePlayServicesRepairableException e) {
            GooglePlayServicesUtil
                    .getErrorDialog(e.getConnectionStatusCode(), getActivity(), 0);
        } catch (GooglePlayServicesNotAvailableException e) {
            Toast.makeText(getActivity(), "Google Play Services is not available.",
                    Toast.LENGTH_LONG)
                    .show();
        }*/ catch (IOException e) {
            e.printStackTrace();
        }
        //Disabling fab after event location is chosen
        myGoogleMap.setOnMapClickListener(null);
    }

    //For PlacePicker
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intentData) {
        if (requestCode == REQUEST_PLACE_PICKER) {
            if (resultCode == RESULT_OK) {
                Place selectedPlace = PlacePicker.getPlace(getContext(), intentData);
                myGoogleMap.addMarker(new MarkerOptions().position(selectedPlace.getLatLng()).title("Event"));
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, intentData);
        }
    }

    private boolean checkMapReady() {
        if (myGoogleMap == null) {
            Toast.makeText(getContext(), R.string.map_not_ready, Toast.LENGTH_SHORT).show(); //------CHECK
            return false;
        }
        return true;
    }

    private void updateMyLocation() {
        // TODO: Zoom in my location on start up

        if (!checkMapReady()) {
            return;
        }
        if (ActivityCompat.checkSelfPermission(getActivity().getBaseContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity().getBaseContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Log.v("LOCATION", "Permissions GIVEN for My Location");
            myGoogleMap.setMyLocationEnabled(true);
            myGoogleMap.setBuildingsEnabled(true);
            return;
        }
        else {
            PermissionUtils.requestPermission((AppCompatActivity) getActivity(), LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, false);
            Log.v("LOCATION", "Permission conditions not satisfied for My Location");
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
}