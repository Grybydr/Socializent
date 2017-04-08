package com.socializent.application.socializent.Controller;

import android.app.Application;
import android.app.DownloadManager;
import android.content.Context;
import android.util.Log;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.socializent.application.socializent.Modal.Event;
import com.socializent.application.socializent.Modal.Person;
import com.socializent.application.socializent.Template;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Irem on 7.4.2017.
 */

public class UserController extends Application{
    //appi açan user
    Person activeUserOnSystem;
    JSONObject userServerObject;
    private int accessToken;
    private Context mContext = getApplicationContext();

    public UserController() {

    }
    public int login(String username,String password) throws IOException {

        RequestQueue mRequestQueue;

        // Instantiate the cache
            Cache cache = new DiskBasedCache(mContext.getCacheDir(), 1024 * 1024); // 1MB cap

        // Set up the network to use HttpURLConnection as the HTTP client.
                Network network = new BasicNetwork(new HurlStack());

        // Instantiate the RequestQueue with the cache and network.
                mRequestQueue = new RequestQueue(cache, network);

        // Start the queue
        mRequestQueue.start();


        // Instantiate the RequestQueue.
        //RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://54.69.152.154:3000/signin";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Log.d("Response is: ", response.substring(0,500));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error: ", error.getMessage());
            }
        });
        // Add the request to the RequestQueue.
        mRequestQueue.add(stringRequest);


       /* URL url = new URL("http://54.69.152.154:3000/signin");
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        try {

            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            Log.d("READ FROM SERVER: ", ((String) in.read()));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            urlConnection.disconnect();
        }
        */
        return accessToken;
    }
    public Person getUserFromServer(String username){

        // GÜRAY buraya server requesti koycak
        //requestten sonra userın bütün bilgileri gelecek
        //URL url = new URL("http://54.69.152.154:3000/getUser");
        //muhtemelen serverdan kullanıcının arkadaşlarını çektiğimizde json objesi olarak gelecek onlar
        //o objeleri de ArrayList<Person> friends'e çevirmemiz gerekiyor
        //aynı şekilde interestAreas ı da arrayListe çevirmemiz gerekiyor

        //public Person(String name, String surname, String username, String birthdate, String password, String mailAddress, ArrayList<Person> friends, ArrayList<String> interestAreas)
        activeUserOnSystem = new Person("İrem", "Herguner", username, "28/02/1994", "adad", "gmail", null, null);
        return activeUserOnSystem;

    }
   // public ArrayList<Event> userPastEvents(String username){
        //burada userın ismine göre geçmiş eventlerini getirecek request atılacak

   // }
}
