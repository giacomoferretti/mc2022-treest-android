package com.giacomoferretti.mobilecomputing2022.treest.android.ui.mapdetails;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.giacomoferretti.mobilecomputing2022.treest.android.core.Event;
import com.giacomoferretti.mobilecomputing2022.treest.android.core.Status;
import com.giacomoferretti.mobilecomputing2022.treest.android.data.model.Post;
import com.giacomoferretti.mobilecomputing2022.treest.android.data.model.Station;
import com.giacomoferretti.mobilecomputing2022.treest.android.data.source.AppDataRepository;
import com.giacomoferretti.mobilecomputing2022.treest.android.di.AppContainer;

import java.util.List;

public class MapDetailsViewModel extends AndroidViewModel {
    private final AppDataRepository mAppDataRepository;

    private final MutableLiveData<String> mDirectionId = new MutableLiveData<>();
    private final MutableLiveData<List<Station>> mStations = new MutableLiveData<>();

    public MapDetailsViewModel(@NonNull Application application) {
        super(application);
        mAppDataRepository = AppDataRepository.getInstance(application);
    }

    public void setDirectionId(String directionId) {
        Log.d("setDirectionId", directionId);
        mDirectionId.setValue(directionId);
        loadStations(directionId, true);
    }

    public LiveData<List<Station>> getStations() {
        return mStations;
    }

    public void loadStations(String directionId, boolean forceUpdate) {
        if (mStations.getValue() == null || forceUpdate) {
            mAppDataRepository.getStations(directionId, result -> {
                if (result.getStatus() == com.giacomoferretti.mobilecomputing2022.treest.android.core.Status.SUCCESS) {
                    mStations.postValue(result.getData());
                }
            });
        }
    }
}
