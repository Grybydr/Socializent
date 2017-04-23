package com.socializent.application.socializent.Fragments;

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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;

import android.widget.TextView;
import android.widget.Toast;

import com.socializent.application.socializent.Controller.PersonBackgroundTask;
import com.socializent.application.socializent.Modal.Event;
import com.socializent.application.socializent.Modal.Person;
import com.socializent.application.socializent.R;
import com.socializent.application.socializent.other.CircleDrawable;

import net.cachapa.expandablelayout.ExpandableLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpCookie;
import java.util.List;

import static com.socializent.application.socializent.Controller.PersonBackgroundTask.msCookieManager;


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
        //setContentView(R.layout.material_design_profile_screen_xml_ui_design);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        JSONObject userObject = null;
        //TextView usernameView = (TextView) getView().findViewById(R.id.user_profile_name);
        //Log.d("getView: ", getView().toString());
        //usernameView.setText("GÃ¼ray BAYDUR");
        //Person p = Hawk.get("user");
        String user = "";
        profileView = inflater.inflate(R.layout.material_design_profile_screen_xml_ui_design, container, false);
        imagePen = (ImageView) profileView.findViewById(R.id.edit_pen);
        imagePen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editPage(v);
            }
        });

        List<HttpCookie> cookieList = msCookieManager.getCookieStore().getCookies();

        for (int i = 0; i < cookieList.size(); i++) {
            if (cookieList.get(i).getName().equals("user")) {
                user = cookieList.get(i).getValue();
                break;
            }
        }

        try {
            userObject = new JSONObject(user);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("User Info: ", user);


        TextView userNameText = (TextView) profileView.findViewById(R.id.user_profile_name);
        TextView bioText = (TextView) profileView.findViewById(R.id.user_profile_short_bio);

        bioText.setText("Part-Time Developer at Etgi Group");
        try {
            userNameText.setText(userObject.getString("fullName"));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        RecyclerView recyclerView = (RecyclerView) profileView.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(profileView.getContext()));
        try {
            recyclerView.setAdapter(new SimpleAdapter(recyclerView));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return profileView;

    }

    public void editPage(View view) throws RuntimeException {
        Fragment mFragment = EditProfile.newInstance();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.content_frame, mFragment);
        transaction.commit();

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }


    private static class SimpleAdapter extends RecyclerView.Adapter<SimpleAdapter.ViewHolder> {
        private static final int UNSELECTED = -1;

        private RecyclerView recyclerView;
        private int selectedItem = UNSELECTED;

        private List<HttpCookie> cookieList = msCookieManager.getCookieStore().getCookies();;
        private String userEvents = "";
        private JSONArray userEventsArray = null;

        public SimpleAdapter(RecyclerView recyclerView) throws JSONException {
            this.recyclerView = recyclerView;

            for (int i = 0; i < cookieList.size(); i++) {
                if (cookieList.get(i).getName().equals("userEvents")){
                    userEvents = cookieList.get(i).getValue();
                    break;
                }
            }
            userEventsArray = new JSONArray(userEvents);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recycler_item, parent, false);
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

            JSONObject eventObject;
            Event userEvent = new Event();

            try {
                eventObject = (JSONObject) userEventsArray.get(position);
                userEvent.setName(eventObject.getString("name"));
                userEvent.setDescription(eventObject.getString("description"));
            } catch (JSONException e) {
                e.printStackTrace();
            }


            holder.bind(position,userEvent);



        }

        @Override
        public int getItemCount() {
            return userEventsArray.length();
        }


        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ExpandableLayout expandableLayout;
        private TextView expandButton;
        private TextView eventDescription;
        private TextView eventDate;
        private TextView eventParticipantNumber;
        private TextView eventTags;
        private TextView eventPlace;

        private int position;

        public ViewHolder(View itemView) {
            super(itemView);

            expandableLayout = (ExpandableLayout) itemView.findViewById(R.id.expandable_layout);
            expandableLayout.setInterpolator(new OvershootInterpolator());

            expandButton = (TextView) itemView.findViewById(R.id.expand_button);
            eventDescription = (TextView) itemView.findViewById(R.id.event_description);
            eventDate = (TextView) itemView.findViewById(R.id.event_date);
            eventParticipantNumber = (TextView) itemView.findViewById(R.id.event_participant_num);
            eventTags = (TextView) itemView.findViewById(R.id.event_tags);
            eventPlace = (TextView) itemView.findViewById(R.id.event_place);

            expandButton.setOnClickListener(this);
        }

        public void bind(int position, Event event) {
            this.position = position;

            expandButton.setText(event.getName());

            expandButton.setSelected(false);
            eventDescription.setText(event.getDescription());

            expandableLayout.collapse(false);
        }

        @Override
        public void onClick(View view) {
            ViewHolder holder = (ViewHolder) recyclerView.findViewHolderForAdapterPosition(selectedItem);
            if (holder != null) {
                holder.expandButton.setSelected(false);
                holder.expandableLayout.collapse();
            }

            if (position == selectedItem) {
                selectedItem = UNSELECTED;
            } else {
                expandButton.setSelected(true);
                expandableLayout.expand();
                selectedItem = position;
                }
            }
        }
    }
}