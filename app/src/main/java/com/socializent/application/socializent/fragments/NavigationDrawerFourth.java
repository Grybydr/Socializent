package com.socializent.application.socializent.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.socializent.application.socializent.R;

public class NavigationDrawerFourth extends Fragment {

    public NavigationDrawerFourth() {
        // Required empty public constructor
    }

    public static NavigationDrawerFourth newInstance() {
        return new NavigationDrawerFourth();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.navigation_drawer_fourth_frag, container, false);
    }

}
