package com.socializent.application.socializent.Controller;

import com.socializent.application.socializent.Modal.Person;

/**
 * Created by Irem on 7.4.2017.
 */

public class UserController {
    //appi açan user
    Person activeUserOnSystem;



    public UserController() {

    }
    public Person getUserFromServer(String username, String password){

        // GÜRAY buraya server requesti koycak
        //requestten sonra userın bütün bilgileri gelecek

        //muhtemelen serverdan kullanıcının arkadaşlarını çektiğimizde string olarak gelecek onlar
        //o stringleri de ArrayList<Person> friends'e çevirmemiz gerekiyor
        //aynı şekilde interestAreas ı da arrayListe çevirmemiz gerekiyor

        //public Person(String name, String surname, String username, String birthdate, String password, String mailAddress, ArrayList<Person> friends, ArrayList<String> interestAreas)
        activeUserOnSystem = new Person("İrem", "Herguner", username, "28/02/1994", password, "gmail", null, null);
        return activeUserOnSystem;

    }
}
