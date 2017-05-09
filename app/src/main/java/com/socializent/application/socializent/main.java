package com.socializent.application.socializent;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.facebook.AccessToken;
import com.facebook.FacebookCallback;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.socializent.application.socializent.Controller.PersonBackgroundTask;
import com.socializent.application.socializent.Modal.Person;

import java.io.IOException;
import java.net.HttpCookie;
import java.util.List;

import static com.socializent.application.socializent.Controller.PersonBackgroundTask.msCookieManager;

/**
 * Created by Zulal Bingol on 5.03.2017.
 */

public class main extends Activity {
    //@Override
    Person activeUser;
    EditText userNameText;
    EditText passwordText;
    String loginToken = null;
    TextView singupText;
    final static String SIGN_UP_OPTION = "1";
    final static String SIGN_IN_OPTION = "2";
    Context mContext;
    private SharedPreferences mPrefs;
    private SharedPreferences.Editor editor;
    private Button forgetPasswordButton;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);
        mContext = this;
        if (AccessToken.getCurrentAccessToken() != null)
            LoginManager.getInstance().logOut();
        //Toast.makeText(this,new Date()+"",Toast.LENGTH_LONG).show();
        ImageView socializentLogo = (ImageView)findViewById(R.id.logoView);
        Button loginButton = (Button)findViewById(R.id.loginButton);
        userNameText = (EditText)findViewById(R.id.userNameText);
        passwordText = (EditText)findViewById(R.id.passwordText);
        Button fbButton = (Button)findViewById(R.id.fbButton);
        forgetPasswordButton = (Button)findViewById(R.id.forgetPasswordButton);

        TextView forgotPasswordText = (TextView) findViewById(R.id.forgotPasswordText);
        TextView fbLoginText = (TextView) findViewById(R.id.fbLoginText);
        singupText = (TextView) findViewById(R.id.singupText);

        forgotPasswordText.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent forgotPassword = new Intent(mContext, ForgotPassword.class);
                startActivity(forgotPassword);
            }
        });

        singupText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //DO you work here
                Intent intentNavigationBar = new Intent(mContext, SignUp.class);
                startActivity(intentNavigationBar);

            }
        });

        //SharedPreferences mPrefs = mContext.getS

        List<HttpCookie> cookieList = msCookieManager.getCookieStore().getCookies();

        for (int i = 0; i < cookieList.size(); i++) {
            if (cookieList.get(i).getName().equals("x-access-token")){
                loginToken = cookieList.get(i).getValue();
                break;
            }
        }

        if(loginToken == null)
            Log.d("login token: ", "null");
        else
            Log.d("login token: ", loginToken);
        /*if (loginToken == null ){
            mPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            editor = mPrefs.edit();
            editor.putString("x-access-token", loginToken);
            editor.commit();
        }
          */


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
       /* savedInstanceState.putBoolean("MyBoolean", true);
        savedInstanceState.putDouble("myDouble", 1.9);
        savedInstanceState.putInt("MyInt", 1);
        savedInstanceState.putString("MyString", "Welcome back to Android");*/
    }

    public void goToStartScreen(View view) throws IOException, InterruptedException {
        //adadad
        //loginToken = userController.login(userNameText.getText().toString(),passwordText.getText().toString(),this);
        //String logM = loginToken + "";
        //Log.d("Login: ", loginToken);
        PersonBackgroundTask loginTask = new PersonBackgroundTask(this);
        loginTask.execute(SIGN_IN_OPTION,userNameText.getText().toString(),passwordText.getText().toString());


        //activeUser = userController.getUserFromServer(userNameText.getText().toString(), passwordText.getText().toString());
        //intentNavigationBar.putExtra("Username", userNameText.getText().toString());
        //intentNavigationBar.putExtra("Password", passwordText.getText().toString());

    }


    public void moveToFacebookLoginPage(View view) {
        //Toast.makeText(mContext,"dadada",Toast.LENGTH_LONG).show();

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        FacebookFragment facebookFragment = new FacebookFragment();
        fragmentTransaction.replace(R.id.content_frame3, facebookFragment, "fragment");
        fragmentTransaction.commit();

    }
    @Override
    public void onBackPressed() {
    }
}
