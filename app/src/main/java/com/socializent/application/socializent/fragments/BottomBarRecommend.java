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

public class BottomBarRecommend extends Fragment {
    public static BottomBarRecommend newInstance() {
        BottomBarRecommend fragment = new BottomBarRecommend();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_item_two, container, false);
    }
}