package com.example.heatcontroler.utils;

import android.util.Log;

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
                URL url = null;
                String urlStr = "http://api.heclouds.com/devices/"+deviceId+"/datapoints?limit="+count;
                HttpURLConnection conn = null;
                InputStream is = null;
                ByteArrayOutputStream baos = null;
                try{
                    url = new URL(urlStr);
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(6000);
                    conn.setConnectTimeout(6000);
                    conn.setRequestMethod("GET");
                    conn.setRequestProperty("api-key",apiKey);
                    conn.connect();
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

    /**
     * @param Json {temperature: 25}
     * */
    public void setTemperature(final String apiKey, final String deviceId, final String Json){
        new Thread(new Runnable() {
            @Override
            public void run() {
                PrintWriter out = null;
                BufferedReader in = null;
                String result = "";
                try
                {
                    String urlStr = "http://api.heclouds.com/devices/"+deviceId+"/datapoints?type=3";
                    URL realUrl = new URL(urlStr);
                    // 打开和URL之间的连接
                    HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
                    // 设置通用的请求属性
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("api-key",apiKey);
                    conn.setUseCaches(false);
                    // 发送POST请求必须设置如下两行
                    conn.setDoOutput(true);
                    conn.setDoInput(true);
                    conn.setReadTimeout(6000);
                    conn.setConnectTimeout(6000);
                    if (Json != null && !Json.trim().equals(""))
                    {
                        byte[] writebytes = Json.getBytes();
                        // 设置文件长度
                        conn.setRequestProperty("Content-Length", String.valueOf(writebytes.length));
                        OutputStream outwritestream = conn.getOutputStream();
                        outwritestream.write(Json.getBytes());
                        outwritestream.flush();
                        outwritestream.close();
                    }
                    // 定义BufferedReader输入流来读取URL的响应
                    in = new BufferedReader(
                            new InputStreamReader(conn.getInputStream()));
                    String line;
                    while ((line = in.readLine()) != null) {
                        result += line;
                    }
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
                // 使用finally块来关闭输出流、输入流
                finally
                {
                    try
                    {
                        if (out != null) {
                            out.close();
                        }
                        if (in != null) {
                            in.close();
                        }
                    } catch (IOException ex)
                    {
                        ex.printStackTrace();
                    }
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
