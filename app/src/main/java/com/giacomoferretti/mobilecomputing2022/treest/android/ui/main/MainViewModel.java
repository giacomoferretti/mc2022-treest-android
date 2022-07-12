package com.giacomoferretti.mobilecomputing2022.treest.android.ui.main;

import android.app.Application;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.giacomoferretti.mobilecomputing2022.treest.android.core.AppExecutors;
import com.giacomoferretti.mobilecomputing2022.treest.android.core.Event;
import com.giacomoferretti.mobilecomputing2022.treest.android.data.model.Delay;
import com.giacomoferretti.mobilecomputing2022.treest.android.data.model.Post;
import com.giacomoferretti.mobilecomputing2022.treest.android.data.model.PostProfilePicture;
import com.giacomoferretti.mobilecomputing2022.treest.android.data.model.Profile;
import com.giacomoferretti.mobilecomputing2022.treest.android.data.model.Status;
import com.giacomoferretti.mobilecomputing2022.treest.android.data.source.AppDataRepository;
import com.giacomoferretti.mobilecomputing2022.treest.android.di.AppContainer;
import com.giacomoferretti.mobilecomputing2022.treest.android.utils.AvatarGenerator;

import java.util.ArrayList;
import java.util.List;

public class MainViewModel extends AndroidViewModel {
    private final AppDataRepository mAppDataRepository;
    private final AppExecutors mAppExecutors;

    private final MutableLiveData<String> mDirectionId = new MutableLiveData<>();
    private final MutableLiveData<String> mTerminusName = new MutableLiveData<>();
    private final MutableLiveData<List<Post>> mPosts = new MutableLiveData<>();
    private final MutableLiveData<Boolean> mIsLoading = new MutableLiveData<>();
    private final MutableLiveData<Event<Boolean>> mAddPostEvent = new MutableLiveData<>();
    private final MutableLiveData<Event<Boolean>> mUpdateProfileEvent = new MutableLiveData<>();
    private final MutableLiveData<Profile> mProfile = new MutableLiveData<>();
    private final MutableLiveData<String> mNewProfilePicture = new MutableLiveData<>();

    public MainViewModel(@NonNull Application application) {
        super(application);
        mAppDataRepository = AppDataRepository.getInstance(application);
        mAppExecutors = AppContainer.getInstance(application).getAppExecutors();
    }

    public LiveData<Event<Boolean>> getAddPostEvent() {
        return mAddPostEvent;
    }

    public LiveData<String> getDirectionId() {
        return mDirectionId;
    }

    public void setDirectionId(String directionId) {
        Log.d("setDirectionId", directionId);
        mDirectionId.setValue(directionId);
        loadPosts(directionId, true);
    }

    public void loadProfile() {
        mAppDataRepository.getProfile(result -> {
            if (result.getStatus() == com.giacomoferretti.mobilecomputing2022.treest.android.core.Status.SUCCESS) {
                mProfile.postValue(result.getData());
            }
        });
    }

    public LiveData<Event<Boolean>> getUpdateProfileEvent() {
        return mUpdateProfileEvent;
    }

    public LiveData<String> getNewProfilePicture() {
        return mNewProfilePicture;
    }

    public void setNewProfilePicture(String picture) {
        mNewProfilePicture.setValue(picture);
    }

    public LiveData<Profile> getProfile() {
        loadProfile();
        return mProfile;
    }

    public void setProfile(String name, String picture) {
        mAppDataRepository.setProfile(name, picture, result -> {
            if (result.getStatus() == com.giacomoferretti.mobilecomputing2022.treest.android.core.Status.SUCCESS) {
                mUpdateProfileEvent.postValue(new Event<>(Boolean.TRUE));
            } else {
                mUpdateProfileEvent.postValue(new Event<>(Boolean.FALSE));
            }
        });
    }

    public LiveData<String> getTerminusName() {
        return mTerminusName;
    }

    public void setTerminusName(String terminusName) {
        mTerminusName.setValue(terminusName);
    }

    public LiveData<List<Post>> getPosts() {
        return mPosts;
    }

    public LiveData<Boolean> isLoading() {
        return mIsLoading;
    }

    public void loadPosts(String directionId) {
        loadPosts(directionId, false);
    }

