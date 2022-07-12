package com.giacomoferretti.mobilecomputing2022.treest.android.core;

import android.app.Application;
import android.util.Log;

import com.giacomoferretti.mobilecomputing2022.treest.android.di.AppContainer;
import com.giacomoferretti.mobilecomputing2022.treest.android.data.source.AppDataRepository;

public class TreEstApp extends Application {
    private static final String TAG = TreEstApp.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();

        AppContainer appContainer = AppContainer.getInstance(this);
        AppDataRepository repository = AppDataRepository.getInstance(this);

        // Initialize session id
        /*if (repository.getSessionId() == null) {
            Log.d(TAG, "[init] Session ID is null, getting a new one...");
            repository.register(result -> {
                if (result.getStatus() == Status.SUCCESS) {
                    Log.d(TAG, "[init] Saving session ID: " + result.getData());
                    repository.setSessionId(result.getData());
                } else {
                    // TODO: Show dialog when error
                }
            });
        }*/

        /*repository.getUserPicture("142019","1", result -> {
            Log.d(TAG, "[result] " + result.getMessage());
            Log.d(TAG, "[result] " + result.getStatus());
            Log.d(TAG, "[result] " + result.getData());
        });

        repository.getLines(result -> {
            Log.d(TAG, "[result] " + result.getMessage());
            Log.d(TAG, "[result] " + result.getStatus());
            Log.d(TAG, "[result] " + result.getData());
        });*/
    }
}