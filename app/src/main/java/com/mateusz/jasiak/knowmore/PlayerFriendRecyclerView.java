package com.mateusz.jasiak.knowmore;

public class PlayerFriendRecyclerView {

    private String idSocialMediaRecyclerView;
    private String nameRecyclerView;
    private String avatar;

    public PlayerFriendRecyclerView(String idSocialMediaRecyclerView, String nameRecyclerView, String avatar) {
        this.idSocialMediaRecyclerView = idSocialMediaRecyclerView;
        this.nameRecyclerView = nameRecyclerView;
        this.avatar = avatar;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getIdSocialMediaRecyclerView() {
        return idSocialMediaRecyclerView;
    }

    public String getNameRecyclerView() {
        return nameRecyclerView;
    }
}
