package com.socializent.application.socializent;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.socializent.application.socializent.Controller.PersonBackgroundTask;



/**
 * Created by Irem on 9.4.2017.
 */

public class SignUp extends Activity {

    EditText name;
    EditText surname;
    EditText username;
    EditText password;
    EditText email;
    Button showSignUpButton;
    Activity signinPage;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        name = (EditText) findViewById(R.id.signUpEditName);
        surname = (EditText) findViewById(R.id.signUpEditSurname);
        username = (EditText) findViewById(R.id.signUpEditUsername);
        password = (EditText) findViewById(R.id.signUpPass);
        email = (EditText) findViewById(R.id.signUpemail);

    }
    public void signUpToServer(View view){
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(SignUp.this);
        View mView = getLayoutInflater().inflate(R.layout.signupdialog, null);
        Button dialogOK = (Button) mView.findViewById(R.id.signUpOK);
        Button dialogCancel = (Button) mView.findViewById(R.id.signUpCancel);

        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();

        dialogOK.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //buralara diğer variablar da eklenebilir
                if(!name.getText().toString().isEmpty() && !surname.getText().toString().isEmpty()){

                    PersonBackgroundTask signUpTask = new PersonBackgroundTask(SignUp.this);
                    signUpTask.execute("1",name.getText().toString(),surname.getText().toString(),username.getText().toString(),password.getText().toString(),email.getText().toString());
                    Toast.makeText(SignUp.this, "You are signing up. Please sign in again!", Toast.LENGTH_LONG).show();
                    //TODO: eklendikten sonra ana sayfaya dönmesi lazım


                }
                else{
                    Toast.makeText(SignUp.this, "Please fill empty areas!", Toast.LENGTH_SHORT).show();
                }

            }
        });
        dialogCancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}
