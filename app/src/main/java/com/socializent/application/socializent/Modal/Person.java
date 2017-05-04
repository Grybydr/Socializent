package com.socializent.application.socializent.Modal;

import com.google.android.gms.location.places.Place;

import java.util.ArrayList;

/**
 * Created by Irem on 17.3.2017.
 */

public class Person {
    private String firstName;
    private String lastName;
    private String username;
    private float birthDate; //TODO: date type mı olsa?
    private String password;
    private String email;
    private String photoUrl;
    private String bio;
    private String id;
    
    private ArrayList<String> friends;
    private ArrayList<String> interests;
    private ArrayList<String> friendRequests;
    private ArrayList<Event> events;
    private ArrayList<Event> pastEvents;
    private ArrayList<Event> upcomingEvents;

    private ArrayList<Event> organizedEvents;
    private Place place;
    private double rate;

    //EventOrganizer için default constructor
    public Person() {
        this.firstName = "";
        this.lastName = "";
        this.username = "";
        this.birthDate = 0;
        this.password = "";
        this.email = "";
        this.bio = "";
        this.rate = 0.0;
        this.friends = new ArrayList<String>();
        this.interests = new ArrayList<String>();
        this.events = new ArrayList<Event>();
        this.upcomingEvents = new ArrayList<Event>();
        this.pastEvents = new ArrayList<Event>();
    }


    public Person(String firstName, String lastName, String username, float birthDate, String password, String email,String bio,  ArrayList<String> friends, ArrayList<String> interests,ArrayList<Event> events,ArrayList<Event> upcomingEvents,ArrayList<Event> pastEvents,double rate) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.birthDate = birthDate;
        this.password  = password;
        this.email     = email;
        this.bio    = bio;
        this.rate      = rate;
        this.friends    = friends;
        this.interests   = interests;
        this.events     = events;
        this.upcomingEvents = upcomingEvents;
        this.pastEvents = pastEvents;
    }
    public Person(String id,String firstName, String lastName, String username, float birthDate, String password, String email,String bio,  ArrayList<String> friends, ArrayList<String> interests,ArrayList<Event> events,ArrayList<Event> upcomingEvents,ArrayList<Event> pastEvents, ArrayList<String> friendRequest) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.birthDate = birthDate;
        this.password  = password;
        this.email     = email;
        this.bio    = bio;
        this.friends    = friends;
        this.interests   = interests;
        this.events     = events;
        this.upcomingEvents = upcomingEvents;
        this.pastEvents = pastEvents;
        this.friendRequests = friendRequest;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public float getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(float birthDate) {
        this.birthDate = birthDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ArrayList<String> getInterests() {
        return interests;
    }

    public void setInterests(ArrayList<String> interests) {
        this.interests = interests;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ArrayList<String> getFriends() {
        return friends;
    }

    public void setFriends(ArrayList<String> friends) {
        this.friends = friends;
    }

    public void setinterests(ArrayList<String> interests) {
        this.interests = interests;
    }
    public void editProfile(String name, String lastName, String username, float birthDate, String password, String email, ArrayList<Person> friends, ArrayList<String> interests,String bio) {
        this.firstName = name;
        this.lastName = lastName;
        this.username = username;
        this.birthDate = birthDate;
        this.password = password;
        this.email = email;
        this.interests = interests;
        this.bio = bio;
    }


    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getBio() {
        return bio;

    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public ArrayList<String> getFriendRequests() {
        return friendRequests;
    }

    public void setFriendRequests(ArrayList<String> friendRequests) {
        this.friendRequests = friendRequests;
    }

    public ArrayList<Event> getEvents() {
        return events;
    }

    public void setEvents(ArrayList<Event> events) {
        this.events = events;
    }

    public ArrayList<Event> getPastEvents() {
        return pastEvents;
    }

    public void setPastEvents(ArrayList<Event> pastEvents) {
        this.pastEvents = pastEvents;
    }

    public ArrayList<Event> getUpcomingEvents() {
        return upcomingEvents;
    }

    public void setUpcomingEvents(ArrayList<Event> upcomingEvents) {
        this.upcomingEvents = upcomingEvents;
    }

    public ArrayList<Event> getOrganizedEvents() {
        return organizedEvents;
    }

    public void setOrganizedEvents(ArrayList<Event> organizedEvents) {
        this.organizedEvents = organizedEvents;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }
}
