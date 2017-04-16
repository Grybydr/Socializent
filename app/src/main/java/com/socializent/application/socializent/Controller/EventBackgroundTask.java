package com.socializent.application.socializent.Controller;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.socializent.application.socializent.Controller.PersonBackgroundTask.msCookieManager;

/**
 * Created by Toshıba on 04/15/2017.
 */

public class EventBackgroundTask extends AsyncTask<String, Object, String> {

    //private Context context;
    final static String EVENT_CREATE_OPTION = "1";

    public EventBackgroundTask(){

    }

    @Override
    protected String doInBackground(String... params) {
        String result = "";
        String type = params[0];



        if (type.equals(EVENT_CREATE_OPTION)){
            try {

                String title = params[1];
                String dateToConvertMiliseconds = params[2];
                String placeName = params[3];
                String longitudeToConvertFloat = params[4];
                String latitudeToConvertFloat = params[5];
                String participantCount = params[6];
                String tags = params[7];
                String description = params[8];
                String fee = params[9];

                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

                String accessToken = msCookieManager.getCookieStore().getCookies().get(msCookieManager.getCookieStore().getCookies().size()-1).getValue();
                //Log.d("Access Token in Event: " ,accessToken);

                //accessToken = accessToken.substring(1,accessToken.length()-1);
                Log.d("Access Token in Event: " ,accessToken);


                Date oldDate = sdf.parse(dateToConvertMiliseconds);
                long dateInMiliseconds = oldDate.getTime();

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


                Log.d("title: ", title);
                Log.d("dateMiliseconds: ", dateInMiliseconds +"");
                Log.d("placeName(City):  ", placeName);
                //Log.d("latitudeToConvert", latitudeToConvertFloat);

                Log.d("tags: ", tags);
                Log.d("description: ", description);
                Log.d("fee: ", fee);

                float latitude = Float.parseFloat(latitudeToConvertFloat);
                float longtitude = Float.parseFloat(longitudeToConvertFloat);

                Log.d("longtitude: ", longtitude+"");
                Log.d("latitude: ", latitude+"");

                JSONObject requestBody = new JSONObject();
                requestBody.put("title", title);
                requestBody.put("date", dateInMiliseconds);
                requestBody.put("place.city", placeName);
                requestBody.put("place.longtitude", longtitude);
                requestBody.put("place.latitude", latitude);
                //requestBody.put("participant", participantCount);
                requestBody.put("tags", tags);
                requestBody.put("description", description);
                requestBody.put("fee", fee);

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

        return result;
    }

    @Override
    protected void onPostExecute(String result) {

        if (result == null){
            Log.d("Result is null: ", result);
            return;
        }
        else if(result.equals("0")){
            Log.d("Error creating: ", result);
            return;
        }

        Log.d("Successfully added: " ,result);
    }
}
