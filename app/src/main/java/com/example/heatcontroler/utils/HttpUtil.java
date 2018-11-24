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
                            .data("0", tempVal)
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
                String url = "http://api.heclouds.com/cmds?device_id="+deviceId;
                try {
                    Document doc = Jsoup.connect(url)
                            .ignoreContentType(true)
                            .header("api-key", apiKey)
                            .data("n","1531")   // 没有意义，随便设的
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
}
