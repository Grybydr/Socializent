package com.socializent.application.socializent;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.socializent.application.socializent.Controller.PersonBackgroundTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;


/**
 * Created by Toshıba on 04/27/2017.
 */

public class FacebookFragment extends Fragment {

    CallbackManager callbackManager;
    //Context context = this;
    private String userFirstName;
    private String userLastName;
    private String userName;
    private String userEmail;
    private String userBirthday;
    private String userPhotoUrl;

    public static FacebookFragment newInstance() {
        return new FacebookFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.facebook_fragment, container, false);

        callbackManager = CallbackManager.Factory.create();

        final LoginButton loginButton = (LoginButton) view.findViewById(R.id.login_button);

        loginButton.setReadPermissions(Arrays.asList(
                "public_profile", "email", "user_birthday", "user_friends","user_about_me","user_actions.music"));

        // If using in a fragment
        loginButton.setFragment(this);
        // Other app specific specialization

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                Log.d("logged in : ", loginResult.getAccessToken().getToken());
                Log.d("logged in : ", AccessToken.getCurrentAccessToken().getToken());
                //String accessToken = loginResult.getAccessToken().getToken();
                GraphRequest graphRequest = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.d("Graph Response: ", response.toString());

                       try {

                                Log.d("Object: ", object.toString());

                                userEmail = object.getString("email");
                                userBirthday = object.getString("birthday");
                                userName = object.getString("name");
                                userFirstName = userName.substring(0,userName.indexOf(' '));
                                userLastName = userName.substring(userName.indexOf(' ')+1,userName.length());
                                JSONObject userPictureObject = new JSONObject(object.getString("picture"));
                                JSONObject userPictureObjectData = new JSONObject(userPictureObject.getString("data"));
                                Log.d("Jsonobj: " , userPictureObject.toString());
                                Log.d("JsonobjData: " , userPictureObjectData.toString());
                                userPhotoUrl = userPictureObjectData.getString("url");

                                Log.d("Email: ", userEmail);
                                Log.d("Birthday: ", userBirthday);
                                Log.d("Username: ", userName);
                                Log.d("First name: ", userFirstName);
                                Log.d("Last name: ", userLastName);
                                Log.d("Photo Url: ", userPhotoUrl);


                                PersonBackgroundTask fbLoginTask = new PersonBackgroundTask(getActivity());

                                fbLoginTask.execute("5",userFirstName,userLastName,userName,userEmail);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender,birthday,about,picture");
                graphRequest.setParameters(parameters);
                graphRequest.executeAsync();



            }

            @Override
            public void onCancel() {
                Log.d("in onCancel: ", "CANCELLED");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("in onError: ", "ERROR OCCURED");
            }
    });
        return view;

}
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


}
