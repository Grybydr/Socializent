package com.socializent.application.socializent.Fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.location.places.Place;
import com.socializent.application.socializent.R;

/**
 * Created by Zülal Bingöl on 9.04.2017.
 */

public class EventCreateFragment extends DialogFragment implements DialogInterface.OnCancelListener, DialogInterface.OnDismissListener{

    private static Place myPlace;
    private BottomBarMap callerActivity;

    static EventCreateFragment newInstance(Place place) {
        myPlace = place;
        return new EventCreateFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            callerActivity = (BottomBarMap) getTargetFragment();
        } catch (Exception e) {
            Log.d("EVENT CREATE DIALOG", "Class cast error ");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_create_event_form, new RelativeLayout(getActivity()), false);

        TextView eventsDetailTitle = (TextView)view.findViewById(R.id.eventDetailForm);

        TextView placeTag = (TextView)view.findViewById(R.id.placeTag);
        TextView addressView = (TextView)view.findViewById(R.id.addressView);
        addressView.setText(myPlace.getName());

        EditText titleView = (EditText)view.findViewById(R.id.titleView);
        EditText feeView = (EditText)view.findViewById(R.id.feeView);
        EditText timeView = (EditText)view.findViewById(R.id.timeView);
        EditText dateView = (EditText)view.findViewById(R.id.dateView);
        EditText participantCountView = (EditText)view.findViewById(R.id.participantCountView);
        EditText tagsView = (EditText)view.findViewById(R.id.tagsView);
        EditText descriptionView = (EditText)view.findViewById(R.id.descriptionView);

        Button createEventButton = (Button)view.findViewById(R.id.createEventButton);
        createEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //getTargetFragment().onActivityResult(getTargetRequestCode(), getActivity().RESULT_OK, getActivity().getIntent());
                callerActivity.addEvent(myPlace);
                EventCreateFragment.this.dismiss();
            }
        });

        // Build dialog
        Dialog builder = new Dialog(getActivity());
        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        builder.setContentView(view);
        return builder;
    }
}
