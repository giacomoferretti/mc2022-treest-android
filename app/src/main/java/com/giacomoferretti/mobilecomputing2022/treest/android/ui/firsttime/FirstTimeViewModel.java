package com.giacomoferretti.mobilecomputing2022.treest.android.ui.firsttime;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.giacomoferretti.mobilecomputing2022.treest.android.data.source.AppDataRepository;
import com.giacomoferretti.mobilecomputing2022.treest.android.core.Event;
import com.giacomoferretti.mobilecomputing2022.treest.android.core.Status;

public class FirstTimeViewModel extends AndroidViewModel {

    private final AppDataRepository mAppDataRepository;

    private final MutableLiveData<String> mName = new MutableLiveData<>();
    private final LiveData<Boolean> mIsEnabled = Transformations.map(mName,
            input -> input.length() != 0 && input.length() <= 20);
    private final MutableLiveData<Event<Boolean>> mSubmitCommand = new MutableLiveData<>();

    public FirstTimeViewModel(@NonNull Application application) {
        super(application);
        mAppDataRepository = AppDataRepository.getInstance(application);
    }

    public LiveData<Boolean> getIsEnabled() {
        return mIsEnabled;
    }

    public LiveData<Event<Boolean>> getSubmitCommand() {
        return mSubmitCommand;
    }

    public void setName(String input) {
        mName.setValue(input);
    }

    public void setProfile() {
        mAppDataRepository.setProfile(
                mName.getValue(),
                null,
                result -> {
                    if (result.getStatus() == Status.SUCCESS) {
                        mAppDataRepository.setShouldSkipFirstTime(true);
                        mSubmitCommand.postValue(new Event<>(Boolean.TRUE));
                    } else {
                        mSubmitCommand.postValue(new Event<>(Boolean.FALSE));
                    }
                }
        );
    }
}
