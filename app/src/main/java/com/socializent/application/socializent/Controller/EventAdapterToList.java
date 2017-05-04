package com.socializent.application.socializent.Controller;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.socializent.application.socializent.Modal.Event;
import com.socializent.application.socializent.Modal.EventTypes;
import com.socializent.application.socializent.R;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Irem on 26.4.2017.
 */

public class EventAdapterToList extends ArrayAdapter<Event> {

    Context mContext;
    public EventAdapterToList(Context context, ArrayList<Event> events) {
        super(context, 0, events);
        mContext = context;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Event event = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_events, parent, false);
        }
        // Lookup view for data population
        TextView eventName = (TextView) convertView.findViewById(R.id.eventName);
        TextView eventDescription = (TextView) convertView.findViewById(R.id.eventDescription);
        TextView eventtime = (TextView) convertView.findViewById(R.id.eventtime);
        // Populate the data into the template view using the data object
        eventName.setText(event.getName());
        eventDescription.setText(event.getDescription());
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(event.getDate());
        String dateStr = df.format(calendar.getTime());
        eventtime.setText(dateStr);
        EventTypes t = event.getEventType();
        String type = t.toString().toLowerCase();
        String uri = "@drawable/" + type;  // where myresource (without the extension) is the file

        int imageResource = mContext.getResources().getIdentifier(uri, "drawable", mContext.getPackageName());
        ImageView imageview= (ImageView)convertView.findViewById(R.id.eventImageView);
        Drawable res = mContext.getResources().getDrawable(imageResource, null);
        imageview.setImageDrawable(res);

        // Return the completed view to render on screen
        return convertView;
    }



}
