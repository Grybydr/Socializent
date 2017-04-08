package com.socializent.application.socializent.Controller;

import com.socializent.application.socializent.Modal.Event;

import java.util.ArrayList;

/**
 * Created by Irem on 8.4.2017.
 */

public class EventController {

    ArrayList<Event> mapEvents;


    public EventController() {
    }

    public ArrayList<Event> getEventsForMap(){

        //burda server'a request atılacak mapte görünecek eventler için


        return mapEvents;
    }

}
