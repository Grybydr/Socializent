package com.socializent.application.socializent.Modal;

import java.util.ArrayList;

/**
 * Created by Irem on 17.3.2017.
 */

public class Attendant extends Person {
    private ArrayList<Event> upcomingEvents;
    private ArrayList<Event> pastEvents;

    public Attendant(String name, String surname, String username, String birthdate, String password, String mailAddress, ArrayList<Person> friends, ArrayList<String> interestAreas) {
        super(name, surname, username, birthdate, password, mailAddress, friends, interestAreas);
        upcomingEvents = null;
        pastEvents = null;

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
    public void rateEvent(Event e, int rate){
        int newRate = e.getEventRate()+ rate;
        newRate = newRate/ (e.getAttendants()).size();
        e.setEventRate(newRate);
    }
}
