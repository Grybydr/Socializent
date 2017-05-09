package com.socializent.application.socializent.Fragments;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;

import android.widget.TextView;
import android.widget.Toast;

import com.socializent.application.socializent.Controller.PersonBackgroundTask;
import com.socializent.application.socializent.Modal.Event;
import com.socializent.application.socializent.Modal.Person;
import com.socializent.application.socializent.R;

import net.cachapa.expandablelayout.ExpandableLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.net.HttpCookie;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.socializent.application.socializent.Controller.PersonBackgroundTask.msCookieManager;
import static com.socializent.application.socializent.Fragments.BottomBarSearch.searchedPerson;
import static com.socializent.application.socializent.Fragments.EditProfile.p;
import static com.socializent.application.socializent.Fragments.NavigationDrawerFourth.myFriend;


public class NavigationDrawerFirst extends Fragment {
    //CircleDrawable circle;
    View profileView;
    ImageView profilePic;
    ImageView imagePen;
    ImageView addFriend,removeFriend;
    TextView userNameText;
    TextView bioText;
    TextView emailText;
    TextView birthdayText;
    TextView interestText;
    JSONObject userObject = null;
    String user = "";
    PersonBackgroundTask conn;
    PersonBackgroundTask refresh;
    Person otherPerson;
    String activeUserId;
    List<HttpCookie> cookieList;
    public NavigationDrawerFirst() {
        // Required empty public constructor
    }

    public static NavigationDrawerFirst newInstance() {
        return new NavigationDrawerFirst();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.material_design_profile_screen_xml_ui_design);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        profileView = inflater.inflate(R.layout.material_design_profile_screen_xml_ui_design, container, false);
        imagePen = (ImageView) profileView.findViewById(R.id.edit_pen);
        profilePic = (ImageView) profileView.findViewById(R.id.user_profile_photo);

        addFriend = (ImageView) profileView.findViewById(R.id.add_friend);
        removeFriend = (ImageView) profileView.findViewById(R.id.remove_friend);
        userNameText = (TextView) profileView.findViewById(R.id.user_profile_name);
        bioText = (TextView) profileView.findViewById(R.id.user_profile_short_bio);
        emailText = (TextView) profileView.findViewById(R.id.user_profile_email);
        birthdayText = (TextView) profileView.findViewById(R.id.profile_birthday);
        interestText = (TextView) profileView.findViewById(R.id.profile_interests);
        cookieList = msCookieManager.getCookieStore().getCookies();

