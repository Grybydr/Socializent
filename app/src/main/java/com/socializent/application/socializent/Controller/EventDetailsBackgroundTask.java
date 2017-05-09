package com.socializent.application.socializent.Controller;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.socializent.application.socializent.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
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
import java.util.Date;
import java.util.List;

import static com.socializent.application.socializent.Controller.PersonBackgroundTask.msCookieManager;

/**
 * Created by Zülal Bingöl on 3.05.2017.
 */

public class EventDetailsBackgroundTask extends AsyncTask<String, Integer , String> {

    final static String JOIN_EVENT_TAG = "joinEvent";
    final static String LOAD_EVENT_TAG = "loadTargetEvent";
    final static String LEAVE_EVENT = "leaveEvent";
    final static String DELETE_EVENT = "deleteEvent";
    final static String GET_ORGANIZER_INFO = "getOrgInfo";
    final static String GET_EVENT_DETAILS = "getEventDetails";
    final static String EDIT_EVENT = "editEvent";

    public ProgressDialog p_dialog;
    public Context context;
    public String type = "";

    public EventDetailsBackgroundTask(Context context){
        this.context = context;
        this.p_dialog = new ProgressDialog(context);
    }

    @Override
    protected void onPreExecute() {
        this.p_dialog.setMessage(context.getResources().getString(R.string.loading));
        this.p_dialog.show();
    }

