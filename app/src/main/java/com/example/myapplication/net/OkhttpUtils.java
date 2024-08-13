package com.example.myapplication.net;

import okhttp3.Call;
import okhttp3.OkHttpClient;

import okhttp3.Request;
import okhttp3.Response;

public class OkhttpUtils {


    private OkhttpUtils() {
    }


    private  OkHttpClient client = new OkHttpClient();

    private static OkhttpUtils instance = new OkhttpUtils();

    public static OkhttpUtils getInstance() {
        return instance;
    }


    public   String doGet(String url) throws Exception {

//        Request request = new Request.Builder().url(url).build();
        Request request = new Request.Builder().url(url).build();
        Call call = client.newCall(request);

        Response response=  call.execute();

        return response.body().string();

    }
}
