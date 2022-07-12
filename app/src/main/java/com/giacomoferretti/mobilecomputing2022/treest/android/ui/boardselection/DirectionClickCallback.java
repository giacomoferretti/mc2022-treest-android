package com.giacomoferretti.mobilecomputing2022.treest.android.ui.boardselection;

import com.giacomoferretti.mobilecomputing2022.treest.android.data.model.Line;

public interface DirectionClickCallback {
    void onDirectionSelected(Line line, int did);
}