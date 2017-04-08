package com.socializent.application.socializent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.socializent.application.socializent.Controller.UserController;
import com.socializent.application.socializent.Modal.Person;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Zulal Bingol on 5.03.2017.
 */

public class main extends Activity {
    //@Override
    UserController userController;
    Person activeUser;
    EditText userNameText;
    EditText passwordText;
    int loginToken;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);

        userController = new UserController();

        ImageView socializentLogo = (ImageView)findViewById(R.id.logoView);
        Button loginButton = (Button)findViewById(R.id.loginButton);
        userNameText = (EditText)findViewById(R.id.userNameText);
        passwordText = (EditText)findViewById(R.id.passwordText);
        Button fbButton = (Button)findViewById(R.id.fbButton);
        Button forgetPasswordButton = (Button)findViewById(R.id.forgetPasswordButton);
        Button signUpButton = (Button)findViewById(R.id.signUpButton);
        TextView forgotPasswordText = (TextView) findViewById(R.id.forgotPasswordText);
        TextView fbLoginText = (TextView) findViewById(R.id.fbLoginText);
        TextView singupText = (TextView) findViewById(R.id.singupText);




    }

    public void goToStartScreen(View view) throws IOException {




        loginToken = userController.login(userNameText.getText().toString(),passwordText.getText().toString());
        if (loginToken == 0)
            Toast.makeText(this,"Wrong Credentials", Toast.LENGTH_LONG);
        else{
            Intent intentNavigationBar = new Intent(this, Template.class);
            startActivity(intentNavigationBar);
        }

        //activeUser = userController.getUserFromServer(userNameText.getText().toString(), passwordText.getText().toString());
        //intentNavigationBar.putExtra("Username", userNameText.getText().toString());
        //intentNavigationBar.putExtra("Password", passwordText.getText().toString());



    }
}
