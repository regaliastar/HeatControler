package com.example.heatcontroler.Activity;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.heatcontroler.MusicProgressBar;
import com.example.heatcontroler.R;
import com.example.heatcontroler.utils.AppContext;
import com.example.heatcontroler.utils.HttpUtil;
import com.example.heatcontroler.utils.ICallBack;
import com.example.heatcontroler.utils.QuickToolsUtil;

public class SetTempActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    private Button buttonStart;
    private MusicProgressBar musicProgressBar;
    private final String TAG = "设置温度界面";

    final AppContext app = new AppContext();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // 自己的设置
        buttonStart = (Button) findViewById(R.id.btn_start);
        musicProgressBar= (MusicProgressBar) findViewById(R.id.musicProgressBar);
        musicProgressBar.setMax(150);
//        musicProgressBar.getDefaultProgress(87);    //37 = 87 - 50

        buttonStart.setOnClickListener(this);

        init();

    }

    /**
     * 初始化温度、设备状态
     * */
    private void init(){
        setCurrentTemperature();
        getCmdMsg();
    }

    private void setCurrentTemperature(){
        HttpUtil httpUtil = new HttpUtil();

        int count = 1;
        httpUtil.setGetTemperatureCallBack(new ICallBack() {
            @Override
            public void postExec(String result) {
                int value = Integer.parseInt(QuickToolsUtil.getTemperatureValue(result));
                if(value < 0 || value > 100){
                    value = 20;
                }
                musicProgressBar.setProgress(value+50);
                app.setCurrTemp(""+value);
                Log.d(TAG,""+value);
            }
        });
        httpUtil.getTemperature(AppContext.getAPIKEY(),AppContext.getDEVICEID(),count);
    }

    private void getCmdMsg(){
        HttpUtil httpUtil = new HttpUtil();
        final AppContext app = new AppContext();
        httpUtil.setCmdCallBack(new ICallBack() {
            @Override
            public void postExec(String str) {
                String value = QuickToolsUtil.getCmdValue(str);
                app.setCMDMSG(value);
                Log.d(TAG,value);
            }
        });
        httpUtil.getCmd(AppContext.getAPIKEY(),AppContext.getDEVICEID());

        // 延时打印命令信息
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(SetTempActivity.this, ""+AppContext.getCMDMSG(), Toast.LENGTH_SHORT).show();
            }
        },1000);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {
            //state
            Intent intent = new Intent(SetTempActivity.this, StateActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View view) {

        if (0 != musicProgressBar.getProgress()){
            //
            Toast.makeText(this, "您设置了 "+musicProgressBar.getTemperature()+"℃", Toast.LENGTH_SHORT).show();
            HttpUtil httpUtil = new HttpUtil();
            String json = "{'expectedTemperature':"+musicProgressBar.getTemperature()+"}";
            httpUtil.setTemperature(AppContext.getAPIKEY(),AppContext.getDEVICEID(),json);
            app.setCurrTemp(""+musicProgressBar.getTemperature());

        }else{
            Toast.makeText(this, "您还没有选择温度", Toast.LENGTH_SHORT).show();
        }
    }
}

