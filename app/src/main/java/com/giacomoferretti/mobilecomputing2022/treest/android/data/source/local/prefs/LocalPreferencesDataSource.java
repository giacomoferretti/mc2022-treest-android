package com.giacomoferretti.mobilecomputing2022.treest.android.data.source.local.prefs;

import android.content.Context;
import android.content.SharedPreferences;

import com.giacomoferretti.mobilecomputing2022.treest.android.data.model.Line;
import com.giacomoferretti.mobilecomputing2022.treest.android.data.source.local.db.AppDatabase;
import com.google.gson.Gson;

public class LocalPreferencesDataSource implements PreferencesDataSource {
    private static final String PREF_KEY_SESSION_ID = "SESSION_ID";
    private static final String PREF_KEY_SKIP_FIRST_TIME = "SKIP_FIRST_TIME";
    private static final String PREF_KEY_SELECTED_DID = "SELECTED_DID";
    private static final String PREF_KEY_SELECTED_LINE = "SELECTED_LINE";

    private static volatile LocalPreferencesDataSource sInstance;
    private final SharedPreferences mPrefs;
    private final Gson mGson;

    private LocalPreferencesDataSource(final Context context) {
        this.mPrefs = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        this.mGson = new Gson();
    }

    public static LocalPreferencesDataSource getInstance(final Context context) {
        if (sInstance == null) {
            synchronized (AppDatabase.class) {
                if (sInstance == null) {
                    sInstance = new LocalPreferencesDataSource(context.getApplicationContext());
                }
            }
        }
        return sInstance;
    }

    @Override
    public String getSessionId() {
        return mPrefs.getString(PREF_KEY_SESSION_ID, null);
    }

    @Override
    public void setSessionId(String sessionId) {
        mPrefs.edit().putString(PREF_KEY_SESSION_ID, sessionId).apply();
    }

    @Override
    public boolean skipFirstTime() {
        return mPrefs.getBoolean(PREF_KEY_SKIP_FIRST_TIME, false);
    }

    @Override
    public void setSkipFirstTime(boolean firstTime) {
        mPrefs.edit().putBoolean(PREF_KEY_SKIP_FIRST_TIME, firstTime).apply();
    }

    @Override
    public String getCurrentDirectionId() {
        return mPrefs.getString(PREF_KEY_SELECTED_DID, null);
    }

    @Override
    public void setCurrentDirectionId(String directionId) {
        mPrefs.edit().putString(PREF_KEY_SELECTED_DID, directionId).apply();
    }

    @Override
    public Line getCurrentLine() {
        String json = mPrefs.getString(PREF_KEY_SELECTED_LINE, "");
        if (json.length() == 0) {
            return null;
        }

        return mGson.fromJson(json, Line.class);
    }

    @Override
    public void setCurrentLine(Line line) {
        String strObject = mGson.toJson(line, Line.class);
        mPrefs.edit().putString(PREF_KEY_SELECTED_LINE, strObject).apply();
    }
}
