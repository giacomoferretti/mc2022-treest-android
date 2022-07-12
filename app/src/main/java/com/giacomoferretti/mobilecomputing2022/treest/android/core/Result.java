package com.giacomoferretti.mobilecomputing2022.treest.android.core;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class Result<T> {
    @NonNull
    private final Status status;

    @Nullable
    private final T data;

    @Nullable
    private final String message;

    public Result(@NonNull Status status, @Nullable T data, @Nullable String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }

    public static <T> Result<T> success(@Nullable T data) {
        return new Result<>(Status.SUCCESS, data, null);
    }

    public static <T> Result<T> error(@NonNull String msg, @Nullable T data) {
        return new Result<>(Status.ERROR, data, msg);
    }

    public static <T> Result<T> loading(@Nullable T data) {
        return new Result<>(Status.LOADING, data, null);
    }

    @NonNull
    public Status getStatus() {
        return status;
    }

    @Nullable
    public T getData() {
        return data;
    }

    @Nullable
    public String getMessage() {
        return message;
    }
}
