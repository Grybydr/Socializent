package com.socializent.application.socializent.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.socializent.application.socializent.Controller.PersonBackgroundTask;
import com.socializent.application.socializent.Controller.UserAdapterToList;
import com.socializent.application.socializent.Modal.Person;
import com.socializent.application.socializent.R;

import static com.socializent.application.socializent.Fragments.BottomBarSearch.searchedPerson;

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
import java.util.ArrayList;
import java.util.List;

import static com.socializent.application.socializent.Controller.PersonBackgroundTask.msCookieManager;

public class NavigationDrawerFourth extends Fragment {
    ListView friendList;
    View friendView;
    List<HttpCookie> cookieList;
    String user = "";
    JSONObject userObject = null;
    String activeUserId;
    ArrayList<Person> friendObjList;
    UserAdapterToList userAdapter;
    String accessToken;

    public NavigationDrawerFourth() {
        // Required empty public constructor
    }

    public static NavigationDrawerFourth newInstance() {
        return new NavigationDrawerFourth();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        friendView = inflater.inflate(R.layout.navigation_drawer_fourth_frag, container, false);
        return friendView;
    }
    public void onViewCreated (View view, Bundle savedInstanceState) {

       // friendList = getListView();
        friendList = (ListView) friendView.findViewById(R.id.friendList);
        cookieList = msCookieManager.getCookieStore().getCookies();

        friendObjList = new ArrayList<>();
        for (int i = 0; i < cookieList.size(); i++) {
            if (cookieList.get(i).getName().equals("user")) {
                user = cookieList.get(i).getValue();
            }
        }
        try {
            userObject = new JSONObject(user);
            activeUserId = userObject.getString("_id");
            new SearchUserForList(getContext()).execute();

        } catch (JSONException e) {
            e.printStackTrace();
        }

        friendList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView arg0, View arg, int position, long a) {

                searchedPerson = (Person)userAdapter.getItem(position);

                Fragment mFragment = new NavigationDrawerFirst();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.content_frame, mFragment);
                transaction.commit();


            }
        });

    }

    private class SearchUserForList extends AsyncTask<Object, Object, ArrayList<Person>> {

        public ProgressDialog p_dialog;
        public Context context;

        public SearchUserForList(Context context){
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
            getUsersProfile();
            return  friendObjList;

        }
        @Override
        protected void onProgressUpdate(Object... values) {
            this.p_dialog.show();
        }

        @Override
        protected void onPostExecute(ArrayList<Person> result) {
            userAdapter = new UserAdapterToList(context, friendObjList);
            friendList.setAdapter(userAdapter);

            if (p_dialog != null && p_dialog.isShowing())
                p_dialog.dismiss();

        }
        public ArrayList<Person> getUsersProfile() {
            String result = "";
            URL url = null;
            try {

                url = new URL("http://54.69.152.154:3000/friendList?id=" + activeUserId);
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
                JSONArray usersArray = new JSONArray(result);
                usersArray.length();
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
                    for (int n = 0; n < interestA.length();n++) {

                        //JSONObject interest = interestA.getJSONObject(k);
                        interestArray.add(interestA.getString(n));
                    }
                    String friendReqJSONArray2 = row.getString("friendRequests");
                    JSONArray frienReqA2 = new JSONArray(friendReqJSONArray2);
                    ArrayList<String> frienReqArray2 = new ArrayList<String>();
                    for (int l = 0; l < frienReqA2.length();l++) {
                        frienReqArray2.add(frienReqA2.getString(l));
                    }

                    String friendJSONArray2 = row.getString("friends");
                    JSONArray friendA2 = new JSONArray(friendJSONArray2);
                    ArrayList<String> friendArray2 = new ArrayList<String>();
                    for (int m = 0; m < friendA2.length(); m++) {

                        friendArray2.add(friendA2.getString(m));
                    }

                    Person p = new Person(id,firstName, lastname, username, bd, password, email, bio,friendArray2, interestArray, null, null, null, frienReqArray2);
                    friendObjList.add(p);
                    friendObjList.size();
                }

                return friendObjList;

            } catch (MalformedURLException er) {
                er.printStackTrace();
            } catch (IOException er) {
                er.printStackTrace();
            } catch (JSONException er) {
                er.printStackTrace();
            }
            return friendObjList;
        }
    }
}
