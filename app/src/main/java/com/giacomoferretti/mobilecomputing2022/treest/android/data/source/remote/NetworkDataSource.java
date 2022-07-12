package com.giacomoferretti.mobilecomputing2022.treest.android.data.source.remote;

import com.giacomoferretti.mobilecomputing2022.treest.android.core.TaskCallback;
import com.giacomoferretti.mobilecomputing2022.treest.android.data.model.Delay;
import com.giacomoferretti.mobilecomputing2022.treest.android.data.model.Line;
import com.giacomoferretti.mobilecomputing2022.treest.android.data.model.Post;
import com.giacomoferretti.mobilecomputing2022.treest.android.data.model.Profile;
import com.giacomoferretti.mobilecomputing2022.treest.android.data.model.Station;
import com.giacomoferretti.mobilecomputing2022.treest.android.data.model.Status;
import com.giacomoferretti.mobilecomputing2022.treest.android.data.model.UserPicture;
import com.giacomoferretti.mobilecomputing2022.treest.android.data.model.api.SetUserRequest;

import java.util.List;

public interface NetworkDataSource {
    void register(TaskCallback<String> callback);

    void setProfile(SetUserRequest data, TaskCallback<Void> callback);

    void getLines(String sid, TaskCallback<List<Line>> callback);

    void getPosts(String sid, String did, TaskCallback<List<Post>> callback);

    void getStations(String sid, String did, TaskCallback<List<Station>> callback);

    void addPost(String sessionId, String directionId, String comment, Delay delay, Status status
            , TaskCallback<Void> callback);

    void follow(String sessionId, String userId, boolean shouldFollow,
                TaskCallback<Void> callback);

    void getUserPicture(String sessionId, String userId, TaskCallback<UserPicture> callback);

    void getProfile(String sessionId, TaskCallback<Profile> callback);
}
