package com.dynasty.getverifycode;

import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Dynasty_Qin on 2017/4/27.
 */

public class HttpRequester {
    public static String uploadFile(String actionUrl) {
        Map map = new HashMap();
        map.put("jsondata","dsfdasfdasf");
        byte[] data = getRequestData(map, "utf-8").toString().getBytes();//获得请求体
        try {

            //String urlPath = "http://192.168.1.9:80/JJKSms/RecSms.php";
            URL url = new URL(actionUrl);

            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setConnectTimeout(3000);              //设置连接超时时间
            httpURLConnection.setDoInput(true);                     //打开输入流，以便从服务器获取数据
            httpURLConnection.setDoOutput(true);                    //打开输出流，以便向服务器提交数据
            httpURLConnection.setRequestMethod("POST");             //设置以Post方式提交数据
            httpURLConnection.setUseCaches(false);                  //使用Post方式不能使用缓存
            //设置请求体的类型是文本类型
            httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            //设置请求体的长度
            httpURLConnection.setRequestProperty("Content-Length", String.valueOf(data.length));
            //获得输出流，向服务器写入数据
            OutputStream outputStream = httpURLConnection.getOutputStream();
            outputStream.write(data);

            int response = httpURLConnection.getResponseCode();            //获得服务器的响应码
            if(response == HttpURLConnection.HTTP_OK) {
                Log.e("111111","上传成功");
                InputStream inputStream = httpURLConnection.getInputStream();
                return dealResponseResult(inputStream); //处理服务器的响应结果
            }else {
                Log.e("111111","上传失败");
            }
        } catch (IOException e) {
            //e.printStackTrace();
            return "err: " + e.getMessage().toString();
        }
        return "-1";
    }

    /*
 * Function  :   封装请求体信息
 * Param     :   params请求体内容，encode编码格式
 */
    public static StringBuffer getRequestData(Map<String, String> params, String encode) {
        StringBuffer stringBuffer = new StringBuffer();        //存储封装好的请求体信息
        try {
            for(Map.Entry<String, String> entry : params.entrySet()) {
                stringBuffer.append(entry.getKey())
                        .append("=")
                        .append(URLEncoder.encode(entry.getValue(), encode))
                        .append("&");
            }
            stringBuffer.deleteCharAt(stringBuffer.length() - 1);    //删除最后的一个"&"
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringBuffer;
    }

    /*
  * Function  :   处理服务器的响应结果（将输入流转化成字符串）
  * Param     :   inputStream服务器的响应输入流
  */
    public static String dealResponseResult(InputStream inputStream) {
        String resultData = null;      //存储处理结果
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] data = new byte[1024];
        int len = 0;
        try {
            while((len = inputStream.read(data)) != -1) {
                byteArrayOutputStream.write(data, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        resultData = new String(byteArrayOutputStream.toByteArray());
        Log.e("1111",resultData);
        return resultData;
    }
}