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
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

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

        return "";
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
