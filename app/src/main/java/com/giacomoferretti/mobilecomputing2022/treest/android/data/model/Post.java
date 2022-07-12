package com.giacomoferretti.mobilecomputing2022.treest.android.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Post {
    // Optional parameters, at least one of them must be provided
    private Integer delay;
    private Integer status;
    private String comment;

    // Required
    private boolean followingAuthor;
    private Date datetime;
    private String authorName;
    @SerializedName("pversion")
    private String pictureVersion;
    @SerializedName("author")
    private String authorId;

    public Post(String comment, boolean followingAuthor, Date datetime, String authorName,
                String pictureVersion, String authorId) {
        this.comment = comment;
        this.followingAuthor = followingAuthor;
        this.datetime = datetime;
        this.authorName = authorName;
        this.pictureVersion = pictureVersion;
        this.authorId = authorId;
    }

    public Integer getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public boolean isFollowingAuthor() {
        return followingAuthor;
    }

    public void setFollowingAuthor(boolean followingAuthor) {
        this.followingAuthor = followingAuthor;
    }

    public Date getDatetime() {
        return datetime;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getPictureVersion() {
        return pictureVersion;
    }

    public void setPictureVersion(String pictureVersion) {
        this.pictureVersion = pictureVersion;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }
}
