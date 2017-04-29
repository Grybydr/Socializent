package com.socializent.application.socializent.Fragments;

/**
 * Created by Irem on 13.3.2017.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TabHost;

import com.socializent.application.socializent.Controller.EventAdapterToList;
import com.socializent.application.socializent.Controller.EventBackgroundTask;
import com.socializent.application.socializent.Controller.PersonBackgroundTask;
import com.socializent.application.socializent.Controller.UserAdapterToList;
import com.socializent.application.socializent.Modal.Event;
import com.socializent.application.socializent.Modal.EventTypes;
import com.socializent.application.socializent.Modal.Person;
import com.socializent.application.socializent.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpCookie;
import java.util.ArrayList;
import java.util.List;

import static com.socializent.application.socializent.Controller.PersonBackgroundTask.msCookieManager;


public class BottomBarSearch extends ListFragment  {

    View searchview;
    EditText searchedString;
    ImageButton searchButton;
    TabHost.TabSpec spec;
    TabHost host;

    ListView eventList;
    ListView userList;
    ArrayList<Event> searchedEvents;
    ArrayList<Person> searchedUsers;

    List<HttpCookie> cookieList;

    EventBackgroundTask task;
    PersonBackgroundTask personTask;

    EventAdapterToList adapter;
    UserAdapterToList userAdapter;

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


        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    int tab = host.getCurrentTab();
                    if(tab == 0) { // 0= event , 1 = user
                        searchEvent(searchedString.getText().toString());
                        adapter = new EventAdapterToList(getActivity().getApplicationContext(), searchedEvents);
                        eventList.setAdapter(adapter);
                        searchedEvents.size();
                    }
                    else if(tab == 1){
                        searchPerson(searchedString.getText().toString());
                        userAdapter = new UserAdapterToList(getActivity().getApplicationContext(), searchedUsers);
                        userList.setAdapter(userAdapter);
                        searchedUsers.size();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        eventList.setOnItemClickListener(new OnItemClickListener() {

            public void onItemClick(AdapterView arg0, View arg, int position, long a) {

                Event selectedEvent = (Event)adapter.getItem(position);

                Fragment mFragment = EventDetailsPage.newInstance();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.tabhost, mFragment);
                transaction.commit();


            }
        });
        userList.setOnItemClickListener(new OnItemClickListener() {

            public void onItemClick(AdapterView arg0, View arg, int position, long a) {

                Person selectedPerson = (Person)userAdapter.getItem(position);

                Fragment mFragment = NavigationDrawerFirst.newInstance();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.tabhost, mFragment);
                transaction.commit();


            }
        });

        //Tab 2
        spec = host.newTabSpec("User");
        spec.setContent(R.id.layoutTab2);
        spec.setIndicator("User");
        host.addTab(spec);

    }

    public void searchEvent(String searchedWord) throws JSONException {
        //Irem bunu ekle senin class a
        task = new EventBackgroundTask();
        task.execute("3",searchedWord);
        cookieList = msCookieManager.getCookieStore().getCookies();
        String events = "";
        for (int i = 0; i < cookieList.size(); i++) {
            if (cookieList.get(i).getName().equals("allSearchedEvents")){
                events = cookieList.get(i).getValue();
                break;
            }
        }
        JSONArray eventsArray = new JSONArray(events);
        for (int i = 0; i < eventsArray.length(); i++) {
            JSONObject row = eventsArray.getJSONObject(i);

            String eventTitle = row.getString("name");
            if (eventTitle == "null" || eventTitle.isEmpty() || eventTitle == "" )
                eventTitle = "Event";
            String description = row.getString("description");
            EventTypes type= EventTypes.STUDY; //= row.getString() //TODO: serverdan gelmesi lazm
            Event e = new Event(eventTitle, description, 0, null, "", null, null, type, 0,0, "", null, null); //TODO:
            searchedEvents.add(e);

        }
    }


    public void searchPerson(String searchedWord) throws JSONException {

        personTask = new PersonBackgroundTask(getContext());
        personTask.execute("4",searchedWord);
        cookieList = msCookieManager.getCookieStore().getCookies();
        String users = "";
        for (int i = cookieList.size()-1; i >=0; i--) {
            if (cookieList.get(i).getName().equals("searchedUsers")){
                users = cookieList.get(i).getValue();
                break;
            }
        }
        JSONArray usersArray = new JSONArray(users);
        for (int i = 0; i < usersArray.length(); i++) {
            JSONObject row = usersArray.getJSONObject(i);
            String fullname = row.getString("fullName");
            String firstName = row.getString("firstName");
            String lastname = row.getString("lastName");
            String email = row.getString("email");
            String shortBio = row.getString("shortBio");
            String username = row.getString("username");
            String password = row.getString("password");
            String bio = row.getString("shortBio");


            String interestJSONArray = row.getString("interests");
            JSONArray interestA = new JSONArray(interestJSONArray);
            ArrayList<String> interestArray = new ArrayList<String>();
            for (int k = 0; k < interestA.length(); k++) {
                JSONObject interest = interestA.getJSONObject(k);
                interestArray.add(interest.toString());
            }
           // String orgEventJSON = row.getString("organizedEvents");
            //friends arrayı ekllenecek
            //events ekleencek//
            //Person(String firstName, String lastName, String username, float birthDate, String password, String email,String bio,  ArrayList<Person> friends, ArrayList<String> interests,ArrayList<Event> events,ArrayList<Event> upcomingEvents,ArrayList<Event> pastEvents,double rate) {


             Person p = new Person(firstName,lastname,username, 0,password, email,bio, null, interestArray,null,null,null,0);
            searchedUsers.add(p);


        }



    }


}