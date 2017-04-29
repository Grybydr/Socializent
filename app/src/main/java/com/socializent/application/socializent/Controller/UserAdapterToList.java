package com.socializent.application.socializent.Controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.socializent.application.socializent.Modal.Event;
import com.socializent.application.socializent.Modal.Person;
import com.socializent.application.socializent.R;

import java.util.ArrayList;

/**
 * Created by Irem on 29.4.2017.
 */

public class UserAdapterToList extends ArrayAdapter<Person> {

    Context mContext;
    public UserAdapterToList(Context context, ArrayList<Person> user) {
        super(context, 0, user);
        mContext = context;
    }
    public View getView(int position, View convertView, ViewGroup parent) {
        Person person = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_user, parent, false);
        }
        TextView userName = (TextView) convertView.findViewById(R.id.userListName);
        TextView userSurname = (TextView) convertView.findViewById(R.id.userListSurname);
        TextView userBio = (TextView) convertView.findViewById(R.id.userShortBio);

        // Populate the data into the template view using the data object
        userName.setText(person.getFirstName());
        userSurname.setText(person.getLastName());
        userBio.setText(person.getBio());

        // Return the completed view to render on screen
        return convertView;

    }
}
