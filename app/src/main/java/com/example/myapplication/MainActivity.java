package com.example.myapplication;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.net.OkhttpUtils;

import com.example.myapplication.net.OkHttpAndIntercept;

public class MainActivity extends AppCompatActivity {

    private Button button;
    private TextView textView;

    private Handler handler = new Handler(Looper.getMainLooper());


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        initView();
        initEvent();


//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
    }


    private void initView() {
        button = findViewById(R.id.button2);
        textView = findViewById(R.id.textView);
    }


    private void initEvent() {

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(){
                   public void run(){
                       try {
//                         String s =  OkhttpUtils.getInstance().doGet("https://www.baidu.com").substring(0,5);
                           String s =  OkHttpAndIntercept.getResponseString("https://www.baidu.com");
                           if(s != null && s.length() > 5){
                               String ss = s.substring(0,5);
                           }
                         handler.post(new Runnable() {
                             @Override
                             public void run() {
                                 textView.setText(s);
                             }
                         });


                       } catch (Exception ex) {
                           try {
                               textView.setText(ex.getMessage());
                           }catch (Exception e){
                               System.out.println(e);
                           }
//                           throw new RuntimeException(ex);
                       }
                   }
                }.start();


            }
        });
    }







}