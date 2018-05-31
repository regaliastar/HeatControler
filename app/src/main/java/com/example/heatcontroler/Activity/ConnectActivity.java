package com.example.heatcontroler.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.heatcontroler.R;

public class ConnectActivity extends AppCompatActivity {
    EditText productId;
    EditText apiKey;
    Button connectBtn;
    final String PID = "130185";
    final String APIKEY = "L63i2WDONLZOth1sE=FNXckQG20=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);

        productId = (EditText) findViewById(R.id.productId);
        productId.setText(PID);
        apiKey = findViewById(R.id.apiKey);
        apiKey.setText(APIKEY);
        connectBtn = findViewById(R.id.connectBtn);
        connectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Connecting();

//                Intent intent = new Intent(ConnectActivity.this, StateActivity.class);
//                startActivity(intent);
            }
        });
    }

    private void Connecting(){

    }
}
