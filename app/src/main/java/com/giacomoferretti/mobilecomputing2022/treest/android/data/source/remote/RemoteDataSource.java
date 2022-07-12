package com.giacomoferretti.mobilecomputing2022.treest.android.data.source.remote;

import android.content.Context;
import android.util.Log;

import com.giacomoferretti.mobilecomputing2022.treest.android.core.AppExecutors;
import com.giacomoferretti.mobilecomputing2022.treest.android.core.TaskCallback;
import com.giacomoferretti.mobilecomputing2022.treest.android.core.Result;
import com.giacomoferretti.mobilecomputing2022.treest.android.data.model.Delay;
import com.giacomoferretti.mobilecomputing2022.treest.android.data.model.Line;
import com.giacomoferretti.mobilecomputing2022.treest.android.data.model.Post;
import com.giacomoferretti.mobilecomputing2022.treest.android.data.model.Profile;
import com.giacomoferretti.mobilecomputing2022.treest.android.data.model.Station;
import com.giacomoferretti.mobilecomputing2022.treest.android.data.model.Status;
import com.giacomoferretti.mobilecomputing2022.treest.android.data.model.UserPicture;
import com.giacomoferretti.mobilecomputing2022.treest.android.data.model.api.AddPostRequest;
import com.giacomoferretti.mobilecomputing2022.treest.android.data.model.api.LinesResponse;
import com.giacomoferretti.mobilecomputing2022.treest.android.data.model.api.PostsResponse;
import com.giacomoferretti.mobilecomputing2022.treest.android.data.model.api.SetUserRequest;
import com.giacomoferretti.mobilecomputing2022.treest.android.data.model.api.SidDidRequest;
import com.giacomoferretti.mobilecomputing2022.treest.android.data.model.api.SidRequest;
import com.giacomoferretti.mobilecomputing2022.treest.android.data.model.api.SidUidRequest;
import com.giacomoferretti.mobilecomputing2022.treest.android.data.model.api.StationsResponse;
import com.giacomoferretti.mobilecomputing2022.treest.android.data.source.local.db.AppDatabase;
import com.giacomoferretti.mobilecomputing2022.treest.android.di.AppContainer;
import com.giacomoferretti.mobilecomputing2022.treest.android.utils.NetworkUtils;

import java.util.List;

import retrofit2.Response;

public class RemoteDataSource implements NetworkDataSource {
    private static volatile RemoteDataSource sInstance;
    private final AppExecutors mAppExecutors;

    private RemoteDataSource(AppExecutors appExecutors) {
        this.mAppExecutors = appExecutors;
    }

    public static RemoteDataSource getInstance(final Context context) {
        if (sInstance == null) {
            synchronized (AppDatabase.class) {
                if (sInstance == null) {
                    sInstance =
                            new RemoteDataSource(AppContainer.getInstance(context).getAppExecutors());
                }
            }
        }
        return sInstance;
    }

    @Override
    public void register(TaskCallback<String> callback) {
        mAppExecutors.networkIO().execute(() -> {
            Result<String> result = NetworkUtils.safeApiCall(() -> {
                Response<SidRequest> response =
                        TreEstRemote.getInstance().getService().register().execute();

                if (response.isSuccessful()) {
                    SidRequest body = response.body();
                    if (body != null) {
                        return Result.success(body.getSid());
                    }
                }

                return Result.error("Could not register", null);
            }, "Undefined error!");

            callback.onFinish(result);
        });
    }

    @Override
    public void setProfile(SetUserRequest data, TaskCallback<Void> callback) {
        mAppExecutors.networkIO().execute(() -> {
            Result<Void> result = NetworkUtils.safeApiCall(() -> {
                Response<Void> response =
                        TreEstRemote.getInstance().getService().setProfile(data).execute();

                if (response.isSuccessful()) {
                    return Result.success(null);
                }

                return Result.error("Could not setProfile", null);
            }, "Undefined error!");

            callback.onFinish(result);
        });
    }

    @Override
    public void getLines(String sid, TaskCallback<List<Line>> callback) {
        mAppExecutors.networkIO().execute(() -> {
            Result<List<Line>> result = NetworkUtils.safeApiCall(() -> {
                Response<LinesResponse> response =
                        TreEstRemote.getInstance().getService().getLines(new SidRequest(sid)).execute();

                if (response.isSuccessful()) {
                    LinesResponse body = response.body();
                    if (body != null) {
                        return Result.success(body.getLines());
                    }
                }

                return Result.error("Could not getLines", null);
            }, "Undefined error!");

            callback.onFinish(result);
        });
    }

