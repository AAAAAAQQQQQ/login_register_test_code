package com.example.myapplication;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ApiHelper {
    public void callApi() {
        String apiUrl="https://api.github.com/users/";
        String requestMethod="GET";
        String requestBody="";

        //构建请求
        HttpURLConnection connection = null;
        try{
            URL url = new URL(apiUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(requestMethod);

            //设置请求头
            connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            connection.setRequestProperty("Authorization", "Bearer " + "token");

            //设置请求体
            if(requestMethod.equals("POST")){
                connection.setDoOutput(true);
                OutputStream outputStream = connection.getOutputStream();
                outputStream.write(requestBody.getBytes());
                outputStream.flush();
                outputStream.close();
            }

            //发送请求
            int responseCode = connection.getResponseCode();
            if(responseCode == HttpURLConnection.HTTP_OK){
                InputStream inputStream = connection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                StringBuilder stringBuilder = new StringBuilder();
                while((line = bufferedReader.readLine()) != null){
                    stringBuilder.append(line);
                }
                bufferedReader.close();
                inputStream.close();
                String response = stringBuilder.toString();
                System.out.println(response);
                //解析响应
                JSONObject jsonObject = new JSONObject(response);
                String login = jsonObject.getString("login");
            }else {
                System.out.println("请求失败");
            }
        }catch (IOException e){
            e.printStackTrace();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        } finally {
            if(connection != null){
                connection.disconnect();
            }
        }
    }
}
