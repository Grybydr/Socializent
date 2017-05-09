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
import android.widget.ListView;
import android.widget.TextView;

import com.socializent.application.socializent.Controller.EventAdapterToList;
import com.socializent.application.socializent.Modal.Event;
import com.socializent.application.socializent.Modal.EventTypes;
import com.socializent.application.socializent.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

import static com.socializent.application.socializent.Controller.PersonBackgroundTask.msCookieManager;


public class BottomBarRecommend extends ListFragment {
    View searchview;

    ListView recommendList;
    ArrayList<Event> searchedEvents;
    EventAdapterToList adapter;
    List<HttpCookie> cookieList;
    String user = "";

    public static BottomBarRecommend newInstance() {
        BottomBarRecommend fragment = new BottomBarRecommend();
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        searchview = inflater.inflate(R.layout.bottom_bar_recommend_fragment, container, false);
        return searchview;
    }

    public void onViewCreated (View view, Bundle savedInstanceState) {

        recommendList = (ListView) searchview.findViewById(R.id.recommendList);

        searchedEvents = new ArrayList<Event>();
        new BottomBarRecommend.SearchInnerTask(getContext()).execute();

     //   TextView event = (TextView) host.getTabWidget().getChildAt(0).findViewById(android.R.id.title);
      //  event.setTextColor(Color.parseColor("#ebca0707"));

        recommendList.setOnItemClickListener(new OnItemClickListener() {

            public void onItemClick(AdapterView arg0, View arg, int position, long a) {

                Event selectedEvent = (Event)adapter.getItem(position);
                Fragment mFragment = EventDetailsPage.newInstance("", selectedEvent);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.content_frame, mFragment);
                transaction.commit();

            }
        });

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

            searchHere();
            return  searchedEvents;

        }
        @Override
        protected void onProgressUpdate(Object... values) {
            this.p_dialog.show();
        }

        @Override
        protected void onPostExecute(ArrayList<Event> result) {
            adapter = new EventAdapterToList(context, searchedEvents);
            recommendList.setAdapter(adapter);

            if (p_dialog != null && p_dialog.isShowing())
                p_dialog.dismiss();

        }
        public ArrayList<Event> searchHere() {
            String result = "";
            URL url = null;
            Event e = null;
            try {
                cookieList = msCookieManager.getCookieStore().getCookies();
                for (int i = 0; i < cookieList.size(); i++) {
                    if (cookieList.get(i).getName().equals("x-access-token")) {
                        user = cookieList.get(i).getValue();
                    }
                }

                //url = new URL("http://54.69.152.154:3000/searchEvent?q=" + queryString);
                url = new URL("http://54.69.152.154:3000/recommend");

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                conn.setReadTimeout(30000);
                conn.setConnectTimeout(30000);
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("x-access-token", user.toString());

                conn.setDoInput(true);
                conn.connect();

                int responseCode = conn.getResponseCode();
                Log.d("Response Code: ", responseCode + "");
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line = br.readLine()) != null) {
                    result += line;
                }

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


}