    @Override
    public void getPosts(String sid, String did, TaskCallback<List<Post>> callback) {
        mAppExecutors.networkIO().execute(() -> {
            Result<List<Post>> result = NetworkUtils.safeApiCall(() -> {
                Response<PostsResponse> response =
                        TreEstRemote.getInstance().getService().getPosts(new SidDidRequest(sid,
                                did)).execute();

                if (response.isSuccessful()) {
                    PostsResponse body = response.body();
                    if (body != null) {
                        return Result.success(body.getPosts());
                    }
                }

                return Result.error("Could not getLines", null);
            }, "Undefined error!");

            callback.onFinish(result);
        });
    }

    @Override
    public void getStations(String sid, String did, TaskCallback<List<Station>> callback) {
        mAppExecutors.networkIO().execute(() -> {
            Result<List<Station>> result = NetworkUtils.safeApiCall(() -> {
                Response<StationsResponse> response =
                        TreEstRemote.getInstance().getService().getStations(new SidDidRequest(sid,
                                did)).execute();

                if (response.isSuccessful()) {
                    StationsResponse body = response.body();
                    if (body != null) {
                        return Result.success(body.getStations());
                    }
                }

                return Result.error("Could not getStations", null);
            }, "Undefined error!");

            callback.onFinish(result);
        });
    }

    @Override
    public void addPost(String sessionId, String directionId, String comment, Delay delay,
                        Status status, TaskCallback<Void> callback) {
        mAppExecutors.networkIO().execute(() -> {
            Result<Void> result = NetworkUtils.safeApiCall(() -> {
                Response<Void> response =
                        TreEstRemote.getInstance().getService()
                                .addPost(new AddPostRequest(
                                        sessionId,
                                        directionId,
                                        comment,
                                        delay,
                                        status))
                                .execute();

                if (response.isSuccessful()) {
                    return Result.success(null);
                }

                return Result.error("Could not addPost", null);
            }, "Undefined error!");

            callback.onFinish(result);
        });
    }

    @Override
    public void follow(String sessionId, String userId, boolean shouldFollow,
                       TaskCallback<Void> callback) {
        mAppExecutors.networkIO().execute(() -> {
            Result<Void> result = NetworkUtils.safeApiCall(() -> {
                Response<Void> response;
                if (shouldFollow) {
                    response =
                            TreEstRemote.getInstance().getService().follow(new SidUidRequest(sessionId, userId)).execute();
                } else {
                    response =
                            TreEstRemote.getInstance().getService().unfollow(new SidUidRequest(sessionId, userId)).execute();
                }

                if (response.isSuccessful()) {
                    return Result.success(null);
                }

                return Result.error("Could not follow", null);
            }, "Undefined error!");

            callback.onFinish(result);
        });
    }

    @Override
    public void getUserPicture(String sessionId, String userId,
                               TaskCallback<UserPicture> callback) {
        mAppExecutors.networkIO().execute(() -> {
            Result<UserPicture> result = NetworkUtils.safeApiCall(() -> {
                Response<UserPicture> response =
                        TreEstRemote.getInstance().getService().getUserPicture(new SidUidRequest(sessionId, userId)).execute();

                Log.d("FIND FIND FIND", response.isSuccessful() + "");
                if (response.isSuccessful()) {
                    UserPicture body = response.body();
                    if (body != null) {
                        return Result.success(body);
                    }
                }

                return Result.error("Could not getUserPicture", null);
            }, "Cannot get user picture " + userId);

            callback.onFinish(result);
        });
    }

    @Override
    public void getProfile(String sessionId, TaskCallback<Profile> callback) {
        mAppExecutors.networkIO().execute(() -> {
            Result<Profile> result = NetworkUtils.safeApiCall(() -> {
                Response<Profile> response =
                        TreEstRemote.getInstance().getService().getProfile(new SidRequest(sessionId)).execute();

                if (response.isSuccessful()) {
                    Profile body = response.body();
                    if (body != null) {
                        return Result.success(body);
                    }
                }

                return Result.error("Could not getProfile", null);
            }, "Cannot get user");

            callback.onFinish(result);
        });
    }
}