    @Override
    protected String doInBackground(String... params) {
        String function = params[0];
        String result_background = "";
        type = params[0];

        if(function.equals(LOAD_EVENT_TAG)) {
            try {
                 String latitude = params[1];
                 String longitude = params[2];

                 URL url = new URL("http://54.69.152.154:3000/findPosition?long=" + longitude + "&lat=" + latitude);
                 HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                 conn.setReadTimeout(30000);
                 conn.setConnectTimeout(30000);
                 conn.setRequestMethod("GET");
                 conn.setRequestProperty("Content-Type", "application/json");
                 conn.setDoInput(true);
                 conn.connect();

                 int responseCode = conn.getResponseCode();
                 //Log.d("Response Code", responseCode + "");

                 String line;
                 BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                 while ((line = br.readLine()) != null) {
                     result_background += line;
                 }
                 Log.v("DetailsBackgroundTask", result_background);
                 HttpCookie accessTokenCookie = new HttpCookie("targetEvent", result_background);
                 msCookieManager.getCookieStore().add(null, accessTokenCookie);

                 conn.disconnect();
                Log.v("DetailsBackgroundTask", "TASK FINISHED = " + type);
            } catch (ProtocolException e) {
                e.printStackTrace();
                Log.d("DetailsBackgroundTask", "protocol error");
            } catch (MalformedURLException e) {
                Log.d("DetailsBackgroundTask", "malformed URL");
                e.printStackTrace();
            } catch (IOException e) {
                Log.d("DetailsBackgroundTask", "IO exception");
                e.printStackTrace();
            }
        }
        else if (function.equals(JOIN_EVENT_TAG)) {
             try {
                 String temp_id = params[1];
                 String accessToken = "";

                 List<HttpCookie> cookieList = msCookieManager.getCookieStore().getCookies();

                 for (int i = 0; i < cookieList.size(); i++) {
                     if (cookieList.get(i).getName().equals("x-access-token")){
                         accessToken = cookieList.get(i).getValue();
                         break;
                     }
                 }

                 Log.d("DetailsBackgroundTask" , accessToken);

                 URL url = new URL("http://54.69.152.154:3000/joinEvent?id=" + temp_id);
                 HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                 conn.setReadTimeout(30000);
                 conn.setConnectTimeout(30000);
                 conn.setRequestMethod("GET");
                 conn.setRequestProperty("Content-Type", "application/json");
                 conn.setRequestProperty("x-access-token", accessToken.toString());
                 conn.setDoInput(true);
                 conn.connect();

                 int responseCode = conn.getResponseCode();
                 Log.d("DetailsBackgroundTask", "Response code Join: " + responseCode + "");
                 result_background = "RESULT Join: " + responseCode;

                 conn.disconnect();
                 Log.v("DetailsBackgroundTask", "TASK FINISHED = " + type);
             } catch (UnsupportedEncodingException e) {
                 e.printStackTrace();
             } catch (ProtocolException e) {
                 e.printStackTrace();
             } catch (MalformedURLException e) {
                 e.printStackTrace();
             } catch (IOException e) {
                 e.printStackTrace();
             }
        }
        else if(function.equals(LEAVE_EVENT)){
            try {
                String event_id = params[1];
                String accessToken = "";

                List<HttpCookie> cookieList = msCookieManager.getCookieStore().getCookies();

                for (int i = 0; i < cookieList.size(); i++) {
                    if (cookieList.get(i).getName().equals("x-access-token")){
                        accessToken = cookieList.get(i).getValue();
                        break;
                    }
                }

                Log.d("DetailsBackgroundTask" , accessToken);

                URL url = new URL("http://54.69.152.154:3000/leaveEvent?id=" + event_id);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                conn.setReadTimeout(30000);
                conn.setConnectTimeout(30000);
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("x-access-token", accessToken.toString());
                conn.setDoInput(true);
                conn.connect();

                int responseCode = conn.getResponseCode();
                Log.d("DetailsBackgroundTask", "Response code Leave: " + responseCode + "");
                result_background = "RESULT Leave: " + responseCode;

                conn.disconnect();
                Log.v("DetailsBackgroundTask", "TASK FINISHED = " + type);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(function.equals(DELETE_EVENT)){
            try {
                String event_id = params[1];
                String accessToken = "";

                List<HttpCookie> cookieList = msCookieManager.getCookieStore().getCookies();

                for (int i = 0; i < cookieList.size(); i++) {
                    if (cookieList.get(i).getName().equals("x-access-token")){
                        accessToken = cookieList.get(i).getValue();
                        break;
                    }
                }

                Log.d("DetailsBackgroundTask" , accessToken);

                URL url = new URL("http://54.69.152.154:3000/deleteEvent?id=" + event_id);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                conn.setReadTimeout(30000);
                conn.setConnectTimeout(30000);
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("x-access-token", accessToken.toString());
                conn.setDoInput(true);
                conn.connect();

                int responseCode = conn.getResponseCode();
                Log.d("DetailsBackgroundTask", "Response code delete: " + responseCode + "");
                result_background = "RESULT delete: " + responseCode;

                conn.disconnect();
                Log.v("DetailsBackgroundTask", "TASK FINISHED = " + type);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(function.equals(GET_ORGANIZER_INFO)){
            try {
                String organizer_id = params[1];

                URL url = new URL("http://54.69.152.154:3000/user/" + organizer_id);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                conn.setReadTimeout(30000);
                conn.setConnectTimeout(30000);
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Content-Type", "application/json");
                //conn.setRequestProperty("x-access-token", accessToken.toString());
                conn.setDoInput(true);
                conn.connect();

                int responseCode = conn.getResponseCode();
                Log.d("DetailsBackgroundTask", "Response code organizerInfo: " + responseCode + "");
                String line;
                String result = "";
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line = br.readLine()) != null) {
                    result += line;
                }

                JSONObject organizerObject = null;
                try {
                    organizerObject = new JSONObject(result);
                    result_background = organizerObject.getString("username");
                } catch (JSONException e) {
                    Log.e("DetailsBackgroundTask", "GET ORGANIZER INFO JSON ERROR");
                    e.printStackTrace();
                }

                conn.disconnect();
                Log.v("DetailsBackgroundTask", "TASK FINISHED = " + type);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(function.equals(GET_EVENT_DETAILS)){
            try {
                String event_id = params[1];

                URL url = new URL("http://54.69.152.154:3000/eventDetail?id=" + event_id);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                conn.setReadTimeout(30000);
                conn.setConnectTimeout(30000);
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Content-Type", "application/json");
                //conn.setRequestProperty("x-access-token", accessToken.toString());
                conn.setDoInput(true);
                conn.connect();

                int responseCode = conn.getResponseCode();
                Log.d("DetailsBackgroundTask", "Response code eventDetails: " + responseCode + "");
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line = br.readLine()) != null) {
                    result_background += line;
                }

                conn.disconnect();
                Log.v("DetailsBackgroundTask", "TASK FINISHED = " + type);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if (function.equals(EDIT_EVENT)){
            try {

                String title = params[1];
                String dateToConvertMiliseconds = params[2];
                String placeCity = params[3];
                String longitudeToConvertFloat = params[4];
                String latitudeToConvertFloat = params[5];
                String myAddress = params[6];
                String placeName = params[7];
                String participantCount = params[8];
                String category = params[9];
                String description = params[10];
                String feeToConvert = params[11];
                String organizerId = params[12];
                String event_id = params[13];

                String accessToken = "";

                List<HttpCookie> cookieList = msCookieManager.getCookieStore().getCookies();

                for (int i = 0; i < cookieList.size(); i++) {
                    if (cookieList.get(i).getName().equals("x-access-token")){
                        accessToken = cookieList.get(i).getValue();
                        break;
                    }
                }

                Log.d("Access Token in Event: " ,accessToken);

                URL url = new URL("http://54.69.152.154:3000/editEvent?id=" + event_id);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                //conn.connect();
                conn.setReadTimeout(30000);
                conn.setConnectTimeout(30000);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("x-access-token", accessToken.toString());
                conn.setDoInput(true);
                conn.setDoOutput(true);

                conn.connect();

                //Conversions
                double latitude = Double.parseDouble(latitudeToConvertFloat);
                double longitude = Double.parseDouble(longitudeToConvertFloat);
                int parCount = Integer.parseInt(participantCount);
                double fee = Double.parseDouble(feeToConvert);

                SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
                Date oldDate = sdf.parse(dateToConvertMiliseconds);
                long dateInMiliseconds = oldDate.getTime();

                JSONObject requestBody = new JSONObject();
                requestBody.put("name", title);
                requestBody.put("date", dateInMiliseconds);
                requestBody.put("city", placeCity);
                requestBody.put("longitude", longitude);
                requestBody.put("latitude", latitude);
                requestBody.put("address", myAddress);
                requestBody.put("placeName", placeName);
                requestBody.put("category", category);
                requestBody.put("description", description);
                requestBody.put("fee", fee);
                requestBody.put("participantCount", parCount);
                requestBody.put("organizer", organizerId);

                OutputStream os = conn.getOutputStream();

                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                String str = requestBody.toString();
                Log.d("DetailsBackgroundTask", str);
                writer.write(str);
                writer.flush();
                writer.close();
                os.close();

                int responseCode = conn.getResponseCode();
                Log.d("DetailsBackgroundTask", responseCode + "");
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line = br.readLine()) != null) {
                    result_background += line;
                }
                Log.d("DetailsBackgroundTask", result_background);

                conn.disconnect();
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
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        else {
            result_background = "DefaultCase";
        }
        return result_background;
    }
    @Override
    protected void onProgressUpdate(Integer... values) {
        this.p_dialog.show();
    }

    @Override
    protected void onPostExecute(String result) {

        if (result == null || result.equals("0") || result.equals("") || result.trim().equals("DefaultCase")){
            Log.v("DetailsBackgroundTask", " Error in onPostExecute " + type + " = " + result);
            return;
        }
        else
            Log.v("DetailsBackgroundTask" , "Result successful: " + type  + " = " + result);

        if (p_dialog != null && p_dialog.isShowing())
            p_dialog.dismiss();

        Log.v("DetailsBackgroundTask", "TASK FINISHED POsT = " + type);

    }
}
