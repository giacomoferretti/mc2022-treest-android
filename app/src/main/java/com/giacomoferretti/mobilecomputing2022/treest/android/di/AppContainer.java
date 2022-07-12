package com.giacomoferretti.mobilecomputing2022.treest.android.di;

import android.content.Context;

import com.giacomoferretti.mobilecomputing2022.treest.android.core.AppExecutors;

public class AppContainer {
    private static volatile AppContainer sInstance;

    private final Context mAppContext;
    private final AppExecutors mAppExecutors;

    private AppContainer(Context appContext, AppExecutors appExecutors) {
        this.mAppContext = appContext;
        this.mAppExecutors = appExecutors;
    }

    public static AppContainer getInstance(final Context context) {
        if (sInstance == null) {
            synchronized (AppContainer.class) {
                if (sInstance == null) {
                    sInstance = new AppContainer(
                            context.getApplicationContext(),
                            new AppExecutors()
                    );
                }
            }
        }
        return sInstance;
    }

    public Context getAppContext() {
        return mAppContext;
    }

    public AppExecutors getAppExecutors() {
        return mAppExecutors;
    }
}
