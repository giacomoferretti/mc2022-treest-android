package com.giacomoferretti.mobilecomputing2022.treest.android.ui.boardselection;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.giacomoferretti.mobilecomputing2022.treest.android.data.model.Line;
import com.giacomoferretti.mobilecomputing2022.treest.android.data.source.AppDataRepository;
import com.giacomoferretti.mobilecomputing2022.treest.android.core.Status;

import java.util.List;

public class BoardSelectionViewModel extends AndroidViewModel {
    private final AppDataRepository mAppDataRepository;
    private final MutableLiveData<List<Line>> mLines = new MutableLiveData<>();

    public BoardSelectionViewModel(@NonNull Application application) {
        super(application);
        mAppDataRepository = AppDataRepository.getInstance(application);
    }

    public LiveData<List<Line>> getLines() {
        return mLines;
    }

    public void loadLines() {
        loadLines(false);
    }

    public void loadLines(boolean forceUpdate) {
        if (mLines.getValue() == null || forceUpdate) {
            mAppDataRepository.getLines(result -> {
                if (result.getStatus() == Status.SUCCESS) {
                    mLines.postValue(result.getData());
                }
            });
        }
    }
}