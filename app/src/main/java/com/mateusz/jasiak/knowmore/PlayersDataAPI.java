package com.mateusz.jasiak.knowmore;

public class PlayersDataAPI {

    private String id;
    private String idSocialMedia;
    private String firstName;
    private String surname;
    private String name;
    private String personPhoto;

    public PlayersDataAPI(String id, String idSocialMedia, String firstName, String surname, String name, String personPhoto) {
        this.id = id;
        this.idSocialMedia = idSocialMedia;
        this.firstName = firstName;
        this.surname = surname;
        this.name = name;
        this.personPhoto = personPhoto;
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
}
