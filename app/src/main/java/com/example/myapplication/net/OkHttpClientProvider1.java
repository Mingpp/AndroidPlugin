package com.example.myapplication.net;

import com.example.myapplication.intercept.LoggingInterceptor;


import java.io.IOException;

import okhttp3.OkHttpClient;

public class OkHttpClientProvider1 {
    public static OkHttpClient getClient() {
            class HeadInterceptor implements okhttp3.Interceptor {
            public okhttp3.Response intercept(Chain chain) throws IOException {
                okhttp3.Request original = chain.request();

                // 添加自定义头信息
                okhttp3.Request.Builder requestBuilder = original.newBuilder()
                        .header("X-b3-traceId", "7ac0c001xxxxxxxx");

                okhttp3.Request request = requestBuilder.build();

                System.out.println("add headers: " + request.headers());

                return chain.proceed(request);
            }
        }
        return new OkHttpClient.Builder()
                .addInterceptor(new LoggingInterceptor())
                .addInterceptor(new HeadInterceptor())
                .build();
    }
}
