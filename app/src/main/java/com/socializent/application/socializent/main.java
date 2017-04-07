package com.socializent.application.socializent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.socializent.application.socializent.Controller.UserController;
import com.socializent.application.socializent.Modal.Person;

/**
 * Created by Zulal Bingol on 5.03.2017.
 */

public class main extends Activity {
    //@Override
    UserController userController;
    Person activeUser;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);

        userController = new UserController();

        ImageView socializentLogo = (ImageView)findViewById(R.id.logoView);
        Button loginButton = (Button)findViewById(R.id.loginButton);
        EditText userNameText = (EditText)findViewById(R.id.userNameText);
        EditText passwordText = (EditText)findViewById(R.id.passwordText);
        Button fbButton = (Button)findViewById(R.id.fbButton);
        Button forgetPasswordButton = (Button)findViewById(R.id.forgetPasswordButton);
        Button signUpButton = (Button)findViewById(R.id.signUpButton);
        TextView forgotPasswordText = (TextView) findViewById(R.id.forgotPasswordText);
        TextView fbLoginText = (TextView) findViewById(R.id.fbLoginText);
        TextView singupText = (TextView) findViewById(R.id.singupText);


        activeUser = userController.getUserFromServer(userNameText.toString(), passwordText.toString());

    }

    public void goToStartScreen(View view) {

        Intent intentNavigationBar = new Intent(this, Template.class);
        startActivity(intentNavigationBar);



    }
}
