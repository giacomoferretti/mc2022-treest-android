package com.giacomoferretti.mobilecomputing2022.treest.android.data.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.NotNull;

@Entity(tableName = "user_picture", primaryKeys = {"uid", "pversion"})
public class UserPicture {
    @NotNull
    @SerializedName("uid")
    @ColumnInfo(name = "uid")
    private String userId;

    @NotNull
    @SerializedName("pversion")
    @ColumnInfo(name = "pversion")
    private String pictureVersion;

    @SerializedName("picture")
    private String picture;

    public UserPicture(@NotNull String userId, @NotNull String pictureVersion, String picture) {
        this.userId = userId;
        this.pictureVersion = pictureVersion;
        this.picture = picture;
    }

    @NotNull
    public String getUserId() {
        return userId;
    }

    public void setUserId(@NotNull String userId) {
        this.userId = userId;
    }

    @NotNull
    public String getPictureVersion() {
        return pictureVersion;
    }

    public void setPictureVersion(@NotNull String pictureVersion) {
        this.pictureVersion = pictureVersion;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}