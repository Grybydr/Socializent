package com.socializent.application.socializent.fragments;

/**
 * Created by Irem on 13.3.2017.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.socializent.application.socializent.R;

public class BottomBarChat extends Fragment {
    public static BottomBarChat newInstance() {
        BottomBarChat fragment = new BottomBarChat();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bottom_bar_chat_fragment, container, false);
    }
}