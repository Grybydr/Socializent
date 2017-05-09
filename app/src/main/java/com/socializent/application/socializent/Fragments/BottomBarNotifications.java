package com.socializent.application.socializent.Fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.socializent.application.socializent.Controller.EventAdapterToList;
import com.socializent.application.socializent.Controller.NotificationAdapterToList;
import com.socializent.application.socializent.Controller.PersonBackgroundTask;
import com.socializent.application.socializent.Modal.Event;
import com.socializent.application.socializent.Modal.EventTypes;
import com.socializent.application.socializent.Modal.Person;
import com.socializent.application.socializent.R;
import com.socializent.application.socializent.SignUp;
import com.socializent.application.socializent.main;

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

/**
 * Created by Irem on 13.3.2017.
 */

public class BottomBarNotifications  extends Fragment {
    View notificationView;
    ListView notificationListView;
    List<HttpCookie> cookieList;
    NotificationAdapterToList1 notficationListAdapter;
    NotificationAdapterToList adapter;
    ArrayList<Person> friendRequests;
    String user = "";
    String request = "";
    JSONObject userObject = null;
    public static String activeUserId;
    PersonBackgroundTask  task;
    PersonBackgroundTask refresh;
    Person s;

    public  ImageButton acceptButton,rejectButton;
    public  TextView notification;

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
        notification = (TextView)LayoutInflater.from(getActivity()).inflate(R.layout.list_notification, null).findViewById(R.id.add_user_rext);
        cookieList = msCookieManager.getCookieStore().getCookies();
        friendRequests = new ArrayList<Person>();
        new NotificationAdapterToList1(getContext()).execute();

        notificationListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                public void onItemClick(AdapterView arg0, View arg, final int position, long a) {

                    final int p = position;
                    s = (Person)adapter.getItem(position);
                    Log.d("Bastımm", "holey");

                    AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
                    View mView = getActivity().getLayoutInflater().inflate(R.layout.friend_req_dialog, null);
                    Button dialogOK = (Button) mView.findViewById(R.id.signUpOK);
                    Button dialogCancel = (Button) mView.findViewById(R.id.signUpCancel);

                    mBuilder.setView(mView);
                    final AlertDialog dialog = mBuilder.create();

                    dialogOK.setOnClickListener(new View.OnClickListener(){

                        @Override
                        public void onClick(View view) {
                            task = new PersonBackgroundTask(getContext());
                            refresh = new PersonBackgroundTask(getContext());
                            task.execute("4",s.getId(),"1");
                            refresh.execute("3");
                            dialog.dismiss();
                            friendRequests.remove(position);
                            adapter = new NotificationAdapterToList(getContext(), friendRequests);
                            notificationListView.setAdapter(adapter);

                        }
                    });
                    dialogCancel.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View view) {
                            task = new PersonBackgroundTask(getContext());
                            refresh = new PersonBackgroundTask(getContext());
                            task.execute("4",s.getId(),"0");
                            refresh.execute("3");
                            dialog.dismiss();
                            friendRequests.remove(position);
                            adapter = new NotificationAdapterToList(getContext(), friendRequests);
                            notificationListView.setAdapter(adapter);
                        }
                    });

                    dialog.show();



                }

            });

    }


    private class NotificationAdapterToList1  extends AsyncTask<Object, Object, ArrayList<Person>> {

        public ProgressDialog p_dialog;
        public Context context;

        public NotificationAdapterToList1(Context context) {
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

            searchHere();
            return friendRequests;

        }

        @Override
        protected void onProgressUpdate(Object... values) {
            this.p_dialog.show();
        }


        protected void onPostExecute(ArrayList<Person> result) {
            adapter = new NotificationAdapterToList(context, friendRequests);
            notificationListView.setAdapter(adapter);

            if (p_dialog != null && p_dialog.isShowing())
                p_dialog.dismiss();

        }

        public ArrayList<Person> searchHere() {


            for (int i = 0; i < cookieList.size(); i++) {
                if (cookieList.get(i).getName().equals("user")) {
                    user = cookieList.get(i).getValue();
                }
                if (cookieList.get(i).getName().equals("friendRequest")) {
                    request = cookieList.get(i).getValue();
                }
            }
            try {
                userObject = new JSONObject(user);
                activeUserId = userObject.getString("_id");
                JSONArray a = new JSONArray(request);
                for (int n = 0; n < a.length(); n++) {
                    String id = a.getJSONObject(n).getString("_id");
                    String name = a.getJSONObject(n).getString("firstName");
                    String surname = a.getJSONObject(n).getString("lastName");
                    Person p = new Person(id, name, surname, "", 0, "", "", "", null, null, null, null, null, null);
                    friendRequests.add(p);

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return friendRequests;
        }
    }


}