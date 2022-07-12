package com.giacomoferretti.mobilecomputing2022.treest.android.data.model;

import android.graphics.Bitmap;

public class PostProfilePicture extends Post {
    private Bitmap profilePicture;

    public PostProfilePicture(Post post, Bitmap profilePicture) {
        super(post.getComment(), post.isFollowingAuthor(), post.getDatetime(),
                post.getAuthorName(), post.getPictureVersion(), post.getAuthorId());
        this.profilePicture = profilePicture;
    }

    public Bitmap getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(Bitmap profilePicture) {
        this.profilePicture = profilePicture;
    }
}
