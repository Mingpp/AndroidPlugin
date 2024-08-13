package com.example.myapplication.net;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OkHttpAndIntercept {


    // 创建 OkHttpClient 实例
    private static OkHttpClient client = OkHttpClientProvider.getClient();

    /**
     * 执行同步请求并返回响应体的字符串
     *
     * @param url 请求的 URL
     * @return 响应体的字符串，如果请求失败或发生错误，则返回 null
     */
    public static String getResponseString(String url) {
        // 构建请求对象
        Request request = new Request.Builder()
                .url(url)
                .build();

        try {
            // 使用 execute() 方法进行同步请求
            Response response = client.newCall(request).execute();

            if (response.isSuccessful()) {
                // 返回响应体的字符串
                return response.body() != null ? response.body().string() : null;
            } else {
                // 请求失败，返回 null 或处理错误
                System.err.println("Request failed with code: " + response.code());
                return null;
            }
        } catch (IOException e) {
            // 捕获异常并处理
            e.printStackTrace();
            return null;
        }

    }


}
