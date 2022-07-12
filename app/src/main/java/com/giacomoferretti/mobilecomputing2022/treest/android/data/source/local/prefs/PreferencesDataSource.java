package com.giacomoferretti.mobilecomputing2022.treest.android.data.source.local.prefs;

import com.giacomoferretti.mobilecomputing2022.treest.android.data.model.Line;

public interface PreferencesDataSource {
    String getSessionId();
    void setSessionId(String sessionId);

    boolean skipFirstTime();
    void setSkipFirstTime(boolean firstTime);

    String getCurrentDirectionId();
    void setCurrentDirectionId(String directionId);

    Line getCurrentLine();
    void setCurrentLine(Line line);
}
