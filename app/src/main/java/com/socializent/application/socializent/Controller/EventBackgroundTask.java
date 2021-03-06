package com.socializent.application.socializent.Controller;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

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
import java.util.Date;
import java.util.List;

import static com.socializent.application.socializent.Controller.PersonBackgroundTask.msCookieManager;

/**
 * Created by Toshıba on 04/15/2017.
 */

public class EventBackgroundTask extends AsyncTask<String, Void , String> {

    private Context context = null;
    final static String EVENT_CREATE_OPTION = "1";
    final static String GET_ALL_EVENTS_OPTION = "2";
    final static String GET_BY_POSITION_EVENT_OPTION = "4";
    final static String RATE_EVENT_OPTION = "5";
    final static String SENT_COMMENT_TO_EVENT_OPTION = "6";
    final static String LIST_COMMENTS_OPTION = "7";

    public EventBackgroundTask(){
    }

    public EventBackgroundTask(Context context){
        this.context = context;
    }

    @Override
    protected String doInBackground(String... params) {
        String result = "";
        String type = params[0];

        if (type.equals(EVENT_CREATE_OPTION)){
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

                String accessToken = "";

                List<HttpCookie> cookieList = msCookieManager.getCookieStore().getCookies();

                for (int i = 0; i < cookieList.size(); i++) {
                    if (cookieList.get(i).getName().equals("x-access-token")){
                        accessToken = cookieList.get(i).getValue();
                        break;
                    }
                }
                //msCookieManager.getCookieStore().getCookies().get(0).getValue();
                //Log.d("Access Token in Event: " ,accessToken);

                //accessToken = accessToken.substring(1,accessToken.length()-1);
                Log.d("Access Token in Event: " ,accessToken);

                URL url = new URL("http://54.69.152.154:3000/createEvent");
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
                //HashMap<String, String> postDataParams = new HashMap<String, String>();
                //postDataParams.put("username", username);
                // postDataParams.put("password", password);

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
                requestBody.put("place.city", placeCity);
                requestBody.put("place.longitude", longitude);
                requestBody.put("place.latitude", latitude);
                requestBody.put("place.address", myAddress);
                requestBody.put("place.name", placeName);
                requestBody.put("category", category);
                requestBody.put("description", description);
                requestBody.put("fee", fee);
                requestBody.put("participantCount", parCount);
                requestBody.put("organizer", organizerId);

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
                //if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line = br.readLine()) != null) {
                    result += line;
                }
                Log.d("Response: ", result);

                //}
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
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        else if (type.equals(GET_ALL_EVENTS_OPTION)){
            try{

                URL url = new URL("http://54.69.152.154:3000/events");
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
                Log.d("AllEvents: ", result);
                HttpCookie accessTokenCookie = new HttpCookie("allEvents",result);

                msCookieManager.getCookieStore().add(null, accessTokenCookie);

                conn.disconnect();
                return result;
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if (type.equals(GET_BY_POSITION_EVENT_OPTION)){
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
                Log.d("Response Code", responseCode + "");

                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line = br.readLine()) != null) {
                    result += line;
                }
                Log.v("targetEvent", result);
                HttpCookie accessTokenCookie = new HttpCookie("targetEvent",result);

                msCookieManager.getCookieStore().add(null, accessTokenCookie);

                conn.disconnect();
                return result;
            } catch (ProtocolException e) {
                e.printStackTrace();
                Log.d("targetEvent", "protocol error");
            } catch (MalformedURLException e) {
                Log.d("targetEvent", "malformed URL");
                e.printStackTrace();
            } catch (IOException e) {
                Log.d("targetEvent", "IO exception");
                e.printStackTrace();
            }
        }
        else if(type.equals(RATE_EVENT_OPTION))
        {
            try {
                String eventId = params[2];
                String rate = params[1];

                URL url = new URL("http://54.69.152.154:3000/rateEvent?rate=" + rate +"&id="+ eventId);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                String accessToken = "";

                List<HttpCookie> cookieList = msCookieManager.getCookieStore().getCookies();

                for (int i = 0; i < cookieList.size(); i++) {
                    if (cookieList.get(i).getName().equals("x-access-token")) {
                        accessToken = cookieList.get(i).getValue();
                        break;
                    }
                }

                conn.setReadTimeout(30000);
                conn.setConnectTimeout(30000);
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("x-access-token", accessToken);
                conn.setDoInput(true);
                conn.connect();

                int responseCode = conn.getResponseCode();
                Log.d("Response Code", responseCode + "");

                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line = br.readLine()) != null) {
                    result += line;
                }
                Log.v("Result of Rate Event: ", result);