    public void loadPosts(String directionId, boolean forceUpdate) {
        if (mPosts.getValue() == null || forceUpdate) {
            mIsLoading.postValue(true);

            mAppDataRepository.getPosts(directionId, result -> {
                if (result.getStatus() == com.giacomoferretti.mobilecomputing2022.treest.android.core.Status.SUCCESS) {
                    /*List<Post> posts = result.getData();

                    List<PostProfilePicture> postsWithProfilePictures = new ArrayList<>();
                    if (posts != null) {
                        for (int i = 0; i < posts.size(); i++) {
                            postsWithProfilePictures.add(
                                    new PostProfilePicture(
                                            posts.get(i),
                                            new AvatarGenerator(getApplication().getApplicationContext())
                                                    .setSize(128)
                                                    .setTextSize(24)
                                                    .setLabel(posts.get(i).getAuthorName())
                                                    .build()
                                    )
                            );
                        }
                    }

                    mAppExecutors.mainThread().execute(() -> {
                        mPosts.setValue(postsWithProfilePictures);
                    });

                    for (int i = 0; i < postsWithProfilePictures.size(); i++) {
                        int finalI = i;
                        mAppDataRepository.getUserPicture(
                                postsWithProfilePictures.get(i).getAuthorId(),
                                postsWithProfilePictures.get(i).getPictureVersion(),
                                picture -> {
                                    if (picture.getStatus() == com.giacomoferretti.mobilecomputing2022.treest.android.core.Status.SUCCESS && picture.getData() != null) {
                                        byte[] decodedData = Base64.decode(picture.getData(),
                                                Base64.DEFAULT);

                                        mAppExecutors.mainThread().execute(() -> {
                                            mPosts.getValue().get(finalI).setProfilePicture(BitmapFactory.decodeByteArray(decodedData
                                                    , 0,
                                                    decodedData.length));
                                        });
                                    }
                                });
                    }*/

                    mPosts.postValue(result.getData());
                    mIsLoading.postValue(false);
                }
            });
        }
    }

    public void publish(String comment, Delay delay, Status status) {
        mAppDataRepository.addPost(mDirectionId.getValue(), comment, delay, status,
                result -> {
                    if (result.getStatus() == com.giacomoferretti.mobilecomputing2022.treest.android.core.Status.SUCCESS) {
                        // mPosts.postValue(result.getData());
                        mAddPostEvent.postValue(new Event<>(Boolean.TRUE));
                        loadPosts(mDirectionId.getValue(), true);
                    } else {
                        mAddPostEvent.postValue(new Event<>(Boolean.FALSE));
                    }
                });
    }

    public void followUser(String authorId, boolean shouldFollow) {
        mAppDataRepository.follow(authorId, shouldFollow, result -> {
            if (result.getStatus() == com.giacomoferretti.mobilecomputing2022.treest.android.core.Status.SUCCESS) {
                if (mPosts.getValue() != null) {
                    List<Post> posts = mPosts.getValue();

                    for (int i = 0; i < posts.size(); i++) {
                        if (posts.get(i).getAuthorId().equals(authorId)) {
                            posts.get(i).setFollowingAuthor(shouldFollow);
                        }
                    }

                    mPosts.postValue(mPosts.getValue());
                }
            }
        });
    }

    /*private final MutableLiveData<Event<String>> mOpenBoardEvent = new MutableLiveData<>();

    public LiveData<Event<String>> getOpenBoardEvent() {
        return mOpenBoardEvent;
    }

    public void openBoard(String directionId) {
        mOpenBoardEvent.setValue(new Event<>(directionId));
    }*/

    /*private MutableLiveData<String> sessionId;

    public LiveData<String> getSessionId() {
        if (sessionId == null) {
            sessionId = new MutableLiveData<>();
            loadUsers();
        }

        return sessionId;
    }

    private void loadUsers() {
        TreEstRemote.getInstance().getService().register().enqueue(new Callback<SidRequest>() {
            @Override
            public void onResponse(@NonNull Call<SidRequest> call, @NonNull Response<SidRequest>
            response) {
                if (response.isSuccessful() && response.body() != null) {
                    sessionId.setValue(response.body().getSid());
                }
            }

            @Override
            public void onFailure(@NonNull Call<SidRequest> call, @NonNull Throwable t) {

            }
        });
    }*/
}