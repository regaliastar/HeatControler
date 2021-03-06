package com.example.heatcontroler.utils;

import com.example.heatcontroler.Model.CmdJsonBean;
import com.example.heatcontroler.Model.TempJsonBean;
import com.google.gson.Gson;

import java.util.List;

/**
 * Created by 艾德米 on 2018/6/2.
 * 用来保存各种方便常用的函数
 */

public class QuickToolsUtil {
    public static String getTemperatureValue(String json){
        String value = null;
        TempJsonBean tempJsonBean = new Gson().fromJson(json,TempJsonBean.class);
        value = tempJsonBean.data.datastreams.get(2).datapoints.get(0).value;   //datastreams.get(0) 表示exceptedTemperature
        return value;
    }

    public static String[] getTemperatureValueArray(String json, int count){
        String[] value = new String[count];
        List datapoints = null;
        TempJsonBean tempJsonBean = new Gson().fromJson(json,TempJsonBean.class);
        datapoints = tempJsonBean.data.datastreams.get(0).datapoints;
        for(int i = 0; i < datapoints.size(); i++){
            value[i] = tempJsonBean.data.datastreams.get(2).datapoints.get(i).value;    //datastreams.get(0) 表示exceptedTemperature
        }
        return value;
    }

    public static String[] getTemperatureTimeArray(String json, int count){
        String[] value = new String[count];
        List datapoints = null;
        TempJsonBean tempJsonBean = new Gson().fromJson(json,TempJsonBean.class);
        datapoints = tempJsonBean.data.datastreams.get(2).datapoints;
        for(int i = 0; i < datapoints.size(); i++){
            String temp = null;
            temp = tempJsonBean.data.datastreams.get(2).datapoints.get(i).at;
            value[i] = temp.substring(temp.length() - 12,temp.length()-7);
        }
        return value;
    }

    public static String getCmdValue(String json){
        String value = null;
        CmdJsonBean cmdJsonBean = new Gson().fromJson(json,CmdJsonBean.class);
        value = cmdJsonBean.error;
        return value;
    }

    public static int getCmdNo(String json){
        int value;
        CmdJsonBean cmdJsonBean = new Gson().fromJson(json,CmdJsonBean.class);
        value = cmdJsonBean.errno;
        return value;
    }
}
