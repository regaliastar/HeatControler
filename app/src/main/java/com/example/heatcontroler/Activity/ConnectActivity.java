package com.example.heatcontroler.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.heatcontroler.R;
import com.example.heatcontroler.utils.HttpUtil;
import com.example.heatcontroler.utils.ICallBack;

public class ConnectActivity extends AppCompatActivity {
    EditText deviceId;
    EditText apiKey;
    Button connectBtn;
    Button getTempBtn;
    Button setTempBtn;
    final String TAG = "ConnectActivity";
    final String PID = "130185";
    final String APIKEY = "L63i2WDONLZOth1sE=FNXckQG20=";
    final String DEVICEID = "29568317";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);

        deviceId = (EditText) findViewById(R.id.deviceId);
        deviceId.setText(DEVICEID);
        apiKey = findViewById(R.id.apiKey);
        apiKey.setText(APIKEY);
        connectBtn = findViewById(R.id.connectBtn);
        getTempBtn = findViewById(R.id.getTemp);
        getTempBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HttpUtil httpUtil = new HttpUtil();
                int count = 1;
                httpUtil.setGetTemperatureCallBack(new ICallBack() {
                    @Override
                    public void postExec(String str) {
                        Log.d(TAG,str);
                    }
                });
                httpUtil.getTemperature(APIKEY,DEVICEID,count);

            }
        });
        setTempBtn = findViewById(R.id.setTemp);
        setTempBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String temp = "250";
                String json = "{'temperature':"+temp+"}";
                HttpUtil httpUtil = new HttpUtil();
                httpUtil.setTemperature(APIKEY,DEVICEID,json);
            }
        });
    }
/*
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.connectBtn:
                Toast.makeText(this, "connectBtn", Toast.LENGTH_SHORT).show();
                Connecting();

//                Intent intent = new Intent(ConnectActivity.this, StateActivity.class);
//                startActivity(intent);
                break;
            case R.id.getTemp:
                Toast.makeText(this, "getTemp", Toast.LENGTH_SHORT).show();
                HttpUtil httpUtil = new HttpUtil();
                String getContainer = httpUtil.doGet("http://api.heclouds.com/keys");
                Log.d("getContainer",getContainer);
                break;
            case R.id.setTemp:
                Toast.makeText(this, "setTemp", Toast.LENGTH_SHORT).show();
                break;
            default:
                Log.d("onClick","default");
                break;
        }
    }
*/
    private void Connecting(){

    }
}
