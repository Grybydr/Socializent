package com.socializent.application.socializent.Fragments;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.socializent.application.socializent.Controller.PersonBackgroundTask;
import com.socializent.application.socializent.Modal.Person;
import com.socializent.application.socializent.R;
import com.socializent.application.socializent.other.CircleDrawable;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpCookie;
import java.util.List;

import static com.socializent.application.socializent.Controller.PersonBackgroundTask.msCookieManager;
import static com.socializent.application.socializent.Template.user;

public class NavigationDrawerFirst extends Fragment {
    //CircleDrawable circle;
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
        JSONObject userObject= null;
        //TextView usernameView = (TextView) getView().findViewById(R.id.user_profile_name);
        //Log.d("getView: ", getView().toString());
        //usernameView.setText("Güray BAYDUR");
        //Person p = Hawk.get("user");
        String user = "";

        List<HttpCookie> cookieList = msCookieManager.getCookieStore().getCookies();

        for (int i = 0; i < cookieList.size(); i++) {
            if (cookieList.get(i).getName().equals("user")){
                user = cookieList.get(i).getValue();
                break;
            }
        }

        try {
            userObject = new JSONObject(user);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("User Info: " ,user);

/**/

        profileView = inflater.inflate(R.layout.material_design_profile_screen_xml_ui_design, container, false);
        TextView userNameText = (TextView) profileView.findViewById(R.id.user_profile_name);
        TextView bioText = (TextView) profileView.findViewById(R.id.user_profile_short_bio);

        bioText.setText("Part-Time Developer at Etgi Group");
        try {
            userNameText.setText(userObject.getString("fullName"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return profileView;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }
}
