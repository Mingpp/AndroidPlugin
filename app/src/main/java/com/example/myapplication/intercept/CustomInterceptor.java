package com.example.myapplication.intercept;

import okhttp3.Interceptor;

import okhttp3.Request;
import okhttp3.Response;
import java.io.IOException;
public class CustomInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        Request modifiedRequest = originalRequest.newBuilder()
                .header("Authorization", "Bearer your_token")
                .build();
        System.out.println();
        return chain.proceed(modifiedRequest);
    }
}
