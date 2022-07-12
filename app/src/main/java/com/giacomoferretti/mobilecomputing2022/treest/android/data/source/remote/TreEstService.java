package com.giacomoferretti.mobilecomputing2022.treest.android.data.source.remote;

import com.giacomoferretti.mobilecomputing2022.treest.android.data.model.Profile;
import com.giacomoferretti.mobilecomputing2022.treest.android.data.model.UserPicture;
import com.giacomoferretti.mobilecomputing2022.treest.android.data.model.api.AddPostRequest;
import com.giacomoferretti.mobilecomputing2022.treest.android.data.model.api.LinesResponse;
import com.giacomoferretti.mobilecomputing2022.treest.android.data.model.api.PostsResponse;
import com.giacomoferretti.mobilecomputing2022.treest.android.data.model.api.SetUserRequest;
import com.giacomoferretti.mobilecomputing2022.treest.android.data.model.api.SidDidRequest;
import com.giacomoferretti.mobilecomputing2022.treest.android.data.model.api.SidRequest;
import com.giacomoferretti.mobilecomputing2022.treest.android.data.model.api.SidUidRequest;
import com.giacomoferretti.mobilecomputing2022.treest.android.data.model.api.StationsResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface TreEstService {
    @GET("register.php")
    Call<SidRequest> register();

    @POST("getProfile.php")
    Call<Profile> getProfile(@Body SidRequest body);

    @POST("setProfile.php")
    Call<Void> setProfile(@Body SetUserRequest body);

    @POST("getLines.php")
    Call<LinesResponse> getLines(@Body SidRequest body);

    @POST("getStations.php")
    Call<StationsResponse> getStations(@Body SidDidRequest body);

    @POST("getPosts.php")
    Call<PostsResponse> getPosts(@Body SidDidRequest body);

    @POST("addPost.php")
    Call<Void> addPost(@Body AddPostRequest body);

    @POST("getUserPicture.php")
    Call<UserPicture> getUserPicture(@Body SidUidRequest body);

    @POST("follow.php")
    Call<Void> follow(@Body SidUidRequest body);

    @POST("unfollow.php")
    Call<Void> unfollow(@Body SidUidRequest body);
}
