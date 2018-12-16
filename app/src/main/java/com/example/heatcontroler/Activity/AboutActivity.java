package com.example.heatcontroler.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.heatcontroler.R;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

public class AboutActivity extends AppCompatActivity {
    private RelativeLayout relativeLayout;

    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        initViews();

        View aboutPage = new AboutPage(this)
                .isRTL(false)
                .setImage(R.drawable.cloud)//图片
                .setDescription("智能家居-基于单片机控制的智能地暖")//介绍
                .addItem(new Element().setTitle("Version 1.0.2"))
                .addGroup("与我联系")
                .addEmail("1183080130@qq.com")//邮箱
                .addWebsite("https://github.com/regaliastar/HeatControler")//网站
                .addPlayStore("com.example.abouttest")//应用商店
                .addGitHub("regaliastar/HeatControler")//github
                .create();

        relativeLayout.addView(aboutPage);

    }


    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            default:

        }
        return true;
    }

    private void initViews(){
        relativeLayout= (RelativeLayout) findViewById(R.id.relativeLayout);
        toolbar= (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
            Intent intent = new Intent(AboutActivity.this, SetTempActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_gallery) {
            //state
            Intent intent = new Intent(AboutActivity.this, StateActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_share) {
            Intent intent = new Intent(AboutActivity.this, AboutActivity.class);
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
