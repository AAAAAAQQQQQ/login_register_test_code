package com.example.myapplication;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
public class LoginActivity extends AppCompatActivity {
    //声明控件
    //登陆界面的控件
    EditText user_name;
    EditText user_password;

    Button denglu;
    Button zhuce;

    HashMap<String, String> stringHashMap;
    String TAG = MainActivity.class.getCanonicalName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //找到当且xml文件内的控件ID
        //数据编辑框的ID
        user_name = this.findViewById(R.id.user_name);
        user_password = this.findViewById(R.id.user_password);
        //按键属性的ID
        denglu = this.findViewById(R.id.denglu);
        zhuce = this.findViewById(R.id.zhuce);


        stringHashMap = new HashMap<>();
        //登录按键按下之后处理的事情
        denglu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //提交到服务器验证
                //获取用户名,密码,并放入HashMap中,准备传入服务器,进行验证
                loginPOST(view);
            }
        });
        //注册按键按下之后，响应的事件
        zhuce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //实现界面跳转，从登录界面跳转到注册界面
                Intent intent = null;  //这个变量初始申明为空
                intent = new Intent(LoginActivity.this, RegisterActivity.class);//跳转界面
                startActivity(intent);
            }
        });
    }
    public void loginPOST(View view){//post请求
        stringHashMap.put("username",user_name.getText().toString());//获取用户名,密码,并放入HashMap中,准备传入服务器,进行验证
        stringHashMap.put("password",user_password.getText().toString());
        new Thread(postRun).start();//开启线程
    }
    /**
     * post请求
     */
    Runnable postRun = new Runnable() {
        @Override
        public void run() {
            requestPost(stringHashMap);
        }
    };
    /**
     * post提交数据
     * @param paramsMap
     */
    private void requestPost(HashMap<String,String>paramsMap){
        try{
            String baseUrl = "http://localhost:8080/AndroidTest_war_exploded/LoginServlet";
            //合成参数
            StringBuilder tempParams = new StringBuilder();
            int pos = 0;
            for(String key :paramsMap.keySet()){
                if(pos>0){
                    tempParams.append("&");
                }
                tempParams.append(String.format("%s=%s",key, URLEncoder.encode(paramsMap.get(key),"utf-8")));
                pos++;
            }
            String params = tempParams.toString();
            Log.e(TAG,"请求的参数为："+params);
            //请求的参数转换为byte数组
            byte[] postData = params.getBytes();
            //新建一个URL对象
            URL url = new URL(baseUrl);
            //打开一个HttpURLConnection连接
            HttpURLConnection urlConn = (HttpURLConnection)url.openConnection();
            //设置连接超时时间
            urlConn.setConnectTimeout(5*1000);
            //设置从主机读取数据超时
            urlConn.setReadTimeout(5*1000);
            //Post请求必须设置允许输出
            urlConn.setDoOutput(true);
            //Post请求不能使用缓存
            urlConn.setUseCaches(false);
            //设置请求允许输入
            urlConn.setDoInput(true);
            //设置为Post请求
            urlConn.setRequestMethod("POST");
            //设置本次连接是否自动处理重定向
            urlConn.setInstanceFollowRedirects(true);
            //配置请求Content-Type
            urlConn.setRequestProperty("Content-Type","application/json");
            //开始连接
            urlConn.connect();
            //发送请求参数
            PrintWriter dos = new PrintWriter(urlConn.getOutputStream());
            dos.write(params);
            dos.flush();
            dos.close();
            //判断请求是否成功
            if(urlConn.getResponseCode()==200){
                //获取返回的数据
                String result = streamToString(urlConn.getInputStream());
                Log.e(TAG,"Post方式请求成功，result--->"+result);
            }else {
                Log.e(TAG,"Post方式请求失败");
            }
            //关闭连接
            urlConn.disconnect();
        }catch (Exception e){
            Log.e(TAG,e.toString());
        }
    }
    /**
     * 将输入流转换成字符串,返回字符串信息
     * @param is
     * @return
     */
    private String streamToString(InputStream is) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = is.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            baos.close();
            is.close();
            byte[] byteArray = baos.toByteArray();
            return new String(byteArray);
        } catch (IOException e) {
            Log.e(TAG, e.toString());
            return null;
        }
    }
}