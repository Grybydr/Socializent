package com.socializent.application.socializent.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.socializent.application.socializent.R;

public class NavigationDrawerFourth extends Fragment {
    public static ListView friendList;
    View friendView;

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
        friendView = inflater.inflate(R.layout.navigation_drawer_fourth_frag, container, false);
        return friendView;
    }
    public void onViewCreated (View view, Bundle savedInstanceState) {

       // friendList = getListView();
        friendList = (ListView) friendView.findViewById(R.id.friendList);
    }

}
