package com.giacomoferretti.mobilecomputing2022.treest.android.data.source;

import android.content.Context;
import android.util.Log;

import com.giacomoferretti.mobilecomputing2022.treest.android.core.Result;
import com.giacomoferretti.mobilecomputing2022.treest.android.data.model.Delay;
import com.giacomoferretti.mobilecomputing2022.treest.android.data.model.Line;
import com.giacomoferretti.mobilecomputing2022.treest.android.data.model.Post;
import com.giacomoferretti.mobilecomputing2022.treest.android.data.model.Profile;
import com.giacomoferretti.mobilecomputing2022.treest.android.data.model.Station;
import com.giacomoferretti.mobilecomputing2022.treest.android.data.model.Status;
import com.giacomoferretti.mobilecomputing2022.treest.android.data.model.UserPicture;
import com.giacomoferretti.mobilecomputing2022.treest.android.data.model.api.SetUserRequest;
import com.giacomoferretti.mobilecomputing2022.treest.android.data.source.local.db.AppDatabase;
import com.giacomoferretti.mobilecomputing2022.treest.android.data.source.local.db.LocalDatabaseDataSource;
import com.giacomoferretti.mobilecomputing2022.treest.android.data.source.local.prefs.LocalPreferencesDataSource;
import com.giacomoferretti.mobilecomputing2022.treest.android.data.source.remote.RemoteDataSource;
import com.giacomoferretti.mobilecomputing2022.treest.android.core.TaskCallback;

import java.util.List;

public class AppDataRepository implements AppRepository {
    private static volatile AppDataRepository sInstance;
    private final RemoteDataSource remoteDataSource;
    private final LocalDatabaseDataSource localDatabaseDataSource;
    private final LocalPreferencesDataSource localPrefsDataSource;

    private AppDataRepository(RemoteDataSource remoteDataSource,
                              LocalDatabaseDataSource localDatabaseDataSource,
                              LocalPreferencesDataSource localPrefsDataSource) {
        this.remoteDataSource = remoteDataSource;
        this.localDatabaseDataSource = localDatabaseDataSource;
        this.localPrefsDataSource = localPrefsDataSource;
    }

    public static AppDataRepository getInstance(final Context context) {
        if (sInstance == null) {
            synchronized (AppDatabase.class) {
                if (sInstance == null) {
                    sInstance = new AppDataRepository(
                            RemoteDataSource.getInstance(context.getApplicationContext()),
                            LocalDatabaseDataSource.getInstance(context.getApplicationContext()),
                            LocalPreferencesDataSource.getInstance(context.getApplicationContext())
                    );
                }
            }
        }
        return sInstance;
    }

    @Override
    public void register(TaskCallback<String> callback) {
        remoteDataSource.register(callback);
    }

    @Override
    public void getProfile(TaskCallback<Profile> callback) {
        remoteDataSource.getProfile(getSessionId(), callback);
    }

    @Override
    public boolean shouldSkipFirstTime() {
        return localPrefsDataSource.skipFirstTime();
    }

    @Override
    public void setShouldSkipFirstTime(boolean status) {
        localPrefsDataSource.setSkipFirstTime(status);
    }

    @Override
    public void setProfile(String name, String picture, TaskCallback<Void> callback) {
        if (name == null && picture == null) {
            callback.onFinish(Result.error("You need to provide at least one of the following " +
                    "parameters: name, picture", null));
            return;
        }

        remoteDataSource.setProfile(
                new SetUserRequest.Builder(getSessionId())
                        .setName(name)
                        .setPicture(picture)
                        .build(),
                callback
        );
    }

    @Override
    public String getSessionId() {
        return localPrefsDataSource.getSessionId();
    }

    @Override
    public void setSessionId(String sessionId) {
        localPrefsDataSource.setSessionId(sessionId);
    }

    @Override
    public void getLines(TaskCallback<List<Line>> callback) {
        remoteDataSource.getLines(getSessionId(), callback);
    }

    @Override
    public void getPosts(String directionId, TaskCallback<List<Post>> callback) {
        remoteDataSource.getPosts(getSessionId(), directionId, callback);
    }

    @Override
    public void getStations(String directionId, TaskCallback<List<Station>> callback) {
        remoteDataSource.getStations(getSessionId(), directionId, callback);
    }

    @Override
    public String getCurrentDirectionId() {
        return localPrefsDataSource.getCurrentDirectionId();
    }

    @Override
    public void setCurrentDirectionId(String directionId) {
        localPrefsDataSource.setCurrentDirectionId(directionId);
    }

    @Override
    public Line getCurrentLine() {
        return localPrefsDataSource.getCurrentLine();
    }

    @Override
    public void setCurrentLine(Line line) {
        localPrefsDataSource.setCurrentLine(line);
    }

    @Override
    public void addPost(String did, String comment, Delay delay, Status status,
                        TaskCallback<Void> callback) {
        remoteDataSource.addPost(getSessionId(), did, comment, delay, status, callback);
    }

    @Override
    public void follow(String userId, boolean shouldFollow, TaskCallback<Void> callback) {
        remoteDataSource.follow(getSessionId(), userId, shouldFollow, callback);
    }

    @Override
    public void follow(String userId, TaskCallback<Void> callback) {
        remoteDataSource.follow(getSessionId(), userId, true, callback);
    }

    @Override
    public void unfollow(String userId, TaskCallback<Void> callback) {
        remoteDataSource.follow(getSessionId(), userId, false, callback);
    }

    @Override
    public void getUserPicture(String userId, int pictureVersion,
                               TaskCallback<String> callback) {
       getUserPicture(userId, String.valueOf(pictureVersion), callback);
    }

    @Override
    public void getUserPicture(String userId, String pictureVersion,
                               TaskCallback<String> callback) {
        if (pictureVersion.equals("0")) {
            callback.onFinish(Result.success(null));
            return;
        }

        localDatabaseDataSource.getUserPictureByUserIdAndVersion(userId, pictureVersion, result -> {
            if (result.getStatus() == com.giacomoferretti.mobilecomputing2022.treest.android.core.Status.SUCCESS && result.getData() != null) {
                callback.onFinish(Result.success(result.getData().getPicture()));
                return;
            }

            if (result.getStatus() == com.giacomoferretti.mobilecomputing2022.treest.android.core.Status.ERROR) {
                Log.d("AppDataRepository", "[getUserPicture] " + result.getMessage());
            }

            Log.d("AppDataRepository", "[getUserPicture] Getting from remote...");
            remoteDataSource.getUserPicture(getSessionId(), userId, result1 -> {
                if (result1.getStatus() == com.giacomoferretti.mobilecomputing2022.treest.android.core.Status.SUCCESS && result1.getData() != null) {
                    localDatabaseDataSource.savePicture(result1.getData());
                    callback.onFinish(Result.success(result1.getData().getPicture()));
                    return;
                }

                callback.onFinish(Result.error("getUserPicture", null));
            });
        });
    }
}