                conn.disconnect();
                return result;
            } catch (ProtocolException e) {
                e.printStackTrace();
                Log.d("Result of Rate Event: ", "protocol error");
            } catch (MalformedURLException e) {
                Log.d("Result of Rate Event: ", "malformed URL");
                e.printStackTrace();
            } catch (IOException e) {
                Log.d("Result of Rate Event: ", "IO exception");
                e.printStackTrace();
            }
        }
        else if(type.equals(SENT_COMMENT_TO_EVENT_OPTION))
        {
            try {
                String eventId = params[1];
                String content = params[2];

                Log.d("content :", content);

                URL url = new URL("http://54.69.152.154:3000/addComment?id="+ eventId);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                String accessToken = "";

                List<HttpCookie> cookieList = msCookieManager.getCookieStore().getCookies();

                for (int i = 0; i < cookieList.size(); i++) {
                    if (cookieList.get(i).getName().equals("x-access-token")) {
                        accessToken = cookieList.get(i).getValue();
                        break;
                    }
                }

                conn.setReadTimeout(30000);
                conn.setConnectTimeout(30000);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("x-access-token", accessToken);
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.connect();


                JSONObject requestBody = new JSONObject();
                requestBody.put("content", content);


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
                Log.d("Response Code", responseCode + "");

                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line = br.readLine()) != null) {
                    result += line;
                }
                Log.v("Result of Rate Event: ", result);

                conn.disconnect();
                return result;
            } catch (ProtocolException e) {
                e.printStackTrace();
                Log.d("Result of Rate Event: ", "protocol error");
            } catch (MalformedURLException e) {
                Log.d("Result of Rate Event: ", "malformed URL");
                e.printStackTrace();
            } catch (IOException e) {
                Log.d("Result of Rate Event: ", "IO exception");
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else if(type.equals(LIST_COMMENTS_OPTION)){
            try {
                String eventId = params[1];

                URL url = new URL("http://54.69.152.154:3000/listComments?id="+ eventId);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                String accessToken = "";

                List<HttpCookie> cookieList = msCookieManager.getCookieStore().getCookies();

                for (int i = 0; i < cookieList.size(); i++) {
                    if (cookieList.get(i).getName().equals("x-access-token")) {
                        accessToken = cookieList.get(i).getValue();
                        break;
                    }
                }

                conn.setReadTimeout(30000);
                conn.setConnectTimeout(30000);
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("x-access-token", accessToken);
                conn.setDoInput(true);
                //conn.setDoOutput(true);
                conn.connect();

                int responseCode = conn.getResponseCode();
                Log.d("Response Code", responseCode + "");

                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line = br.readLine()) != null) {
                    result += line;
                }
                Log.v("Result List Comments: ", result);

                conn.disconnect();

                msCookieManager.getCookieStore().add(null,new HttpCookie("eventComments",result));

                return result;
            } catch (ProtocolException e) {
                e.printStackTrace();
                Log.d("Error List Comments: ", "protocol error");
            } catch (MalformedURLException e) {
                Log.d("Error List Comments: ", "malformed URL");
                e.printStackTrace();
            } catch (IOException e) {
                Log.d("Error List Comments: ", "IO exception");
                e.printStackTrace();
            }
        }
        return result;
    }

    @Override
    protected void onPostExecute(String result) {

        if (result == null){
            Log.d("EventBackgroundTask", " Error in onPostExecute " + result);
            return;
        }
        else if(result.equals("0") || result.equals("")){
            Log.d("EventBackgroundTask", " Error in onPostExecute " + result);
            return;
        }
        if(result.equals("1") && context == null)
        {
            Log.d("Event Rated: " , result);
            return;
        }
        else if(context != null){
            Log.d("Comment Added: " , result);
            return;
        }

        Log.d("EventBackgroundTask" , "Succesfully added " + result);
        Log.d("EventBackgroundTask" , "Succesfully listed: " + result);
    }

}
