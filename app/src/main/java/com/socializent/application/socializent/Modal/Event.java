package com.socializent.application.socializent.Modal;

import android.location.Location;

import com.google.android.gms.location.places.Place;

import org.json.JSONArray;
import org.json.JSONException;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Irem on 16.3.2017.
 */

public class Event {
    private String name;
    private long date;
    private String address;
    Location l;
    private ArrayList<Attendant> attendants;
    private int participantCount;
    private EventTypes eventType;
    private double eventRate;
    private String description;
    private ArrayList<String> comments;
    private String photoUrl;
    String city;
    String placeName;
    String organizer;
    private double fee;

    public Event(String name, String description, double fee, long date, String address,  String organizerId, EventTypes category, int eventRate, int participantCount,  ArrayList<String> comments, String photoUrl, Location l, String city, String placeName) {
        this.name = name;
        this.participantCount = participantCount;
        this.date = date; //berk milisecond olarak yolluyormuş bunu dönüştürmek lazım
        this.address = address;
        this.eventType = category;
        this.eventRate = eventRate;
        this.description = description;
        this.organizer = organizerId;
        this.fee = fee;
        this.comments = comments;
        this.photoUrl = photoUrl;
        this.l = l;
        this.city = city;
        this.placeName = placeName;
    }

    public Event() {

        this.eventType = EventTypes.CONFERENCE;
        this.organizer ="";
        this.photoUrl = "";
        this.comments = new ArrayList<String>();
        this.fee = 0;
        this.participantCount =0;
        this.name = "Default Event";
        this.date = 0;
        this.address = "Default Address";
        this.eventRate = 0;
        this.description = "Default Description";
        this.l = null;
        this.city = "";
        this.placeName = "";

    }

    public int getParticipantCount() {
        return participantCount;
    }

    public void setParticipantCount(int participantCount) {
        this.participantCount = participantCount;
    }


    public Location getL() {
        return l;
    }

    public void setL(Location l) {
        this.l = l;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getOrganizer() {
        return organizer;
    }

    public void setOrganizer(String organizer) {
        this.organizer = organizer;
    }

    public double getFee() {
        return fee;
    }

    public void setFee(int fee) {
        this.fee = fee;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public EventTypes getEventType() {
        return eventType;
    }

    public void setEventType(EventTypes eventType) {
        this.eventType = eventType;
    }

    public double getEventRate() {
        return eventRate;
    }

    public void setEventRate(double eventRate) {
        this.eventRate = eventRate;
    }

    public ArrayList<Attendant> getAttendants() {
        return attendants;
    }

    public void setAttendants(ArrayList<Attendant> attendants) {
        this.attendants = attendants;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public ArrayList<String> getComments() {
        return comments;
    }

    public void setComments(ArrayList<String> comments) {
        this.comments = comments;
    }


}
