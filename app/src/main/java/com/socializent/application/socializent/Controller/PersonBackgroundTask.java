package com.socializent.application.socializent.Controller;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;


import com.socializent.application.socializent.Modal.Person;
import com.socializent.application.socializent.Template;
import com.socializent.application.socializent.main;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import static com.socializent.application.socializent.Controller.EventBackgroundTask.GET_ALL_EVENTS_OPTION;
import static com.socializent.application.socializent.Template.user;

/**
 * Created by Toshıba on 04/09/2017.
 */

public class PersonBackgroundTask extends AsyncTask<String, Object, String> {

    private Context context;
    final static String COOKIES_HEADER = "Set-Cookie";
    final static String SIGN_IN_OPTION = "2";
    final static String GET_PERSON_OPTION = "3";
    final static String SIGN_UP_OPTION = "1";
    private static int signedInBefore = 0;

    public static java.net.CookieManager msCookieManager = new java.net.CookieManager();
    public PersonBackgroundTask(Context context){
        this.context=context;
    }

    @Override
    protected String doInBackground(String... params) {
        String result = "";
        String type = params[0];
        Log.d("Type(0Signup1Signin): ", type);

        if(type.equals(SIGN_UP_OPTION)){
            try
            {
                String name = params[1];
                String surname = params[2];
                String username = params[3];
                String password = params[4];
                String email = params[5];

                URL url = new URL("http://54.69.152.154:3000/signup");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                //conn.connect();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                //HashMap<String, String> postDataParams = new HashMap<String, String>();
                //postDataParams.put("username", username);
                // postDataParams.put("password", password);
                Log.d("username", username);
                Log.d("password", password);
                Log.d("firstname", name);
                Log.d("lastname", surname);
                Log.d("email", email);

                JSONObject requestBody = new JSONObject();

                requestBody.put("username", username);
                requestBody.put("firstname", name);
                requestBody.put("lastname", surname);
                requestBody.put("password", password);
                requestBody.put("email", email);

                OutputStream os = conn.getOutputStream();

                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                String str = requestBody.toString();
                Log.d("RequestBody: ", str);
                writer.write(str);
                //writer.write(postDataParams.toString());
                writer.flush();
                writer.close();
                os.close();


                int responseCode = conn.getResponseCode();
                Log.d("Response Code: ", responseCode + "");
                //if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line = br.readLine()) != null) {
                    result += line;
                }
                Log.d("Response5: ", result);
                //}
                conn.disconnect();
                return result;
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else if(type.equals(SIGN_IN_OPTION)){
            try {

                String username = params[1];
                String password = params[2];

                URL url = new URL("http://54.69.152.154:3000/signin");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                //conn.connect();
                conn.setReadTimeout(30000);
                conn.setConnectTimeout(30000);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoInput(true);
                conn.setDoOutput(true);



                //HashMap<String, String> postDataParams = new HashMap<String, String>();
                //postDataParams.put("username", username);
                // postDataParams.put("password", password);

                Log.d("username", username);
                Log.d("password", password);
                JSONObject requestBody = new JSONObject();
                requestBody.put("username", username);
                requestBody.put("password", password);
                OutputStream os = conn.getOutputStream();

                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                String str = requestBody.toString();
                Log.d("requestbody: ", str);
                writer.write(str);
                //writer.write(postDataParams.toString());
                writer.flush();
                writer.close();
                os.close();


                int responseCode = conn.getResponseCode();
                Log.d("Response Code: ", responseCode + "");
                if (responseCode == HttpsURLConnection.HTTP_OK) {

                    String line;
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    while ((line = br.readLine()) != null) {
                        result += line;
                    }
                    Log.d("Response4: ", result);
                }else
                {
                    Log.d("ErrorWithResponseCode: ", responseCode +"");
                    return null;
                }
                //}
                //msCookieManager.getCookieStore().add(null,HttpCookie.parse(result).get(0));
                //Log.d("Cookie: " ,HttpCookie.parse(result).get(0).toString() );
                conn.disconnect();
                return result;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else if(type.equals(GET_PERSON_OPTION)){
            try {

                String accessToken = "";

                List<HttpCookie> cookieList = msCookieManager.getCookieStore().getCookies();

                for (int i = 0; i < cookieList.size(); i++) {
                    if (cookieList.get(i).getName().equals("x-access-token")){
                        accessToken = cookieList.get(i).getValue();
                        break;
                    }
                }
                //String accessToken = msCookieManager.getCookieStore().getCookies().get(msCookieManager.getCookieStore().getCookies().size()-1).getValue();
                //Log.d("Access Token in Event: " ,accessToken);


                //accessToken = accessToken.substring(1,accessToken.length()-1);
                Log.d("AccessTokeninGetPerson:" ,accessToken);


                URL url = new URL("http://54.69.152.154:3000/getuser");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                //conn.connect();
                conn.setReadTimeout(30000);
                conn.setConnectTimeout(30000);
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("x-access-token", accessToken.toString());
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
                Log.d("Response3: ", result);
                //}

                JSONObject userObject = new JSONObject(result);

                ArrayList<Person> friends = new ArrayList<Person>();
                Object obj = userObject.get("friends");
                JSONArray jsonArray = (JSONArray)obj;

                for (int i = 0; i < jsonArray.length(); i++) {
                    friends.add((Person)jsonArray.get(i));
                }

                ArrayList<String> interestAreas = new ArrayList<String>();
                Object obj2 = userObject.get("interests");
                JSONArray jsonArray2 = (JSONArray)obj2;


                for (int i = 0; i < jsonArray.length(); i++) {
                    interestAreas.add(jsonArray2.getString(i));
                }


                Object object = userObject.get("upcomingEvents");
                JSONArray upcomingEventsArray = (JSONArray) object;
                Log.d("upcomingEVENTS: ", upcomingEventsArray.toString());
                /*user = new Person(userObject.getString("firstName"),userObject.getString("lastName"),
                        userObject.getString("fullName"),userObject.getString("birthDate"),userObject.getString("password"),
                        userObject.getString("email"),friends,interestAreas);22*/

                JSONObject jsonObject= new JSONObject();

                jsonObject.put("firstName", userObject.getString("firstName"));
                jsonObject.put("lastName", userObject.getString("lastName"));
                jsonObject.put("fullName", userObject.getString("fullName"));
                jsonObject.put("birthDate", userObject.getString("birthDate"));
                jsonObject.put("password", userObject.getString("password"));
                jsonObject.put("email", userObject.getString("email"));
                jsonObject.put("friends", userObject.get("friends"));
                jsonObject.put("interests", userObject.get("interests"));
                //Hawk.put("user",user);

                msCookieManager.getCookieStore().add(null,new HttpCookie("user",jsonObject.toString()));
                msCookieManager.getCookieStore().add(null,new HttpCookie("userEvents",upcomingEventsArray.toString()));


                conn.disconnect();
                return result;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        return result;
    }



    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        if (result == null || result.equals("0") ){
            Toast.makeText(context,"Wrong Credentials or you are not connected to internet",Toast.LENGTH_LONG).show();
            return;
        }

        if (result.equals("1")){
            Intent forMain = new Intent(context, main.class);
            context.startActivity(forMain);
            return;
        }

        if(signedInBefore == 0){
            Log.d("Access token: ",result);

            result = result.substring(1,result.length()-1);
            HttpCookie accessTokenCookie = new HttpCookie("x-access-token",result);

            msCookieManager.getCookieStore().add(null, accessTokenCookie);

            Log.d("Cookie: " ,accessTokenCookie.getValue());
            //Toast.makeText(context,result,Toast.LENGTH_LONG).show();
            signedInBefore++;

            PersonBackgroundTask getCurrentUserTask = new PersonBackgroundTask(context);
            getCurrentUserTask.execute(GET_PERSON_OPTION);

            EventBackgroundTask getAllAvailableEvents = new EventBackgroundTask();
            getAllAvailableEvents.execute(GET_ALL_EVENTS_OPTION);

            Intent intentNavigationBar = new Intent(context, Template.class);
            context.startActivity(intentNavigationBar);

        }
        else
            Log.d("Reached end and user:",result);
    }
}