package com.socializent.application.socializent.Modal;

import java.util.ArrayList;

/**
 * Created by Irem on 17.3.2017.
 */

public class Attendant extends Person {
    Event event;
    public Attendant(Event e, String name, String surname, String username, String birthdate, String password, String mailAddress, ArrayList<Person> friends, ArrayList<String> interestAreas) {
        super(name, surname, username, birthdate, password, mailAddress, friends, interestAreas);
        event = e;
    }

 /*   public ArrayList<Event> viewUpcomingEvents(Person p){
        return p.upcomingEvents;
    }
    public ArrayList<Event> viewPastEvents(Person p){
        return p.pastEvents;
    }*/
}
