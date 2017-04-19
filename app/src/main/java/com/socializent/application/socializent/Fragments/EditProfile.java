package com.socializent.application.socializent.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.socializent.application.socializent.R;
import com.socializent.application.socializent.Template;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Irem on 13.4.2017.
 */

public class EditProfile extends Fragment {
    View profileView;
    CheckBox celebration;
    CheckBox concert;
    CheckBox conferance;
    CheckBox lecture;
    CheckBox movieScreen;
    CheckBox party;
    CheckBox study;
    CheckBox travel;

    Button update;
    EditText name;
    EditText surname;
    EditText username;
    EditText password;
    EditText email;
    EditText city;
    EditText bio;
    EditText birthdate;

    ArrayList<String> interest;

    public EditProfile() {
        // Required empty public constructor
    }

    public static EditProfile newInstance() {
        return new EditProfile();
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.edit_profile);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        profileView = inflater.inflate(R.layout.edit_profile, container, false);
        update = (Button) profileView.findViewById(R.id.UpdateButton);

        name = (EditText) profileView.findViewById(R.id.editProfileEditName);
        surname = (EditText) profileView.findViewById(R.id.editProfileEditSurname);
        username = (EditText) profileView.findViewById(R.id.editProfileEditUsername);
        password = (EditText)profileView.findViewById(R.id.editProfileEditPassword);
        email = (EditText)profileView.findViewById(R.id.editProfileEditEmail);
        city = (EditText) profileView.findViewById(R.id.editProfileEditCity);
        bio = (EditText)profileView.findViewById(R.id.editProfileEditBio);
        birthdate = (EditText)profileView.findViewById(R.id.editProfileEditBirthdate);


        celebration = (CheckBox) profileView.findViewById(R.id.celebration);
        concert = (CheckBox) profileView.findViewById(R.id.concert);
        conferance = (CheckBox) profileView.findViewById(R.id.conferance);
        lecture = (CheckBox) profileView.findViewById(R.id.lecture);
        movieScreen = (CheckBox) profileView.findViewById(R.id.movieScreen);
        party = (CheckBox) profileView.findViewById(R.id.party);
        study = (CheckBox) profileView.findViewById(R.id.study);
        travel = (CheckBox) profileView.findViewById(R.id.travel);

        interest = new ArrayList<String>();
        celebration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(celebration.isChecked()){
                    interest.add("celebration");
                }
                else{
                    interest.remove("celebration");
                }
            }
        });
        concert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(concert.isChecked()){
                    interest.add("concert");
                }
                else{
                    interest.remove("concert");
                }
            }
        });
        conferance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(conferance.isChecked()){
                    interest.add("conferance");
                }
                else{
                    interest.remove("conferance");
                }
            }
        });
        lecture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(lecture.isChecked()){
                    interest.add("lecture");
                }
                else{
                    interest.remove("lecture");
                }
            }
        });
        movieScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(movieScreen.isChecked()){
                    interest.add("movieScreen");
                }
                else{
                    interest.remove("movieScreen");
                }
            }
        });
        party.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(party.isChecked()){
                    interest.add("party");
                }
                else{
                    interest.remove("party");
                }
            }
        });
        study.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(study.isChecked()){
                    interest.add("study");
                }
                else{
                    interest.remove("study");
                }
            }
        });
        travel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(travel.isChecked()){
                    interest.add("travel");
                }
                else{
                    interest.add("travel");
                }
            }
        });

        JSONObject jsonObject= new JSONObject();

        try {
            jsonObject.put("firstName", name.getText().toString());
            jsonObject.put("lastName", surname.getText().toString());
            jsonObject.put("username", username.getText().toString());
            jsonObject.put("birthDate", birthdate.getText().toString());
            jsonObject.put("password", password.getText().toString());
            jsonObject.put("email", email.getText().toString());
            //jsonObject.put("bio", bio.getText().toString());
            jsonObject.put("interests", interest.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        update.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
               updateProfile(v);
            }
        });
        return profileView;

    }

    public void updateProfile(View view)throws RuntimeException{

        Intent intent = new Intent(getActivity(), Template.class);
        startActivity(intent);

    }

}
