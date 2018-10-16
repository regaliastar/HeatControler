package com.example.heatcontroler.utils;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;

/**
 * Created by 艾德米 on 2018/6/1.
 */

public class HttpUtil{
    private final String TAG = "网络请求";
    private final String masterKey = "L63i2WDONLZOth1sE=FNXckQG20=";
    private ICallBack callBack;

    public HttpUtil(){

    }

    public void setGetTemperatureCallBack(ICallBack callBack) {
        this.callBack = callBack;
    }

    public void setSetTemperatureCallBack(ICallBack callBack){
        this.callBack = callBack;
    }

    public void setCmdCallBack(ICallBack callBack){
        this.callBack = callBack;
    }

    /**
     * 得到温度数据
     * @param apiKey masterKey
     * @param deviceId 设备号，在onenet上查看
     * @param count 查询温度的数量
     * */
    public void getTemperature(final String apiKey, final String deviceId, final int count){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = "http://api.heclouds.com/devices/" + deviceId + "/datapoints?limit=" + count;
                try {
                    Document doc = Jsoup.connect(url)
                            .header("api-key", apiKey)
                            .ignoreContentType(true)
                            .get();
                    String docs = doc.getElementsByTag("body").html();
                    Log.d(TAG, "docs: "+docs);
                    callBack.postExec(docs);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * @param tempVal {String}
     * */
    public void setTemperature(final String apiKey, final String deviceId, final String tempVal){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = "http://api.heclouds.com/cmds?device_id="+deviceId;
                try {
                    Document doc = Jsoup.connect(url)
                            .ignoreContentType(true)
                            .header("api-key", apiKey)
                            .data("0",tempVal)
                            .post();
                    String docs = doc.getElementsByTag("body").html();
                    Log.d(TAG, "docs: "+docs);
                    callBack.postExec(docs);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    public void getCmd(final String apiKey, final String deviceId){
        new Thread(new Runnable() {
            @Override
            public void run() {
                URL url = null;
                String urlStr = "http://api.heclouds.com/cmds?device_id="+deviceId;
                HttpURLConnection conn = null;
                InputStream is = null;
                ByteArrayOutputStream baos = null;
                String Json = "{}";
                try{
                    url = new URL(urlStr);
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(6000);
                    conn.setConnectTimeout(6000);
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("api-key",apiKey);
                    OutputStream outwritestream = conn.getOutputStream();
                    outwritestream.write(Json.getBytes());
                    outwritestream.flush();
                    outwritestream.close();

                    if (conn.getResponseCode() == 200) {
                        is = conn.getInputStream();
                        baos = new ByteArrayOutputStream();
                        int len = -1;
                        byte[] buf = new byte[1024];

                        while ((len = is.read(buf)) != -1) {
                            baos.write(buf, 0, len);
                            baos.flush();
                        }
                        callBack.postExec(baos.toString());
                    }else {
                        throw new RuntimeException(" responseCode is not 200 ... ");
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }finally{
                    try {
                        if (is != null)
                            is.close();
                    } catch (IOException e) {
                    }
                    try{
                        if (baos != null)
                            baos.close();
                    }catch (SocketTimeoutException e){
                        Log.i(TAG, "doGet: "+"网络超时！！！");
                    }catch (IOException e) {
                    }
                    conn.disconnect();
                }
            }
        }).start();
    }
}
