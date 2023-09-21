package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button denglu;
    Button zhuce;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //按键属性的ID
        denglu = this.findViewById(R.id.denglu);
        zhuce = this.findViewById(R.id.zhuce);
        //注册按键按下之后，响应的事件
        zhuce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //实现界面跳转，从登录界面跳转到注册界面
                Intent intent = null;  //这个变量初始申明为空
                intent = new Intent(MainActivity.this, RegisterActivity.class);//跳转界面
                startActivity(intent);
            }
        });
        //登录按键按下之后，响应的事件
        denglu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //实现界面跳转，从登录界面跳转到注册界面
                Intent intent = null;  //这个变量初始申明为空
                intent = new Intent(MainActivity.this, LoginActivity.class);//跳转界面
                startActivity(intent);
            }
        });
    }
}