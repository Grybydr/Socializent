package com.socializent.application.socializent.Fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.socializent.application.socializent.R;
import com.socializent.application.socializent.other.CircleDrawable;

public class NavigationDrawerFirst extends Fragment {
    CircleDrawable circle;
    View profileView;
    public NavigationDrawerFirst() {
        // Required empty public constructor
    }

    public static NavigationDrawerFirst newInstance() {
        return new NavigationDrawerFirst();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.material_design_profile_screen_xml_ui_design);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        profileView = inflater.inflate(R.layout.material_design_profile_screen_xml_ui_design, container, false);
        ImageView image = (ImageView) profileView.findViewById(R.id.user_profile_photo);
        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.id.user_profile_photo);
        //image.setImageBitmap(bm);
        circle = new CircleDrawable(bm,true);
        image.setBackground(circle);

        return profileView;

    }

}