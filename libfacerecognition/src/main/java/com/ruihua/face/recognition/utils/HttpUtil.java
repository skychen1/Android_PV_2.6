package com.ruihua.face.recognition.utils;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * HTTP请求工具类
 */
public class HttpUtil {

    //GET请求
    public static String get(String url){
        InputStream inputStream = null;
        HttpURLConnection conn = null;
        String result = null;
        try {
            URL u = new URL(url);
            conn = (HttpURLConnection)u.openConnection();
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestMethod("GET");
            int statusCode = conn.getResponseCode();
            if(statusCode == 200){
                inputStream = new BufferedInputStream(conn.getInputStream());
                result =readStream(inputStream);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    //POST请求（Map参数）
    public static String post(String url, HashMap<String, String> params){
        InputStream inputStream = null;
        HttpURLConnection conn = null;
        String result = null;
        try {
            URL u = new URL(url);
            conn = (HttpURLConnection)u.openConnection();
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setConnectTimeout(3000);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            if(params != null && params.size() > 0){
                OutputStream os = conn.getOutputStream();
                JSONObject jo = new JSONObject(params);
                Log.i("JSON Str:", jo.toString());
                byte[] requestData = jo.toString().getBytes("utf-8");
                os.write(requestData);
            }
            int statusCode = conn.getResponseCode();
            Log.i("STATUS CODE:", "" + statusCode);
            if(statusCode == 200){
                inputStream = new BufferedInputStream(conn.getInputStream());
                result =readStream(inputStream);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    //POST请求（List参数，以JSONArray方式传入）
    public static String post(String url, JSONArray paramArray){
        InputStream inputStream = null;
        HttpURLConnection conn = null;
        String result = null;
        try {
            URL u = new URL(url);
            conn = (HttpURLConnection)u.openConnection();
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setConnectTimeout(3000);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            if(paramArray != null && paramArray.length() > 0){
                OutputStream os = conn.getOutputStream();
                byte[] requestData = paramArray.toString().getBytes("utf-8");
                os.write(requestData);
            }
            int statusCode = conn.getResponseCode();
            if(statusCode == 200){
                inputStream = new BufferedInputStream(conn.getInputStream());
                result =readStream(inputStream);
                result = URLDecoder.decode(result, "utf-8");
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    public static String readStream(InputStream is) throws Exception{
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = is.read(buffer))!=-1){
            baos.write(buffer, 0, len);
        }
        is.close();
        String content = new String(baos.toByteArray(), "utf-8");
        return content;
    }


    //x-www-form-urlencoded 方式传参拼接请求参数
    public static StringBuffer getRequestData(Map<String, String> params) {
        StringBuffer stringBuffer = new StringBuffer();        //存储封装好的请求体信息
        try {
            for(Map.Entry<String, String> entry : params.entrySet()) {
                stringBuffer.append(entry.getKey())
                        .append("=")
                        .append(URLEncoder.encode(entry.getValue(), "utf-8"))
                        .append("&");
            }
            stringBuffer.deleteCharAt(stringBuffer.length() - 1);    //删除最后的一个"&"
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringBuffer;
    }
}
