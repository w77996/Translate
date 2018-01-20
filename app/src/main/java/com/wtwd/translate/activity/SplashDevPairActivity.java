package com.wtwd.translate.activity;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wtwd.translate.MainActivity;
import com.wtwd.translate.R;
import com.wtwd.translate.utils.Constants;
import com.wtwd.translate.utils.blue.GetConnectedBluetoothDeviceFromSystem;
import com.wtwd.translate.utils.blue.SppBluetoothManager;

import java.util.Timer;
import java.util.TimerTask;

/**
 * time:2018/1/10
 * Created by w77996
 */
public class SplashDevPairActivity extends Activity implements View.OnClickListener ,SppBluetoothManager.BluetoothListener,GetConnectedBluetoothDeviceFromSystem{

    private static final String TAG = "SplashDevPairActivity";
    /**
     * 跳过
     */
    private TextView tv_ignore;
    /**
     * 底部图片
     */
    private ImageView img_pair_state;
    /**
     * 配对旋转进度
     */
    private ImageView img_bind_circle;
    /**
     *  连接状态
     */
    private TextView tv_pair_state;



    BluetoothDevice mBluetoothDevice;
    private SppBluetoothManager mSppBluetoothManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devpair);
        initView();
        addListener();
        //mBluetoothDevice = getIntent().getParcelableExtra(Constants.BLUE_DEVICE);
        mSppBluetoothManager = SppBluetoothManager.getInstance(this);
        mSppBluetoothManager.setBluetoothListener(this);

        mSppBluetoothManager.setGetConnectedBluetoothDeviceFromSystem(this);
        mSppBluetoothManager.getConnectBt();

       // mSppBluetoothManager.start();

//        connectBlue();
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG,"mSppBluetoothManager.start()");
        mSppBluetoothManager.start();
    }

    /**
     * 初始化界面控件
     */
    private void initView() {
        tv_ignore = (TextView)findViewById(R.id.devpair_ignore);
        img_pair_state = (ImageView)findViewById(R.id.img_devpair_next);
        img_bind_circle = (ImageView)findViewById(R.id.img_bind_circle);
        tv_pair_state = (TextView)findViewById(R.id.tv_pair_state);
        Animation circle_anim = AnimationUtils.loadAnimation(SplashDevPairActivity.this, R.anim.anim_round_rotate);
        LinearInterpolator interpolator = new LinearInterpolator();  //设置匀速旋转，在xml文件中设置会出现卡顿
        circle_anim.setInterpolator(interpolator);
        if (circle_anim != null) {
            img_bind_circle.startAnimation(circle_anim);  //开始动画
        }
        tv_pair_state.setText("正在配对中...");
        img_pair_state.setClickable(false);
    }

    /**
     * 添加监听事件
     */
    private void addListener() {
        tv_ignore.setOnClickListener(this);
        img_pair_state.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.devpair_ignore:
                Intent mianIntent = new Intent(SplashDevPairActivity.this, MainActivity.class);
                startActivity(mianIntent);
                break;
            case R.id.img_devpair_next:
                Intent intent = new Intent(SplashDevPairActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    @Override
    public void notifyChangeConnectstate(int mState) {
        if(mState == SppBluetoothManager.STATE_CONNECTED){
            Log.d(TAG,"蓝牙连接state "+mState);
           /* img_bind_circle.clearAnimation();
           // Toast.makeText(SplashDevPairActivity.this,"蓝牙连接state "+mState,Toast.LENGTH_SHORT).show();
            tv_pair_state.setText("配对成功");
            img_pair_state.setClickable(true);*/
          /*  try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Intent intent = new Intent(SplashDevPairActivity.this,MainActivity.class);
            startActivity(intent);
            finish();*/
            //getHomeActivity();
        }
        if(mState == SppBluetoothManager.STATE_CONNECTING){
            Log.d(TAG,"蓝牙连接中state "+mState);
            /*tv_pair_state.setText("正在配对...");
            img_pair_state.setClickable(true);*/
           // Toast.makeText(SplashDevPairActivity.this,"蓝牙连接中state "+mState,Toast.LENGTH_SHORT).show();
        }
        if(mState == SppBluetoothManager.STATE_NONE){
            Log.d(TAG,"蓝牙断开连接状态state "+mState);
           // img_bind_circle.clearAnimation();
           /* tv_pair_state.setText("配对失败");
            img_pair_state.setClickable(true);*/

            //Toast.makeText(SplashDevPairActivity.this,"蓝牙断开连接状态state"+mState,Toast.LENGTH_SHORT).show();
        }
        if(mState == SppBluetoothManager.STATE_LISTEN){

        }

    }
    private void getHomeActivity() {
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            public void run() {
                Intent intent = new Intent(SplashDevPairActivity.this, MainActivity.class);
                startActivity(intent);
                //overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        };
        timer.schedule(task, 2500);

    }
        @Override
    public void foundBluetoothDevice(BluetoothDevice mDevice) {

    }

    @Override
    public void scanBluetoothFinish() {

    }

    @Override
    public void notifyChangeOpenstate(int mState) {
            Log.d(TAG,mState+"");

    }

    @Override
    public void onConnectedBluetoothDevice(BluetoothDevice mDevice) {
        if(null == mDevice){
            Log.e(TAG,"mDevice 为空");
            Toast.makeText(SplashDevPairActivity.this,"错误,未连接设备",Toast.LENGTH_SHORT).show();
            img_bind_circle.clearAnimation();
            tv_pair_state.setText("配对失败");
            img_pair_state.setClickable(true);
        }else{
            Log.e(TAG,"蓝牙连接");
            mSppBluetoothManager.connect(mDevice);
        }
    }
}
