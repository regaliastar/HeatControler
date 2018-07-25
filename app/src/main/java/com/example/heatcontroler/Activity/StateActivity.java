package com.example.heatcontroler.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.heatcontroler.R;
import com.example.heatcontroler.utils.AppContext;
import com.example.heatcontroler.utils.HttpUtil;
import com.example.heatcontroler.utils.ICallBack;
import com.example.heatcontroler.utils.QuickToolsUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.view.LineChartView;

public class StateActivity extends Activity implements NavigationView.OnNavigationItemSelectedListener {
    private LineChartView lineChart;
    private final int dot_number = 7;   //坐标点的个数
    private final String TAG = "StateActivity";
//    String[] weeks = {"12:00","13:00","14:00","15:00","16:00","17:00","18:00"};//X轴的标注
//    int[] weather = {19,27,26,27,18,26,18};//图表的数据
    private List<PointValue> mPointValues = new ArrayList<PointValue>();
    private List<AxisValue> mAxisValues = new ArrayList<AxisValue>();

    private TextView currTemp;
    private TimerTask task = null;
    private Timer timer = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_state);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //初始化界面
        currTemp = findViewById(R.id.CurrTemp);
        setTimerTask(); //设置每 12s 更新一次坐标

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cancelTimerTask();
    }

    @Override
    protected void onStop() {
        super.onStop();
        cancelTimerTask();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        setTimerTask();
    }

    /**
     * 初始化当前温度、图表
     * */
    private void init(){
        lineChart = (LineChartView)findViewById(R.id.chart);
        mPointValues.clear();
        mAxisValues.clear();
        // 自己的控件
        getAxisLables();//获取x轴的标注
        getAxisPoints();//获取坐标点
    }

    /**
     * 设置定时任务
     * */
    public Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    init();
                    break;
            }
        }
    };

    private void setTimerTask(){
        cancelTimerTask();
        task = new TimerTask() {
            @Override
            public void run() {
                Message message = new Message();
                message.what = 0;
                mHandler.sendMessage(message);
            }
        };
        timer = new Timer();
        long delay = 0;
        long intervalPeriod = 12 * 1000;
        timer.scheduleAtFixedRate(task, delay, intervalPeriod);
    }
    private void cancelTimerTask(){
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (task != null) {
            task.cancel();
            task = null;
        }
    }

    /**
     * 初始化LineChart的一些设置
     */
    private void initLineChart(){
        Line line = new Line(mPointValues).setColor(Color.WHITE).setCubic(false);  //折线的颜色
        List<Line> lines = new ArrayList<Line>();
        line.setShape(ValueShape.CIRCLE);//折线图上每个数据点的形状  这里是圆形 （有三种 ：ValueShape.SQUARE  ValueShape.CIRCLE  ValueShape.SQUARE）
        line.setCubic(true);//曲线是否平滑
        line.setFilled(true);//是否填充曲线的面积
//      line.setHasLabels(true);//曲线的数据坐标是否加上备注
        line.setHasLabelsOnlyForSelected(true);//点击数据坐标提示数据（设置了这个line.setHasLabels(true);就无效）
        line.setHasLines(true);//是否用直线显示。如果为false 则没有曲线只有点显示
        line.setHasPoints(true);//是否显示圆点 如果为false 则没有原点只有点显示
        lines.add(line);
        LineChartData data = new LineChartData();
        data.setLines(lines);

        //坐标轴
        Axis axisX = new Axis(); //X轴
        axisX.setHasTiltedLabels(true);
        axisX.setTextColor(Color.WHITE);  //设置字体颜色
//        axisX.setName("最近时间");  //表格名称
        axisX.setTextSize(10);//设置字体大小
        axisX.setMaxLabelChars(7);  //最多几个X轴坐标
        axisX.setValues(mAxisValues);  //填充X轴的坐标名称
        data.setAxisXBottom(axisX); //x 轴在底部
//      data.setAxisXTop(axisX);  //x 轴在顶部

        Axis axisY = new Axis();  //Y轴
        axisY.setMaxLabelChars(7); //默认是3，只能看最后三个数字
//        axisY.setName("温度");//y轴标注
        axisY.setTextSize(10);//设置字体大小
        axisY.setTextColor(Color.WHITE);

        data.setAxisYLeft(axisY);  //Y轴设置在左边
//      data.setAxisYRight(axisY);  //y轴设置在右边

        //设置行为属性，支持缩放、滑动以及平移
        lineChart.setInteractive(true);
        lineChart.setZoomType(ZoomType.HORIZONTAL_AND_VERTICAL);
        lineChart.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
        lineChart.setLineChartData(data);
        lineChart.setVisibility(View.VISIBLE);
    }


    /**
     * X 轴的显示
     */
    private void getAxisLables(){

//        for (int i = 0; i < weeks.length; i++) {
//            mAxisValues.add(new AxisValue(i).setLabel(weeks[i]));
//        }
    }

    /**
     * 图表的每个点的显示
     */
    private void getAxisPoints(){
        HttpUtil httpUtil = new HttpUtil();
        httpUtil.setGetTemperatureCallBack(new ICallBack() {
            @Override
            public void postExec(String str) {
                //设置点的值
                Log.d(TAG,str);
                String[] value = QuickToolsUtil.getTemperatureValueArray(str, dot_number);
                Log.d(TAG, Arrays.toString(value));
                final AppContext app = new AppContext();
                app.setCurrTemp(value[0]);
                currTemp.setText(AppContext.getCurrTemp());
                if(checkSame(value)){
                    value[value.length - 1] = (Integer.parseInt(value[value.length - 1]) - 1)+"";
                }
                for(int i = 0; i < value.length;i++){
                    int v = Integer.parseInt(value[i]);
                    mPointValues.add(new PointValue(i, v));
                }

                //设置横坐标
                String[] time = QuickToolsUtil.getTemperatureTimeArray(str, dot_number);

                for(int i = 0; i < time.length;i++){
                    mAxisValues.add(new AxisValue(i).setLabel(time[i]));
                }

                initLineChart();//初始化
            }
        });
        httpUtil.getTemperature(AppContext.getAPIKEY(),AppContext.getDEVICEID(),dot_number);

//        for (int i = 0; i < weather.length; i++) {
//            mPointValues.add(new PointValue(i, weather[i]));
//        }
    }

    private boolean checkSame(String[] arr){
        boolean flag = true;
        for(int i = 0; i < arr.length - 1; i++){
            if( !arr[i].equals(arr[i+1] )){
                flag = false;
            }
        }
        Log.d(TAG, "flag: "+flag);
        return flag;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
            Intent intent = new Intent(StateActivity.this, SetTempActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_gallery) {
            //state
            Intent intent = new Intent(StateActivity.this, StateActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
