package com.example.heatcontroler.utils;

import android.app.Application;
import android.text.Editable;

/**
 * Created by 艾德米 on 2018/6/3.
 */

public class AppContext extends Application {
    private static String APIKEY;
    private static String DEVICEID;
    private static String CMDMSG;
    private static String CurrTemp; //当前温度

    public void onCreate() {
        super.onCreate();
    }

    public static String getAPIKEY() {
        return APIKEY;
    }

    public void setAPIKEY(String APIKEY) {
        this.APIKEY = APIKEY;
    }

    public static String getDEVICEID() {
        return DEVICEID;
    }

    public void setDEVICEID(String DEVICEID) {
        this.DEVICEID = DEVICEID;
    }

    public static String getCMDMSG() {
        return CMDMSG;
    }

    public void setCMDMSG(String CMDMSG) {
        this.CMDMSG = CMDMSG;
    }

    public static String getCurrTemp() {
        return CurrTemp;
    }

    public void setCurrTemp(String currTemp) {
        CurrTemp = currTemp;
    }
}
