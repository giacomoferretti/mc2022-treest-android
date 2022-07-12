package com.giacomoferretti.mobilecomputing2022.treest.android.data.source.local.db;

import android.content.Context;
import android.content.SharedPreferences;

import com.giacomoferretti.mobilecomputing2022.treest.android.core.AppExecutors;
import com.giacomoferretti.mobilecomputing2022.treest.android.core.Result;
import com.giacomoferretti.mobilecomputing2022.treest.android.core.TaskCallback;
import com.giacomoferretti.mobilecomputing2022.treest.android.data.model.UserPicture;
import com.giacomoferretti.mobilecomputing2022.treest.android.data.source.local.prefs.LocalPreferencesDataSource;
import com.giacomoferretti.mobilecomputing2022.treest.android.di.AppContainer;
import com.google.gson.Gson;

public class LocalDatabaseDataSource implements DatabaseDataSource {
    private static volatile LocalDatabaseDataSource sInstance;
    private final AppDatabase mAppDatabase;
    private final AppExecutors mAppExecutors;

    private LocalDatabaseDataSource(final Context context) {
        this.mAppDatabase = AppDatabase.getInstance(context);
        this.mAppExecutors = AppContainer.getInstance(context).getAppExecutors();
    }

    public static LocalDatabaseDataSource getInstance(final Context context) {
        if (sInstance == null) {
            synchronized (AppDatabase.class) {
                if (sInstance == null) {
                    sInstance = new LocalDatabaseDataSource(context.getApplicationContext());
                }
            }
        }
        return sInstance;
    }

    @Override
    public void getUserPictureByUserIdAndVersion(String userId, String version, TaskCallback<UserPicture> callback) {
        mAppExecutors.diskIO().execute(() -> {
            UserPicture userPicture = mAppDatabase.userPictureDao().findByUidAndVersion(userId, version);

            if (userPicture != null) {
                callback.onFinish(Result.success(userPicture));
            } else {
                callback.onFinish(Result.error("No picture found on DB", null));
            }
        });
    }

    @Override
    public void savePicture(UserPicture data) {
        mAppExecutors.diskIO().execute(() -> mAppDatabase.userPictureDao().insert(data));
    }
}
