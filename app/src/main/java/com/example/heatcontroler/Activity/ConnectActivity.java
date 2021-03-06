package com.example.heatcontroler.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.heatcontroler.MainActivity;
import com.example.heatcontroler.Model.TempJsonBean;
import com.example.heatcontroler.R;
import com.example.heatcontroler.utils.AppContext;
import com.example.heatcontroler.utils.HttpUtil;
import com.example.heatcontroler.utils.ICallBack;
import com.example.heatcontroler.utils.QuickToolsUtil;
import com.google.gson.Gson;

public class ConnectActivity extends AppCompatActivity {
    EditText deviceId;
    EditText apiKey;
    Button connectBtn;

    final String TAG = "ConnectActivity";
    final String APIKEY = "4cp78=CBjGQnKhtReMCIb4j4gcw=";
    final String DEVICEID = "29568317";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);

        // 设置全局变量
        final AppContext app = new AppContext();

        deviceId = (EditText) findViewById(R.id.deviceId);
        deviceId.setText(DEVICEID);
        apiKey = findViewById(R.id.apiKey);
        apiKey.setText(APIKEY);
        connectBtn = findViewById(R.id.connectBtn);
        connectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                app.setAPIKEY(String.valueOf(apiKey.getText()));
                app.setDEVICEID(String.valueOf(deviceId.getText()));

                startActivity(new Intent(ConnectActivity.this, SetTempActivity.class));
            }
        });

    }

}
