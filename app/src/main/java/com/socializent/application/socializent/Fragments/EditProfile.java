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

import com.socializent.application.socializent.Controller.EditUserController;
import com.socializent.application.socializent.Controller.PersonBackgroundTask;
import com.socializent.application.socializent.Modal.Person;
import com.socializent.application.socializent.R;
import com.socializent.application.socializent.Template;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Irem on 13.4.2017.
 */

public class EditProfile extends Fragment {

    static Person p;
    View profileView;
    EditUserController task;
    JSONObject jsonObject;
    CheckBox celebration;
    CheckBox concert;
    CheckBox conferance;
    CheckBox lecture;
    CheckBox movieScreen;
    CheckBox party;
    CheckBox study;
    CheckBox travel,meeting,birthday,sport;

    Button update;
    EditText name;
    EditText surname;
    EditText username;
    EditText email;
    EditText bio;
    EditText birthdate;

    ArrayList<String> interest;

    public EditProfile() {
        // Required empty public constructor
    }

    public static EditProfile newInstance(Person person) {
        p = person;
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
        email = (EditText)profileView.findViewById(R.id.editProfileEditEmail);
        bio = (EditText)profileView.findViewById(R.id.editProfileEditBio);
        birthdate = (EditText)profileView.findViewById(R.id.editProfileEditBirthdate);
        if(p == null)
        {
            Log.d("null", "geliyor");
        }
        Float bdf = p.getBirthDate();
        long number =bdf.longValue();
        Date date=new Date(number);
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        String dateStr = df.format(date);

        celebration = (CheckBox) profileView.findViewById(R.id.celebration);
        concert = (CheckBox) profileView.findViewById(R.id.concert);
        conferance = (CheckBox) profileView.findViewById(R.id.conferance);
        lecture = (CheckBox) profileView.findViewById(R.id.lecture);
        movieScreen = (CheckBox) profileView.findViewById(R.id.movie);
        party = (CheckBox) profileView.findViewById(R.id.party);
        study = (CheckBox) profileView.findViewById(R.id.study);
        travel = (CheckBox) profileView.findViewById(R.id.travel);
        meeting = (CheckBox) profileView.findViewById(R.id.meeting);
        birthday = (CheckBox) profileView.findViewById(R.id.birthday);
        sport = (CheckBox) profileView.findViewById(R.id.sport);

        name.setText(p.getFirstName());
        surname.setText(p.getLastName());
        username.setText(p.getUsername());
        email.setText(p.getEmail());
        bio.setText(p.getBio());
        birthdate.setText(dateStr);
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
                    interest.add("conference");
                }
                else{
                    interest.remove("conference");
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
                    interest.add("movie");
                }
                else{
                    interest.remove("movie");
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
                    interest.remove("travel");
                }
            }
        });
        meeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(travel.isChecked()){
                    interest.add("meeting");
                }
                else{
                    interest.remove("meeting");
                }
            }
        });
        birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(travel.isChecked()){
                    interest.add("birthday");
                }
                else{
                    interest.remove("birthday");
                }
            }
        });
        sport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(travel.isChecked()){
                    interest.add("sports");
                }
                else{
                    interest.add("sports");
                }
            }
        });

        update.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
               updateProfile(v);
                PersonBackgroundTask loginTask = new PersonBackgroundTask(getContext());
                loginTask.execute("3");
            }
        });
        return profileView;

    }

    public void updateProfile(View view)throws RuntimeException{

        task = new EditUserController(getContext());
        long newBDate = 0;
        String myDate = birthdate.getText().toString();
        SimpleDateFormat newsdf = new SimpleDateFormat("dd.MM.yyyy");
        try {
            Date newDate = newsdf.parse(myDate);
            newBDate = newDate.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        jsonObject= new JSONObject();

        try {
            jsonObject.put("firstName", name.getText().toString());
            jsonObject.put("lastName", surname.getText().toString());
            jsonObject.put("username", username.getText().toString());
            jsonObject.put("birthDate",newBDate);
            jsonObject.put("email", email.getText().toString());

            //****** Array insert etmek için
            JSONArray jsArray = new JSONArray();
            for(int i =0; i<interest.size(); i++)
                jsArray.put(interest.get(i));
            jsonObject.put("interests", jsArray);

            jsonObject.put("shortBio", bio.getText().toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        task.execute(jsonObject);
        Intent intent = new Intent(getActivity(), Template.class);
        startActivity(intent);

    }

}
