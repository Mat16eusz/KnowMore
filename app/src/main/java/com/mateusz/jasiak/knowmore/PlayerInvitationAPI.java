package com.mateusz.jasiak.knowmore;

public class PlayerInvitationAPI {

    private String _id;
    private String myIdSocialMedia;
    private String idSocialMedia;
    private String name;
    private String personPhoto;

    public PlayerInvitationAPI(String _id, String myIdSocialMedia, String idSocialMedia, String name, String personPhoto) {
        this._id = _id;
        this.myIdSocialMedia = myIdSocialMedia;
        this.idSocialMedia = idSocialMedia;
        this.name = name;
        this.personPhoto = personPhoto;
    }

    public String getId() {
        return _id;
    }

    public String getMyIdSocialMedia() {
        return myIdSocialMedia;
    }

    public String getIdSocialMedia() {
        return idSocialMedia;
    }

    public String getName() {
        return name;
    }

    public String getPersonPhoto() {
        return personPhoto;
    }
}
