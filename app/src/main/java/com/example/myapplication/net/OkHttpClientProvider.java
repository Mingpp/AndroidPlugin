package com.example.myapplication.net;

import com.example.myapplication.intercept.LoggingInterceptor;

import okhttp3.OkHttpClient;

public class OkHttpClientProvider {
    public static OkHttpClient getClient() {
        return new OkHttpClient.Builder()
                .addInterceptor(new LoggingInterceptor())
                .build();
    }
}
