package com.socializent.application.socializent.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.socializent.application.socializent.Controller.NotificationAdapterToList;
import com.socializent.application.socializent.Modal.Person;
import com.socializent.application.socializent.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpCookie;
import java.util.ArrayList;
import java.util.List;

import static com.socializent.application.socializent.Controller.PersonBackgroundTask.msCookieManager;

/**
 * Created by Irem on 13.3.2017.
 */

public class BottomBarNotifications  extends Fragment {
    View notificationView;
    ListView notificationListView;
    List<HttpCookie> cookieList;
    NotificationAdapterToList notficationListAdapter;
    ArrayList<Person> friendRequests;
    String user = "";
    String request = "";
    JSONObject userObject = null;
    public static String activeUserId;


    public BottomBarNotifications(){}
    public static BottomBarNotifications newInstance() {
        BottomBarNotifications fragment = new BottomBarNotifications();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        notificationView = inflater.inflate(R.layout.bottom_bar_notification_fragment, container, false);
        return notificationView;
    }
    public void onViewCreated (View view, Bundle savedInstanceState) {
        notificationListView =  (ListView) notificationView.findViewById(R.id.notificationList);

        cookieList = msCookieManager.getCookieStore().getCookies();
        friendRequests = new ArrayList<>();

        for (int i = 0; i < cookieList.size(); i++) {
            if (cookieList.get(i).getName().equals("user")) {
                user = cookieList.get(i).getValue();
            }
            if(cookieList.get(i).getName().equals("friendRequest")){
                request = cookieList.get(i).getValue();
            }
        }
        try {
            userObject = new JSONObject(user);
            activeUserId = userObject.getString("_id");
            JSONArray a = new JSONArray(request);
            for (int n = 0; n < a.length();n++) {
                String id = a.getJSONObject(n).getString("_id");
                String name = a.getJSONObject(n).getString("firstName");
                String surname = a.getJSONObject(n).getString("lastName");
                Person p = new Person(id,name,surname,"",0,"","","",null,null,null,null,null,null);
                friendRequests.add(p);

            }
            notficationListAdapter = new NotificationAdapterToList(getContext(),friendRequests);
            notificationListView.setAdapter(notficationListAdapter);


           // new NavigationDrawerFourth.SearchUserForList(getContext()).execute();

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}