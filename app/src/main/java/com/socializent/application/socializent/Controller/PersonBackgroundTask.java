package com.socializent.application.socializent.Controller;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.socializent.application.socializent.Template;
import com.socializent.application.socializent.main;

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
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Toshıba on 04/09/2017.
 */

public class PersonBackgroundTask extends AsyncTask<String, Object, String> {

    private Context context;

    public PersonBackgroundTask(Context context){
        this.context=context;
    }

    @Override
    protected String doInBackground(String... params) {
        String result = "";
        try {
            String username = params[0];
            String password = params[1];

            URL url = new URL("http://54.69.152.154:3000/signin");
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
            JSONObject requestBody = new JSONObject();
            requestBody.put("username", username);
            requestBody.put("password", password);
            OutputStream os = conn.getOutputStream();

            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            String str = requestBody.toString();
            Log.d("Result1: ", str);
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
                Log.d("Result1: ", result);
            //}
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

        return result;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        Toast.makeText(context,result,Toast.LENGTH_LONG).show();
        Intent intentNavigationBar = new Intent(context, Template.class);
        context.startActivity(intentNavigationBar);
    }
}