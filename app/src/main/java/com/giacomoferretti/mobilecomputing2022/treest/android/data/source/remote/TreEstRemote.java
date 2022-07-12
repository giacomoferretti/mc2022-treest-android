package com.giacomoferretti.mobilecomputing2022.treest.android.data.source.remote;

import com.giacomoferretti.mobilecomputing2022.treest.android.BuildConfig;
import com.giacomoferretti.mobilecomputing2022.treest.android.data.source.local.db.AppDatabase;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TreEstRemote {
    private static volatile TreEstRemote sInstance = null;

    private final TreEstService service;

    private TreEstRemote() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .readTimeout(1, TimeUnit.MINUTES)
                .callTimeout(1, TimeUnit.MINUTES)
                .writeTimeout(1, TimeUnit.MINUTES)
                .protocols(Arrays.asList(Protocol.HTTP_1_1, Protocol.HTTP_2))
                .build();

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS")
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();

        service = retrofit.create(TreEstService.class);
    }

    public static TreEstRemote getInstance() {
        if (sInstance == null) {
            synchronized (AppDatabase.class) {
                if (sInstance == null) {
                    sInstance = new TreEstRemote();
                }
            }
        }
        return sInstance;
    }

    public TreEstService getService() {
        return service;
    }
}
