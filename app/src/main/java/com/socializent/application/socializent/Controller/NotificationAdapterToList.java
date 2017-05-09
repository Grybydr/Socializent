package com.socializent.application.socializent.Controller;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.socializent.application.socializent.Fragments.BottomBarNotifications;
import com.socializent.application.socializent.Modal.Person;
import com.socializent.application.socializent.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Irem on 7.5.2017.
 */

public class NotificationAdapterToList extends ArrayAdapter<Person> {
    Person s;
    Context mContext;
    String accessToken;
    PersonBackgroundTask  task;
    PersonBackgroundTask refresh;
    public NotificationAdapterToList(Context context, ArrayList<Person> notification) {
        super(context, 0, notification);
        mContext = context;
    }
    public View getView(int position, View convertView, ViewGroup parent) {

        s = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_notification, parent, false);
        }
        final TextView notification = (TextView) convertView.findViewById(R.id.add_user_rext);
        TextView userName = (TextView) convertView.findViewById(R.id.add_user_person_name);
        String fullname = s.getFirstName() + " " + s.getLastName();
        userName.setText(fullname);

        final ImageButton acceptButton = (ImageButton) convertView.findViewById(R.id.accept_user);
        final ImageButton rejectButton = (ImageButton) convertView.findViewById(R.id.reject_user);
        accessToken = BottomBarNotifications.activeUserId ;

        acceptButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                task = new PersonBackgroundTask(mContext);
                refresh = new PersonBackgroundTask(mContext);
                task.execute("4",s.getId(),"1");
                notification.setText("You are now friends!");
                acceptButton.setVisibility(View.GONE);
                rejectButton.setVisibility(View.GONE);
                refresh.execute("3");

            }
        });
        rejectButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                task = new PersonBackgroundTask(mContext);
                refresh = new PersonBackgroundTask(mContext);
                task.execute("4",s.getId(),"0");
                notification.setText("You rejected the request!");
                acceptButton.setVisibility(View.GONE);
                rejectButton.setVisibility(View.GONE);
                refresh.execute("3");
            }
        });


        return convertView;
    }

}
