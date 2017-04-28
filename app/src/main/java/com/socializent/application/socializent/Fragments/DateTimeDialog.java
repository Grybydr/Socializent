package com.socializent.application.socializent.Fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.socializent.application.socializent.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;


/**
 * Created by Zülal Bingöl on 12.04.2017.
 */

//TODO: for Zülal: arrange the code again (comments etc)

public class DateTimeDialog extends DialogFragment implements DialogInterface.OnCancelListener, DialogInterface.OnDismissListener{

    //CONSTANTS
    public static final String TIME = "Time";
    public static final String DATE = "Date";

    //VARIABLES
    private Date myDate;
    private String myDateOrTimeChoice;
    private DatePicker myDatePicker;
    private TimePicker myTimePicker;
    private EventCreateDialog callerActivity;
    private int myYear, myMonth, myDay;

    static DateTimeDialog newInstance() {
        return new DateTimeDialog();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            callerActivity = (EventCreateDialog) getTargetFragment();
        } catch (Exception e) {
            Log.d("DATETIME_PICKER", "Class cast error ");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        myDate = new Date();
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(myDate);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        myYear = year;
        myDay = day;
        myMonth = month;

        @SuppressLint("InflateParams")
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_date_time, null);

        // Spinner to choose either Date or Time to edit
        final Spinner dateTimeSpinner = (Spinner) view.findViewById(R.id.date_time_spinner);
        myDatePicker = (DatePicker) view.findViewById(R.id.datePicker);
        myTimePicker = (TimePicker) view.findViewById(R.id.timePicker);

        myDatePicker.init(year, month, day, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int selectedYear, int selectedMonth,int selectedDay) {
                myYear = selectedYear;
                myDay = selectedDay;
                myMonth = selectedMonth;
            }
        });
        myDatePicker.setMinDate(System.currentTimeMillis() - 1000);

        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        int minuteOfHour = calendar.get(Calendar.MINUTE);

        myTimePicker.setCurrentHour(hourOfDay);
        myTimePicker.setCurrentMinute(minuteOfHour);

        arrangeSpinner(dateTimeSpinner);

        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle(R.string.dateTimeChoice)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (getTargetFragment() == null) {
                            Log.e("DATETIME_PICKER", "Target Fragment is null!");
                        } else {
                            //noinspection deprecation
                            myDate = computeDateFromComponents(
                                    myYear,
                                    myMonth,
                                    myDay,
                                    myTimePicker.getCurrentHour(),
                                    myTimePicker.getCurrentMinute());
                            Intent intent = new Intent();
                            intent.putExtra("DateTimeDialog", myDate);
                                        getTargetFragment().onActivityResult(
                                        getTargetRequestCode(),
                                        Activity.RESULT_OK,
                                        intent);
                            callerActivity.setDate(myDate);
                        }
                    }
                })
                .create();
    }

    private void arrangeSpinner(final Spinner dateTimeSpinner) {

        List<String> choices = new ArrayList<>();
        if (Objects.equals(DATE, myDateOrTimeChoice)) {
            choices.add(DATE);
        } else if (Objects.equals(TIME, myDateOrTimeChoice)) {
            choices.add(TIME);
        } else {
            choices.add(TIME);
            choices.add(DATE);
        }
        dateTimeSpinner.setAdapter(
                new ArrayAdapter<>(
                        getActivity(),
                        android.R.layout.simple_spinner_dropdown_item,
                        choices));
        dateTimeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String choice = (String) dateTimeSpinner.getAdapter().getItem(position);
                if (choice.equalsIgnoreCase(DATE)) {
                    // Make the DatePicker visible
                    myDatePicker.setVisibility(View.VISIBLE);
                    myTimePicker.setVisibility(View.GONE);
                } else {
                    // Make the TimePicker visible
                    myTimePicker.setVisibility(View.VISIBLE);
                    myDatePicker.setVisibility(View.GONE);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        // Select initial choice if neither preferred.
        if (myDateOrTimeChoice == null) {
            dateTimeSpinner.setSelection(choices.indexOf(DATE));
        } else {
            dateTimeSpinner.setSelection(choices.indexOf(myDateOrTimeChoice));
        }
    }

    private Date computeDateFromComponents(int year, int monthOfYear, int dayOfMonth, int hourOfDay, int minuteOfHour) {
        Calendar changedDateCalendar = Calendar.getInstance();
        changedDateCalendar.set(Calendar.YEAR, year);
        changedDateCalendar.set(Calendar.MONTH, monthOfYear);
        changedDateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        changedDateCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        changedDateCalendar.set(Calendar.MINUTE, minuteOfHour);
        changedDateCalendar.set(Calendar.SECOND, 0);
        changedDateCalendar.set(Calendar.MILLISECOND, 0);
        Date ret = changedDateCalendar.getTime();
        Log.d("DATETIME_PICKER", "Returning date: " + ret);
        return ret;
    }
}
