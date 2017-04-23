package com.socializent.application.socializent.Modal;

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
   /* TODO: bu listleri ekranda göstermek için ArrayAdapter kullanılcak
     http://stackoverflow.com/questions/5070830/populating-a-listview-using-an-arraylist
     sitesinden alınacak*/
    private ArrayList<String> interestArea; //buna gerek yok galiba?
    private ArrayList<Attendant> attendants;
    private EventOrganizer eventOrganizer;
    private boolean payable;
    private EventTypes eventType;
    private int eventRate;
    private String description;

    public Event(String name,String description, Date date, String address, ArrayList<String> interestArea, EventOrganizer eventOrganizer, boolean payable, EventTypes eventType, int eventRate) {
        this.name = name;
        this.date = date; //berk milisecond olarak yolluyormuş bunu dönüştürmek lazım
        this.address = address;
        this.interestArea = interestArea;
        this.eventOrganizer = eventOrganizer;
        this.payable = payable;
        this.eventType = eventType;
        this.eventRate = eventRate;
        this.description = description;
    }

    public Event() {
        this.name = "Default Event";
        this.date = new Date();
        this.address = "Default Address";
        this.interestArea = new ArrayList<String>();
        this.eventOrganizer = new EventOrganizer();
        this.payable = false;
        this.eventType = EventTypes.BIRTHDAY;
        this.eventRate = 0;
        this.description = "Default Description";
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

    public boolean isPayable() {
        return payable;
    }

    public void setPayable(boolean payable) {
        this.payable = payable;
    }

    public EventTypes getEventType() {
        return eventType;
    }

    public void setEventType(EventTypes eventType) {
        this.eventType = eventType;
    }

    public int getEventRate() {
        return eventRate;
    }

    public void setEventRate(int eventRate) {
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
}