        for (int i = 0; i < cookieList.size(); i++) {
            if (cookieList.get(i).getName().equals("user")) {
                user = cookieList.get(i).getValue();
                break;
            }
        }
        try {
            userObject = new JSONObject(user);
            activeUserId = userObject.getString("_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(searchedPerson != null || myFriend != null) {
            searchedUserProfile();
            searchedPerson = null;
            myFriend = null;
        }
        else {
            myProfile();
        }
        return profileView;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }
    public void myProfile(){
        imagePen.setVisibility(View.VISIBLE);
        addFriend.setVisibility(View.GONE);
        removeFriend.setVisibility(View.GONE);

       /* profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
                getIntent.setType("image*//*");

                Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickIntent.setType("image*//*");

                Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});

                startActivityForResult(chooserIntent, 1);

            }

        });*/


        imagePen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editPage(v);
            }
        });


        try {

            String fullname = userObject.getString("fullName");
            userNameText.setText(fullname);
            String firstName = userObject.getString("firstName");
            String lastname = userObject.getString("lastName");
            String email = userObject.getString("email");
            emailText.setText(email);
            String shortBio = userObject.getString("shortBio");
            bioText.setText(shortBio);
            String username = userObject.getString("username");
            String password = userObject.getString("password");

            String birthday = userObject.getString("birthDate");
            long number = Long.parseLong(birthday);
            float bd = Float.parseFloat(birthday);
            Date date=new Date(number);
            DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
            String dateStr = df.format(date);
            birthdayText.setText(dateStr);

            String interestJSONArray = userObject.getString("interests");
            JSONArray interestA = new JSONArray(interestJSONArray);
            ArrayList<String> interestArray = new ArrayList<String>();
            for (int k = 0; k < interestA.length(); k++) {
                //JSONObject interest = interestA.getJSONObject(k);
                interestArray.add(interestA.getString(k));
            }
            interestText.setText(interestArray.toString());

            p = new Person(firstName, lastname, username, bd, password, email, shortBio, null, interestArray, null, null, null, 0);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

 /*   public  void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        String filePath = null;
        String file_extn = null;
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch(requestCode) {
            case 0:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                    profilePic.setImageURI(selectedImage);
                    filePath = getPath(selectedImage);
                    file_extn = filePath.substring(filePath.lastIndexOf(".") + 1);
                   // image_name_tv.setText(filePath);
                }

                break;
            case 1:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                    profilePic.setImageURI(selectedImage);
                }
                break;
        }
        if (file_extn.equals("img") || file_extn.equals("jpg") || file_extn.equals("jpeg") || file_extn.equals("gif") || file_extn.equals("png")) {
            //FINE
        } else {
            //NOT IN REQUIRED FORMAT
        }
    }*/
    public String getPath(Uri uri) {
        String[] projection = {MediaStore.MediaColumns.DATA};
        Cursor cursor = getActivity().managedQuery(uri, projection, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();
        //String imagePath = cursor.getString(column_index);

        return cursor.getString(column_index);
    }

    public void editPage(View view) throws RuntimeException {
        Fragment mFragment = EditProfile.newInstance(p);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.content_frame, mFragment);
        transaction.commit();

    }

    public void searchedUserProfile(){
        boolean alreadyFriend = false;
        boolean friendReqSent = false;
        conn = new PersonBackgroundTask(getContext());
        refresh = new PersonBackgroundTask(getContext());
        imagePen.setVisibility(View.GONE);
        if(searchedPerson == null && myFriend != null)
            otherPerson = myFriend;
        else if(searchedPerson != null && myFriend == null)
            otherPerson = searchedPerson;
        else{
            myProfile();
            return;
        }

        String fullName = otherPerson.getFirstName() + otherPerson.getLastName();
        userNameText.setText(fullName);
        emailText.setText(otherPerson.getEmail());
        bioText.setText(otherPerson.getBio());

        float number = otherPerson.getBirthDate();
        Date date=new Date((long)number);
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        String dateStr = df.format(date);
        birthdayText.setText(dateStr);
        if(activeUserId.equals(otherPerson.getId())){
           // alreadyFriend = true;
            myProfile();
            return;

        }
        for(int k = 0; k< otherPerson.getFriends().size(); k++){
            if(activeUserId.equals(otherPerson.getFriends().get(k))){
                alreadyFriend = true;
                break;
            }
        }
        if(!alreadyFriend){
            removeFriend.setVisibility(View.GONE);
            for(int k = 0; k< otherPerson.getFriendRequests().size(); k++){ //req gönderenlerin idleri
                if(activeUserId.equals(otherPerson.getFriendRequests().get(k))){
                    friendReqSent = true;
                    break;
                }
            }
            if(friendReqSent){
                addFriend.setImageResource(R.drawable.waiting);

            }
            else{

                addFriend.setVisibility(View.VISIBLE);
                addFriend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addFriend(v);
                    }
                });

            }
        }
        else{
            removeFriend.setVisibility(View.VISIBLE);
            addFriend.setImageResource(R.drawable.checked);
            removeFriend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //addFriend.setVisibility(View.VISIBLE);
                    removeFriend(v);

                }
            });

        }

    }

    private void removeFriend(View v) {
        Toast.makeText(getContext(), "You are not friends anymore!", Toast.LENGTH_SHORT).show();
        conn.execute("7", otherPerson.getId());
        removeFriend.setVisibility(View.GONE);
        addFriend.setImageResource(R.drawable.ic_action_user_add);
        addFriend.setVisibility(View.VISIBLE);
        refresh.execute("3");

        //addFriend.setImageResource(R.drawable.ic_action_user_add);

    }

    public void addFriend(View view) throws RuntimeException{
        Toast.makeText(getContext(), "Friend Request Sent!", Toast.LENGTH_SHORT).show();
        conn.execute("6", otherPerson.getId());
        addFriend.setImageResource(R.drawable.waiting);
        refresh.execute("3");

    }
}