package com.mateusz.jasiak.knowmore;

import android.net.Uri;

public class PlayerFriendRecyclerView {
    private Uri avatar; //TODO: Usunąć jeśli nie będzie wykorzystywane
    private String idSocialMediaRecyclerView;
    private String nameRecyclerView;

    public PlayerFriendRecyclerView(String idSocialMediaRecyclerView, String nameRecyclerView) {
        this.idSocialMediaRecyclerView = idSocialMediaRecyclerView;
        this.nameRecyclerView = nameRecyclerView;
    }

    //TODO: Docelowo ma działać to v
    /*public PlayerFriendRecyclerView(Uri avatar, String idSocialMediaRecyclerView, String nameRecyclerView) {
        this.avatar = avatar;
        this.idSocialMediaRecyclerView = idSocialMediaRecyclerView;
        this.nameRecyclerView = nameRecyclerView;
    }*/

    //TODO: Usunąć jeśli nie będzie wykorzystywane
    public Uri getAvatar() {
        return avatar;
    }

    public String getIdSocialMediaRecyclerView() {
        return idSocialMediaRecyclerView;
    }

    public String getNameRecyclerView() {
        return nameRecyclerView;
    }
}
