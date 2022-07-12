package com.giacomoferretti.mobilecomputing2022.treest.android.core;

public enum Status {
    SUCCESS,
    ERROR,
    LOADING;

    public final boolean isLoading() {
        return this == LOADING;
    }
}
