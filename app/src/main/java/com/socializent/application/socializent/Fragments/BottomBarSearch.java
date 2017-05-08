package com.socializent.application.socializent.Fragments;

/**
 * Created by Irem on 13.3.2017.
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.socializent.application.socializent.Controller.EventAdapterToList;
import com.socializent.application.socializent.Controller.UserAdapterToList;
import com.socializent.application.socializent.Modal.Event;
import com.socializent.application.socializent.Modal.EventTypes;
import com.socializent.application.socializent.Modal.Person;
import com.socializent.application.socializent.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.Normalizer;
import java.util.ArrayList;

public class BottomBarSearch extends ListFragment  {

    View searchview;
    EditText searchedString;
    ImageButton searchButton;
    TabHost.TabSpec spec;
    TabHost host;

    public static ListView eventList;
    ListView userList;
    ArrayList<Event> searchedEvents;
    ArrayList<Person> searchedUsers;


    EventAdapterToList adapter;
    UserAdapterToList userAdapter;

    static Person searchedPerson = null;

    public static BottomBarSearch newInstance() {
        BottomBarSearch fragment = new BottomBarSearch();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        searchview = inflater.inflate(R.layout.bottom_bar_search_fragment, container, false);
        return searchview;
    }

    public void onViewCreated (View view, Bundle savedInstanceState) {
        searchedString = (EditText) searchview.findViewById(R.id.searchText);
        searchButton = (ImageButton) searchview.findViewById(R.id.searchImageButton);
        eventList = getListView();
        userList = (ListView) searchview.findViewById(R.id.listuser);

        searchedEvents = new ArrayList<Event>();
        searchedUsers = new ArrayList<Person>();
        host = (TabHost)searchview.findViewById(R.id.tabhost);
        host.setup();

        //Tab 1
        spec = host.newTabSpec("Event");
        spec.setContent(R.id.layoutTab1);
        spec.setIndicator("Event");
        host.addTab(spec);
        TextView event = (TextView) host.getTabWidget().getChildAt(0).findViewById(android.R.id.title);
        event.setTextColor(Color.parseColor("#ebca0707"));
        host.setCurrentTab(0);
        host.setOnTabChangedListener(new TabHost.OnTabChangeListener(){
            @Override
            public void onTabChanged(String tabId) {
                if(host.getCurrentTab() == 0) {
                    //destroy earth
                    changeTabColor(0);
                }
                if(host.getCurrentTab() == 1) {
                    //destroy mars
                    changeTabColor(1);
                }
            }});


        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchedEvents.clear();
                searchedUsers.clear();

                    if(host.getCurrentTab() == 0) { // 0= event , 1 = user
                        new SearchInnerTask(getContext()).execute(searchedString.getText().toString());
                        /*if(searchedEvents.size() == 0)
                            Toast.makeText(getContext(),"There is no event to show!", Toast.LENGTH_LONG).show();*/

                    }
                    else if(host.getCurrentTab() == 1){
                        new SearchUserTask(getContext()).execute(searchedString.getText().toString());

                    }

                host.setOnTabChangedListener(new TabHost.OnTabChangeListener(){
                    @Override
                    public void onTabChanged(String tabId) {
                        if(host.getCurrentTab() == 0) {
                            changeTabColor(0);
                            eventList.setAdapter(adapter);
                        }
                        if(host.getCurrentTab() == 1) {
                            //destroy mars
                            changeTabColor(1);
                            userList.setAdapter(userAdapter);
                        }
                    }});

            }
        });
        eventList.setOnItemClickListener(new OnItemClickListener() {

            public void onItemClick(AdapterView arg0, View arg, int position, long a) {

                Event selectedEvent = (Event)adapter.getItem(position);
                Fragment mFragment = EventDetailsPage.newInstance("", selectedEvent);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.content_frame, mFragment);
                transaction.commit();

            }
        });
        userList.setOnItemClickListener(new OnItemClickListener() {

            public void onItemClick(AdapterView arg0, View arg, int position, long a) {

                searchedPerson = (Person)userAdapter.getItem(position);

                Fragment mFragment = new NavigationDrawerFirst();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.content_frame, mFragment);
                transaction.commit();


            }
        });

        //Tab 2
        spec = host.newTabSpec("User");
        spec.setContent(R.id.layoutTab2);
        spec.setIndicator("User");
        host.addTab(spec);

    }


    public void changeTabColor(int tabNum){
        TextView user = (TextView) host.getTabWidget().getChildAt(1-tabNum).findViewById(android.R.id.title);
        user.setTextColor(Color.parseColor("#000000"));
        TextView event = (TextView) host.getTabWidget().getChildAt(tabNum).findViewById(android.R.id.title);
        event.setTextColor(Color.parseColor("#ebca0707"));

    }

    private class SearchInnerTask extends AsyncTask<Object, Object, ArrayList<Event>> {

        public ProgressDialog p_dialog;
        public Context context;

        public SearchInnerTask(Context context){
            this.context = context;
            this.p_dialog = new ProgressDialog(context);
        }

        @Override
        protected void onPreExecute() {
            this.p_dialog.setMessage(context.getResources().getString(R.string.loading));
            this.p_dialog.show();
        }

        @Override
        protected ArrayList<Event> doInBackground(Object... params) {

            Object queryString = params[0];
            searchHere(queryString);
            return  searchedEvents;

        }
        @Override
        protected void onProgressUpdate(Object... values) {
            this.p_dialog.show();
        }

        @Override
        protected void onPostExecute(ArrayList<Event> result) {
            adapter = new EventAdapterToList(context, searchedEvents);
            eventList.setAdapter(adapter);

            if (p_dialog != null && p_dialog.isShowing())
                p_dialog.dismiss();

        }
        public ArrayList<Event> searchHere(Object queryString) {
            String result = "";
            URL url = null;
            Event e = null;
            try {
                url = new URL("http://54.69.152.154:3000/searchEvent?q=" + queryString);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                conn.setReadTimeout(30000);
                conn.setConnectTimeout(30000);
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Content-Type", "application/json");

                conn.setDoInput(true);
                conn.connect();

                int responseCode = conn.getResponseCode();
                Log.d("Response Code: ", responseCode + "");
                //if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line = br.readLine()) != null) {
                    result += line;
                }
                Log.d("allSearchedEvents: ", result);

                JSONArray eventsArray = new JSONArray(result);
                for (int i = 0; i < eventsArray.length(); i++) {
                    JSONObject row = eventsArray.getJSONObject(i);

                    String eventTitle = row.getString("name");
                    if (eventTitle == "null" || eventTitle.isEmpty() || eventTitle == "")
                        eventTitle = "Event";
                    String description = row.getString("description");
                    String typeS = row.getString("category").toUpperCase();
                    if (typeS == "null" || typeS.isEmpty() || typeS == "")
                        typeS = "CONFERENCE";
                    typeS = Normalizer.normalize(typeS, Normalizer.Form.NFD);
                    EventTypes type = EventTypes.valueOf(typeS);


                    JSONObject pl = row.getJSONObject("place");
                    String tempLat = pl.getString("latitude");
                    String tempLong = pl.getString("longitude");
                    String city = pl.getString("city");
                    String address = pl.getString("address");
                    String placeName = pl.getString("name");
                    Location l = new Location("");
                    l.setLatitude(Double.parseDouble(tempLat));
                    l.setLongitude(Double.parseDouble(tempLong));
                    double fee = Double.parseDouble(row.getString("fee"));
                    String tempDate = row.getString("date");
                      //TODO:: Organizer id
                    String organizer = row.getString("organizer");
                    long millis = Long.parseLong(tempDate);
                    int partCount = Integer.parseInt(row.getString("participantCount"));
                    String part = row.getString("participants");
                    JSONArray participants =  new JSONArray(part);
                    String id = row.getString("_id");
                    //String name, String description, double fee, long date, String address,  String organizerId, EventTypes category, int eventRate, int participantCount,  ArrayList<String> comments, String photoUrl, Location l, String city, String placeName) {

                    e = new Event(id,eventTitle, description, fee, millis, address,organizer, type,0, partCount, null, "", l, city,placeName, participants);
                    conn.disconnect();
                    searchedEvents.add(e);
                }
            } catch (MalformedURLException er) {
                er.printStackTrace();
            } catch (IOException er) {
                er.printStackTrace();
            } catch (JSONException er) {
                er.printStackTrace();
            }
            return searchedEvents;
        }
    }



    private class SearchUserTask extends AsyncTask<Object, Object, ArrayList<Person>> {

        public ProgressDialog p_dialog;
        public Context context;

        public SearchUserTask(Context context){
            this.context = context;
            this.p_dialog = new ProgressDialog(context);
        }

        @Override
        protected void onPreExecute() {
            this.p_dialog.setMessage(context.getResources().getString(R.string.loading));
            this.p_dialog.show();
        }

        @Override
        protected ArrayList<Person> doInBackground(Object... params) {
            Object queryString = params[0];
            searchPersonHere(queryString);
            return  searchedUsers;

        }
        @Override
        protected void onProgressUpdate(Object... values) {
            this.p_dialog.show();
        }

        @Override
        protected void onPostExecute(ArrayList<Person> result) {
            userAdapter = new UserAdapterToList(context, searchedUsers);
            userList.setAdapter(userAdapter);

            if (p_dialog != null && p_dialog.isShowing())
                p_dialog.dismiss();

        }
        public ArrayList<Person> searchPersonHere(Object queryString) {
            String result = "";
            URL url = null;
            try {

                url = new URL("http://54.69.152.154:3000/searchUser?q=" + queryString);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                //conn.connect();
                conn.setReadTimeout(30000);
                conn.setConnectTimeout(30000);
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Content-Type", "application/json");
                //conn.setRequestProperty("x-access-token", accessToken.toString());
                conn.setDoInput(true);
                conn.connect();

                int responseCode = conn.getResponseCode();
                Log.d("Response Code: ", responseCode + "");
                //if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line = br.readLine()) != null) {
                    result += line;
                }

                JSONArray usersArray = new JSONArray(result);
                for (int i = 0; i < usersArray.length(); i++) {
                    JSONObject row = usersArray.getJSONObject(i);
                    String id = row.getString("_id");
                    String fullname = row.getString("fullName");
                    String firstName = row.getString("firstName");
                    String lastname = row.getString("lastName");
                    String email = row.getString("email");
                    String username = row.getString("username");
                    String password = row.getString("password");
                    String bio = row.getString("shortBio");

                    String birthday = row.getString("birthDate");;
                    float bd = Float.parseFloat(birthday);

                    String interestJSONArray = row.getString("interests");
                    JSONArray interestA = new JSONArray(interestJSONArray);
                    ArrayList<String> interestArray = new ArrayList<String>();
                    for (int k = 0; k < interestA.length(); k++) {
                        interestArray.add(interestA.getString(k));
                    }
                    String friendReqJSONArray = row.getString("friendRequests");
                    JSONArray frienReqA = new JSONArray(friendReqJSONArray);
                    ArrayList<String> frienReqArray = new ArrayList<String>();
                    for (int k = 0; k < frienReqA.length(); k++) {

                        frienReqArray.add(frienReqA.getString(k));
                    }

                    String friendJSONArray = row.getString("friends");
                    JSONArray friendA = new JSONArray(friendJSONArray);
                    ArrayList<String> friendArray = new ArrayList<String>();
                    for (int k = 0; k < friendA.length(); k++) {

                        friendArray.add(friendA.getString(k));
                    }

                  /*  ArrayList<Person> friends = new ArrayList<Person>();
                    Object obj = row.get("friends");
                    JSONArray jsonArray = (JSONArray)obj;

                    for (int p = 0; p < jsonArray.length(); p++) {
                        friends.add((Person)jsonArray.get(i));
                    }*/

                    Person p = new Person(id,firstName, lastname, username, bd, password, email, bio,friendArray, interestArray, null, null, null, frienReqArray);
                    searchedUsers.add(p);

                }
            } catch (MalformedURLException er) {
                er.printStackTrace();
            } catch (IOException er) {
                er.printStackTrace();
            } catch (JSONException er) {
                er.printStackTrace();
            }
            return searchedUsers;
        }
    }
}