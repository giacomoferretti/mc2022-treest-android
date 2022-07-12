package com.giacomoferretti.mobilecomputing2022.treest.android.data.model;

import com.google.gson.annotations.SerializedName;

public class Profile {
    @SerializedName("uid")
    private String userId;
    private String name;
    private String picture;
    @SerializedName("pversion")
    private String pictureVersion;

    public Profile(String userId, String name, String picture, String pictureVersion) {
        this.userId = userId;
        this.name = name;
        this.picture = picture;
        this.pictureVersion = pictureVersion;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getPictureVersion() {
        return pictureVersion;
    }

    public void setPictureVersion(String pictureVersion) {
        this.pictureVersion = pictureVersion;
    }
}
