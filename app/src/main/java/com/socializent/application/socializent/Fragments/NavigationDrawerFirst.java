package com.socializent.application.socializent.Fragments;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.socializent.application.socializent.Controller.PersonBackgroundTask;
import com.socializent.application.socializent.Modal.Person;
import com.socializent.application.socializent.R;
import com.socializent.application.socializent.other.CircleDrawable;

import org.json.JSONException;
import org.json.JSONObject;

public class NavigationDrawerFirst extends Fragment {
    //CircleDrawable circle;
    View profileView;
    ImageView imagePen;
    public NavigationDrawerFirst() {
        // Required empty public constructor
    }

    public static NavigationDrawerFirst newInstance() {
        return new NavigationDrawerFirst();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //TextView usernameView = (TextView) getView().findViewById(R.id.user_profile_name);
        //usernameView.setText("Güray BAYDUR");
        profileView = inflater.inflate(R.layout.material_design_profile_screen_xml_ui_design, container, false);
        imagePen = (ImageView) profileView.findViewById(R.id.edit_pen);
        imagePen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editPage(v);
            }
        });

        JSONObject userObject= null;
        String user = com.socializent.application.socializent.Controller.PersonBackgroundTask
                .msCookieManager.getCookieStore().getCookies().get(com.socializent.application.socializent.Controller.PersonBackgroundTask
                        .msCookieManager.getCookieStore().getCookies().size()-1).getValue();
        try {
            userObject = new JSONObject(user);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("User Info: " ,user);

        TextView userNameText = (TextView) profileView.findViewById(R.id.user_profile_name);
        TextView bioText = (TextView) profileView.findViewById(R.id.user_profile_short_bio);

        bioText.setText("Part-Time Developer at Etgi Group");
//        try {
//            userNameText.setText(userObject.getString("fullName"));
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
        return profileView;

    }

    public void editPage(View view)throws RuntimeException{
        Fragment mFragment = EditProfile.newInstance();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.content_frame, mFragment);
        transaction.commit();

    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

}
