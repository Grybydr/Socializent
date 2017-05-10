package com.socializent.application.socializent.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.socializent.application.socializent.Controller.EventBackgroundTask;
import com.socializent.application.socializent.Controller.PersonBackgroundTask;
import com.socializent.application.socializent.Modal.Event;
import com.socializent.application.socializent.R;

import net.cachapa.expandablelayout.ExpandableLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpCookie;
import java.util.List;

import static com.socializent.application.socializent.Controller.PersonBackgroundTask.msCookieManager;

public class NavigationDrawerThird extends Fragment {

    View pastEventsView;
    NavigationDrawerThird.SimpleAdapter mAdapter;
    RecyclerView recyclerView;

    public NavigationDrawerThird() {
        // Required empty public constructor
    }

    public static NavigationDrawerThird newInstance() {
        return new NavigationDrawerThird();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        pastEventsView = inflater.inflate(R.layout.navigation_drawer_third_fragm, container, false);

        recyclerView = (RecyclerView) pastEventsView.findViewById(R.id.recycler_view2);
        recyclerView.setLayoutManager(new LinearLayoutManager(pastEventsView.getContext()));



        try {
            mAdapter = new NavigationDrawerThird.SimpleAdapter(recyclerView);
            recyclerView.setAdapter(mAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return pastEventsView;

    }

    private class SimpleAdapter extends RecyclerView.Adapter<NavigationDrawerThird.SimpleAdapter.ViewHolder> {
        private static final int UNSELECTED = -1;

        //private RecyclerView recyclerView;
        private int selectedItem = UNSELECTED;

        private List<HttpCookie> cookieList = msCookieManager.getCookieStore().getCookies();;
        private String userEvents = "";
        private JSONArray userEventsArray = null;

        public SimpleAdapter(RecyclerView recyclerView) throws JSONException {
            //this.recyclerView = recyclerView;

            for (int i = 0; i < cookieList.size(); i++) {
                if (cookieList.get(i).getName().equals("pastEvents")){
                    userEvents = cookieList.get(i).getValue();
                    break;
                }
            }
            userEventsArray = new JSONArray(userEvents);
        }

        @Override
        public NavigationDrawerThird.SimpleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recycler_item, parent, false);
            return new NavigationDrawerThird.SimpleAdapter.ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(NavigationDrawerThird.SimpleAdapter.ViewHolder holder, int position) {

            JSONObject eventObject;
            Event userEvent = new Event();

            try {
                eventObject = (JSONObject) userEventsArray.get(position);
                userEvent.setName(eventObject.getString("name"));
                userEvent.setDescription(eventObject.getString("description"));
                userEvent.setId(eventObject.getString("_id"));
                userEvent.setEventRate(Double.parseDouble(eventObject.getString("rate")));

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
            private TextView eventGeneralRate;
            private String eventId = "";
            private TextView currentRate;
            private RatingBar ratingBar;
            private Button submitButton;
            private Context context;
            private int position;

            public ViewHolder(View itemView) {
                super(itemView);

                expandableLayout = (ExpandableLayout) itemView.findViewById(R.id.expandable_layout);
                expandableLayout.setInterpolator(new OvershootInterpolator());
                context = itemView.getContext();
                submitButton = (Button) itemView.findViewById(R.id.submit_rate_button);
                ratingBar = (RatingBar) itemView.findViewById(R.id.ratingBar);
                expandButton = (TextView) itemView.findViewById(R.id.expand_button);
                eventDescription = (TextView) itemView.findViewById(R.id.event_description);
                currentRate = (TextView) itemView.findViewById(R.id.current_rate);
                eventGeneralRate = (TextView) itemView.findViewById(R.id.general_rate);
                currentRate.setText(R.string.currentRateText);
                ratingBar.setVisibility(View.VISIBLE);
                currentRate.setVisibility(View.VISIBLE);
                submitButton.setVisibility(View.VISIBLE);
                ratingBar.setStepSize((float) 1.0);

                ratingBar.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_UP) {
                            float touchPositionX = event.getX();
                            float width = ratingBar.getWidth();
                            float starsf = (touchPositionX / width) * 5.0f;
                            int stars = (int)starsf + 1;
                            ratingBar.setRating(stars);
                            //ratingBar.se

                            currentRate.setText(String.valueOf(ratingBar.getRating()));
                            v.setPressed(false);
                        }
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            v.setPressed(true);
                        }

                        if (event.getAction() == MotionEvent.ACTION_CANCEL) {
                            v.setPressed(false);
                        }

                        return true;
                    }
                });
                expandButton.setOnClickListener(this);


                submitButton.setOnClickListener(new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        currentRate.setText(String.valueOf(ratingBar.getRating()));
                        EventBackgroundTask rateTask = new EventBackgroundTask();
                        PersonBackgroundTask refresh = new PersonBackgroundTask(getContext());
                        Log.d("rating: ", currentRate.getText().toString());
                        Log.d("rating int : ",String.valueOf((int)(ratingBar.getRating())));
                        Log.d("eventId: ", eventId);
                        rateTask.execute("5",String.valueOf((int)(ratingBar.getRating())), eventId);
                        selectedItem = UNSELECTED;
                        expandableLayout.collapse(false);
                        Toast.makeText(context,"Event Rated", Toast.LENGTH_LONG).show();
                        refresh.execute("3");

                        try {
                            mAdapter = new NavigationDrawerThird.SimpleAdapter(recyclerView);
                            recyclerView.setAdapter(mAdapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //mAdapter.notifyDataSetChanged();

                    }
                });
            }

            public void bind(int position, Event event) {
                this.position = position;

                expandButton.setText(event.getName());

                expandButton.setSelected(false);
                eventDescription.setText(event.getDescription());
                eventId = event.getId();
                eventGeneralRate.setText(String.valueOf( event.getEventRate()));

                Log.d("eventId: ", eventId);
                expandableLayout.collapse(false);
            }

            @Override
            public void onClick(View view) {
                NavigationDrawerThird.SimpleAdapter.ViewHolder holder = (NavigationDrawerThird.SimpleAdapter.ViewHolder) recyclerView.findViewHolderForAdapterPosition(selectedItem);
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
