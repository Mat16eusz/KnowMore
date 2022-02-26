package com.mateusz.jasiak.knowmore;

import android.net.Uri;

public class PlayerFriendRecyclerView {
    private Uri avatar;
    private String idSocialMediaRecyclerView;
    private String nameRecyclerView;

    public PlayerFriendRecyclerView(String nameRecyclerView) {
        this.nameRecyclerView = nameRecyclerView;
    }

    //TODO: Docelowo ma działać to v
    /*public PlayerFriendRecyclerView(Uri avatar, String idSocialMediaRecyclerView, String nameRecyclerView) {
        this.avatar = avatar;
        this.idSocialMediaRecyclerView = idSocialMediaRecyclerView;
        this.nameRecyclerView = nameRecyclerView;
    }*/

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
