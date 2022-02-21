package com.mateusz.jasiak.knowmore;

public class PlayersDataAPI {

    private String id;
    private String idSocialMedia;
    private String firstName;
    private String surname;
    private String name;

    public PlayersDataAPI(String id, String idSocialMedia, String firstName, String surname, String name) {
        this.id = id;
        this.idSocialMedia = idSocialMedia;
        this.firstName = firstName;
        this.surname = surname;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getIdSocialMedia() {
        return idSocialMedia;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getSurname() {
        return surname;
    }

    public String getName() {
        return name;
    }
}
