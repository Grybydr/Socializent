package com.socializent.application.socializent.Modal;

import java.util.ArrayList;

/**
 * Created by Irem on 17.3.2017.
 */

public class Attendant extends Person {
    private ArrayList<Event> upcomingEvents;
    private ArrayList<Event> pastEvents;

    public Attendant(String firstName, String lastName, String username, float birthDate, String password, String email, String bio,  ArrayList<String> friends, ArrayList<String> interests, ArrayList<Event> events, ArrayList<Event> upcomingEvents, ArrayList<Event> pastEvents, double rate, ArrayList<Event> upcomingEvents1, ArrayList<Event> pastEvents1) {
        super(firstName, lastName, username, birthDate, password, email, bio,  friends, interests, events, upcomingEvents, pastEvents, rate);
        this.upcomingEvents = upcomingEvents1;
        this.pastEvents = pastEvents1;
    }

    public ArrayList<Event> getUpcomingEvents(){
        return upcomingEvents;
    }
    public ArrayList<Event> getPastEvents(){
        return pastEvents;
    }
    public void addUpcomingEvent(Event e){
        upcomingEvents.add(e);
    }
    public void addPastEvent(Event e){
        pastEvents.add(e);
    }
    public void deleteUpcomingEvent(Event e){
        upcomingEvents.remove(e);
    }
    //event rate changed when user rate the event
    public void rateEvent(Event e, double rate){
        double newRate = e.getEventRate()+ rate;
        newRate = newRate/ (e.getAttendants()).size();
        e.setEventRate(newRate);
    }
}
