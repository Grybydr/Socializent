package com.socializent.application.socializent.Modal;

import com.google.android.gms.location.places.Place;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Irem on 16.3.2017.
 */

public class Event {
    private String name;
    private Date date;
    private String address;
    private Place p;
   /* TODO: bu listleri ekranda göstermek için ArrayAdapter kullanılcak
     http://stackoverflow.com/questions/5070830/populating-a-listview-using-an-arraylist
     sitesinden alınacak*/
    private ArrayList<String> interestArea; //buna gerek yok galiba?
    private ArrayList<Attendant> attendants;


    private int participantCount;
    private EventOrganizer eventOrganizer;
    private EventTypes eventType;
    private double eventRate;
    private String description;
    private String category;
    private ArrayList<String> comments;
    private String photoUrl;

    public Event(String name, String description, int fee, Date date, String address, ArrayList<String> interestArea, EventOrganizer eventOrganizer, EventTypes eventType, int eventRate, int participantCount, String category, ArrayList<String> comments, String photoUrl) {
        this.name = name;
        this.participantCount = participantCount;
        this.date = date; //berk milisecond olarak yolluyormuş bunu dönüştürmek lazım
        this.address = address;
        this.interestArea = interestArea;
        this.eventOrganizer = eventOrganizer;

        this.eventType = eventType;
        this.eventRate = eventRate;
        this.description = description;
        this.fee = fee;
        this.category = category;
        this.comments = comments;
        this.photoUrl = photoUrl;
    }

    public Event() {
        this.photoUrl = "";
        this.comments = new ArrayList<String>();
        this.category = "";
        this.fee = 0;
        this.participantCount =0;
        this.name = "Default Event";
        this.date = new Date();
        this.address = "Default Address";
        this.interestArea = new ArrayList<String>();
        this.eventOrganizer = new EventOrganizer();
        this.eventType = EventTypes.BIRTHDAY;
        this.eventRate = 0;
        this.description = "Default Description";
    }

    public int getParticipantCount() {
        return participantCount;
    }

    public void setParticipantCount(int participantCount) {
        this.participantCount = participantCount;
    }
    private int fee;

    public int getFee() {
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public ArrayList<String> getInterestArea() {
        return interestArea;
    }

    public void setInterestArea(ArrayList<String> interestArea) {
        this.interestArea = interestArea;
    }

    public EventOrganizer getEventOrganizer() {
        return eventOrganizer;
    }

    public void setEventOrganizer(EventOrganizer eventOrganizer) {
        this.eventOrganizer = eventOrganizer;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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
