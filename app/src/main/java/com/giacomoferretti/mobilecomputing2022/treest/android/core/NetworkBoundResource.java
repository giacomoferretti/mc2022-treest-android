package com.giacomoferretti.mobilecomputing2022.treest.android.core;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import retrofit2.Response;

public abstract class NetworkBoundResource<ResultType, RequestType> {
    private final MediatorLiveData<Result<ResultType>> result = new MediatorLiveData<>();
    private final AppExecutors appExecutors;

    public NetworkBoundResource(@NotNull AppExecutors appExecutors) {
        this.appExecutors = appExecutors;

        result.setValue(Result.loading(null));

        LiveData<ResultType> dbSource = loadFromDb();
        result.addSource(dbSource, data -> {
            result.removeSource(dbSource);

            if (shouldFetch(data)) {
                fetchFromNetwork(dbSource);
            } else {
                result.addSource(dbSource, newData -> {
                    setValue(Result.success(newData));
                });
            }
        });
    }

    private void setValue(Result<ResultType> newValue) {
        if (result.getValue() != newValue) {
            result.setValue(newValue);
        }
    }

    private void fetchFromNetwork(LiveData<ResultType> dbSource) {

    }

    protected abstract LiveData<ResultType> loadFromDb();

    protected abstract boolean shouldFetch(@Nullable ResultType data);

    protected abstract LiveData<Response<RequestType>> createCall();
}
