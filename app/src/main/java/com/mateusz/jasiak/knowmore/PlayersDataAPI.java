package com.mateusz.jasiak.knowmore;

public class PlayersDataAPI {

    private String id;
    private String idSocialMedia;
    private String firstName;
    private String surname;
    private String name;
    private String personPhoto;
    private String token;

    public PlayersDataAPI(String id, String idSocialMedia, String firstName, String surname, String name, String personPhoto, String token) {
        this.id = id;
        this.idSocialMedia = idSocialMedia;
        this.firstName = firstName;
        this.surname = surname;
        this.name = name;
        this.personPhoto = personPhoto;
        this.token = token;
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

    public String getPersonPhoto() {
        return personPhoto;
    }

    public String getToken() {
        return token;
    }
}
