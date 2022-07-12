package com.giacomoferretti.mobilecomputing2022.treest.android.data.source;

import com.giacomoferretti.mobilecomputing2022.treest.android.core.TaskCallback;
import com.giacomoferretti.mobilecomputing2022.treest.android.data.model.Delay;
import com.giacomoferretti.mobilecomputing2022.treest.android.data.model.Line;
import com.giacomoferretti.mobilecomputing2022.treest.android.data.model.Post;
import com.giacomoferretti.mobilecomputing2022.treest.android.data.model.Profile;
import com.giacomoferretti.mobilecomputing2022.treest.android.data.model.Station;
import com.giacomoferretti.mobilecomputing2022.treest.android.data.model.Status;

import java.util.List;

public interface AppRepository {
    void register(TaskCallback<String> callback);

    void getProfile(TaskCallback<Profile> callback);

    boolean shouldSkipFirstTime();

    void setShouldSkipFirstTime(boolean status);

    void setProfile(String name, String picture, TaskCallback<Void> callback);

    String getSessionId();

    void setSessionId(String sessionId);

    void getLines(TaskCallback<List<Line>> callback);

    void getPosts(String directionId, TaskCallback<List<Post>> callback);

    void getStations(String directionId, TaskCallback<List<Station>> callback);

    String getCurrentDirectionId();

    void setCurrentDirectionId(String directionId);

    Line getCurrentLine();

    void setCurrentLine(Line line);

    void addPost(String did, String comment, Delay delay, Status status,
                 TaskCallback<Void> callback);

    void follow(String userId, boolean shouldFollow, TaskCallback<Void> callback);

    void follow(String userId, TaskCallback<Void> callback);

    void unfollow(String userId, TaskCallback<Void> callback);

    void getUserPicture(String userId, int pictureVersion, TaskCallback<String> callback);

    void getUserPicture(String userId, String pictureVersion, TaskCallback<String> callback);
}
