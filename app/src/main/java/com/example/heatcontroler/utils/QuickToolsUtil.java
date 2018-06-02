package com.example.heatcontroler.utils;

import com.example.heatcontroler.Model.CmdJsonBean;
import com.example.heatcontroler.Model.TempJsonBean;
import com.google.gson.Gson;

/**
 * Created by 艾德米 on 2018/6/2.
 */

public class QuickToolsUtil {
    public static String getTemperatureValue(String json){
        String value = null;
        TempJsonBean tempJsonBean = new Gson().fromJson(json,TempJsonBean.class);
        value = tempJsonBean.data.datastreams.get(1).datapoints.get(0).value;
        return value;
    }

    public static String getCmdValue(String json){
        String value = null;
        CmdJsonBean cmdJsonBean = new Gson().fromJson(json,CmdJsonBean.class);
        value = cmdJsonBean.error;
        return value;
    }
}
