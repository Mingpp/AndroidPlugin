package com.example.myapplication.intercept;

import java.io.IOException;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 拦截器，添加日志
 */
public class LoggingInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();

        // 修改请求，例如添加自定义头部
        Request modifiedRequest = originalRequest.newBuilder()
                .header("mine", "mineValue")
                .build();



        // Log request details
//        System.out.println("Sending request to " + modifiedRequest.url());
//        System.out.println("Request headers: " + modifiedRequest.headers());
//        System.out.println("Request method: " + modifiedRequest.method());
        //打印请求头部
        Headers requestHeads = modifiedRequest.headers();
//        for (int i = 0; i < requestHeads.size(); i++) {
//            System.out.println(requestHeads.name(i) + ": " + requestHeads.value(i));
//        }

        // Proceed with the request
        Response response = chain.proceed(modifiedRequest);
        // 打印所有响应头部
//        Headers headers = response.headers();
//        for (int i = 0; i < headers.size(); i++) {
//            System.out.println(headers.name(i) + ": " + headers.value(i));
//        }
        if (response.code() == 302) {
            String location = response.header("Location");
            System.out.println("Redirect to: " + location);
        }

//        // Log response details
//        System.out.println("Received response for " + response.request().url());
//        System.out.println("Response code: " + response.code());
//        System.out.println("Response headers: " + response.headers());

        return response;
    }



}
