package com.example.myapplication.intercept;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Test {
    public static void main(String[] args) {

        try {
            // 生成字节码
            byte[] classData = CustomInterceptorGenerator.generate();

            // 使用自定义的类加载器加载类
            MyClassLoader classLoader = new MyClassLoader();
            Class<?> customInterceptorClass = classLoader.defineClass("CustomInterceptor", classData);

            // 实例化类
            Object interceptor = customInterceptorClass.newInstance();

            // 如果需要将其转换为 Interceptor 接口类型
            Interceptor httpInterceptor = (Interceptor) interceptor;

            // 现在可以在 OkHttpClient 中使用这个自定义的拦截器
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(httpInterceptor)
                    .build();

            // 执行一个请求
            Request request = new Request.Builder()
                    .url("https://www.baidu.com")
                    .build();

            Response response = client.newCall(request).execute();
            System.out.println(response.body().string());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
