package com.socializent.application.socializent.Modal;

import java.util.ArrayList;

/**
 * Created by Irem on 17.3.2017.
 */

public class Person {
    private String name;
    private String surname;
    private String username;
    private String birthdate; //TODO: date type mı olsa?
    private String password;
    private String mailAddress;
    private BadgeTypes badgeStatus;
    private ArrayList<Person> friends;
    private ArrayList<String> interestAreas;

    //EventOrganizer için default constructor
    public Person() {
    }

    public Person(String name, String surname, String username, String birthdate, String password, String mailAddress, BadgeTypes badgeStatus, ArrayList<Person> friends, ArrayList<String> interestAreas) {
        this.name = name;
        this.surname = surname;
        this.username = username;
        this.birthdate = birthdate;
        this.password = password;
        this.mailAddress = mailAddress;
        this.badgeStatus = badgeStatus;
        this.friends = friends;
        this.interestAreas = interestAreas;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMailAddress() {
        return mailAddress;
    }

    public void setMailAddress(String mailAddress) {
        this.mailAddress = mailAddress;
    }

    public BadgeTypes getBadgeStatus() {
        return badgeStatus;
    }

    public void setBadgeStatus(BadgeTypes badgeStatus) {
        this.badgeStatus = badgeStatus;
    }

    public ArrayList<Person> getFriends() {
        return friends;
    }

    public void setFriends(ArrayList<Person> friends) {
        this.friends = friends;
    }

    public ArrayList<String> getInterestAreas() {
        return interestAreas;
    }

    public void setInterestAreas(ArrayList<String> interestAreas) {
        this.interestAreas = interestAreas;
    }
}
