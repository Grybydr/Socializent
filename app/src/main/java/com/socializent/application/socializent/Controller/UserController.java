package com.socializent.application.socializent.Controller;

import android.app.Application;
import android.app.DownloadManager;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.socializent.application.socializent.Modal.Event;
import com.socializent.application.socializent.Modal.Person;
import com.socializent.application.socializent.Template;

import org.gradle.wrapper.Download;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Irem on 7.4.2017.
 */

public class UserController extends Application{
    //appi açan user
    Person activeUserOnSystem;
    JSONObject userServerObject;
    //String accessToken;
    //private Context mContext;
    String result;
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    public UserController() {

    }


    public String login(final String username, final String password, Context context) throws IOException, InterruptedException {

       try {
           URL url = new URL("http://54.69.152.154:3000/signin");
           HttpURLConnection client = (HttpURLConnection) url.openConnection();

           client.setRequestMethod("POST");
           //client.setRequestProperty("username",username);
           //client.setRequestProperty("password",password);
           client.setDoOutput(true);

           OutputStream outputPost = new BufferedOutputStream(client.getOutputStream());



           JSONObject root = new JSONObject();
           root.put("username", username) ;
           root.put("password", password);

           String str = root.toString();
           byte[] outputBytes = str.getBytes("UTF-8");
           outputPost.write(outputBytes);
           outputPost.flush();
           outputPost.close();

           int responseCode = client.getResponseCode();
           Log.d( "13 - responseCode : " , ""+responseCode);
           String line;
           BufferedReader br = new BufferedReader(new InputStreamReader(
                   client.getInputStream()));
           while ((line = br.readLine()) != null) {
               result += line;
           }
       }catch (Exception e){

       }

        return result;
      /*  RequestQueue mRequestQueue;

        // Instantiate the cache
            Cache cache = new DiskBasedCache(context.getCacheDir(), 1024 * 1024); // 1MB cap

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
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d("Response is: ", response);

                        accessToken = Integer.valueOf(response);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error: ", error.getMessage());
            }
        }
        ){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", username);
                params.put("password", password);

                return params;
            }
        };
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
