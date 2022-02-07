package com.mateusz.jasiak.knowmore;

public class PlayersDataAPI {

    private String name;
    private String surname;
    private String id;

    public PlayersDataAPI(String name, String surname, String id) {
        this.name = name;
        this.surname = surname;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getId() {
        return id;
    }
}
