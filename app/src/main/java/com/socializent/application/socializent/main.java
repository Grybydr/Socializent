package com.socializent.application.socializent;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

/**
 * Created by Zulal Bingol on 5.03.2017.
 */

public class main extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);

        ImageView socializentLogo = (ImageView)findViewById(R.id.logoView);
        Button loginButton = (Button)findViewById(R.id.loginButton);
        EditText userNameText = (EditText)findViewById(R.id.userNameText);
        EditText passwordText = (EditText)findViewById(R.id.passwordText);

    }
}
