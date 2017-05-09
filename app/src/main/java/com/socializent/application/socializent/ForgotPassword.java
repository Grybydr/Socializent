package com.socializent.application.socializent;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Toshıba on 8.05.2017.
 */

public class ForgotPassword extends Activity {

    private EditText email;
    private EditText birthdate;
    private Button forgotPassword;
    private Date fakeDate;
    long newBDate = 0;
    SimpleDateFormat newsdf = new SimpleDateFormat("dd.MM.yyyy");
    private Context mContext;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password);
        email = (EditText) findViewById(R.id.emailText);
        birthdate = (EditText) findViewById(R.id.birthdate);
        forgotPassword = (Button) findViewById(R.id.sendButton);
        mContext = this;

        forgotPassword.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Date newDate = newsdf.parse(birthdate.getText().toString());
                    newBDate = newDate.getTime();
                } catch (ParseException e) {
                    e.printStackTrace();
                    Toast.makeText(mContext,"Please Enter the birtdate in correct form: DD.MM.YYYY",Toast.LENGTH_SHORT).show();
                }

                // PersonBackgroundTask learnPasswordTask = new PersonBackgroundTask();
                // learnPasswordTask.execute("6", email.getText().toString() , newBDate);

            }
        });
    }
}
