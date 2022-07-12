package com.giacomoferretti.mobilecomputing2022.treest.android.core;

public interface TaskCallback<T> {
    void onFinish(Result<T> result);
}