package com.giacomoferretti.mobilecomputing2022.treest.android.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.giacomoferretti.mobilecomputing2022.treest.android.core.Result;
import com.giacomoferretti.mobilecomputing2022.treest.android.core.SafeApiCallCallback;

import org.jetbrains.annotations.NotNull;

public class NetworkUtils {
    public static <T> Result<T> safeApiCall(@NotNull SafeApiCallCallback<T> call, @NotNull String errorMessage) {
        try {
            return call.call();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(errorMessage, null);
        }
    }

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        }
        return false;
    }
}
