package com.giacomoferretti.mobilecomputing2022.treest.android.ui.boardselection;

import com.giacomoferretti.mobilecomputing2022.treest.android.data.model.Line;

public interface BoardSelectionNavigator {
    void onSelectBoard(Line selectedLine, String selectedDirectionId);
}
