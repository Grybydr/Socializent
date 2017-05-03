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

    public ProgressDialog p_dialog;
    public Context context;

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
        String result = "";

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
                     result += line;
                 }
                 Log.v("DetailsBackgroundTask", result);
                 HttpCookie accessTokenCookie = new HttpCookie("targetEvent", result);
                 msCookieManager.getCookieStore().add(null, accessTokenCookie);

                 conn.disconnect();
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
                 result = "RESULT Join: " + responseCode;

                 conn.disconnect();
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
        else {
            result = "DefaultCase";
        }
        return result;
    }
    @Override
    protected void onProgressUpdate(Integer... values) {
        this.p_dialog.show();
    }

    @Override
    protected void onPostExecute(String result) {

        if (result == null || result.equals("0") || result.equals("") || result.trim().equals("DefaultCase")){
            Log.d("DetailsBackgroundTask", " Error in onPostExecute " + result);
            return;
        }
        else
            Log.d("DetailsBackgroundTask" , "Result successful: " + result);

        if (p_dialog != null && p_dialog.isShowing())
            p_dialog.dismiss();

    }
}
