package com.wtwd.translate.activity;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.wtwd.translate.R;
import com.wtwd.translate.utils.Constants;
import com.wtwd.translate.utils.blue.GetConnectedBluetoothDeviceFromSystem;
import com.wtwd.translate.utils.blue.SppBluetoothManager;
import com.wtwd.translate.utils.blue.SppBluetoothReceivedManager;
import com.wtwd.translate.utils.permissions.PermissionsActivity;

/**
 * time:2018/1/24
 * Created by w77996
 */

public class DevBindActivity extends Activity  implements View.OnClickListener{


    private static final String TAG = "DevBindActivity";

    Switch dev_bind_switch;
    ImageView img_blue;
    TextView dev_bind_con_state;
    ImageView language_select_back;

    SppBluetoothReceivedManager mSppBluetoothReceivedManager;
    SppBluetoothManager mSppBluetoothManager;
    private int mSppBluetoothManagerState;
    private int mSppBluetoothReceivedManagerState;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devbind);
        mSppBluetoothReceivedManager = SppBluetoothReceivedManager.getInstance(this);
        mSppBluetoothManager = SppBluetoothManager.getInstance(this);
        initView();
        initBluetooth();
        initListener();
    }

    private void initBluetooth() {
        if(!mSppBluetoothManager.enableBluetooth()){
            // TODO: 2018/1/25 请打开蓝牙
        }else if(mSppBluetoothManager.getState() == SppBluetoothManager.STATE_CONNECTED && mSppBluetoothReceivedManager.getState() == SppBluetoothReceivedManager.STATE_CONNECTED){
            dev_bind_switch.setChecked(true);
            dev_bind_switch.setClickable(false);
        }else if(mSppBluetoothManager.getState() != SppBluetoothManager.STATE_CONNECTED || mSppBluetoothReceivedManager.getState() != SppBluetoothReceivedManager.STATE_CONNECTED){
            // TODO: 2018/1/24 获取蓝牙对象
        }
    }

    public void initView(){
        img_blue = (ImageView)findViewById(R.id.img_blue);
        dev_bind_switch = (Switch)findViewById(R.id.dev_bind_switch);
        dev_bind_con_state = (TextView)findViewById(R.id.dev_bind_con_state);
        language_select_back = (ImageView)findViewById(R.id.language_select_back);
    }
    private void initListener(){
        img_blue.setOnClickListener(this);
        //dev_bind_switch.setChecked(false);
        dev_bind_switch.setOnClickListener(this);
        language_select_back.setOnClickListener(this);
        mSppBluetoothManager.setBluetoothListener(new SppBluetoothManager.BluetoothListener() {
            @Override
            public void notifyChangeConnectstate(int mState) {
                mSppBluetoothManagerState = mState;
                if(mSppBluetoothManagerState == SppBluetoothManager.STATE_CONNECTED && mSppBluetoothReceivedManagerState == SppBluetoothReceivedManager.STATE_CONNECTED){
                    Log.d(TAG,"蓝牙连接state "+mState);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dev_bind_switch.setChecked(true);
                            dev_bind_con_state.setText(R.string.blue_iscon);
                        }
                    });
                }else if(mSppBluetoothManagerState != SppBluetoothManager.STATE_CONNECTED || mSppBluetoothReceivedManagerState != SppBluetoothReceivedManager.STATE_CONNECTED){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dev_bind_switch.setChecked(false);
                            dev_bind_con_state.setText(R.string.blue_uncon);
                        }
                    });
                }
            }

            @Override
            public void foundBluetoothDevice(BluetoothDevice mDevice) {

            }

            @Override
            public void scanBluetoothFinish() {

            }

            @Override
            public void notifyChangeOpenstate(int mState) {

            }
        });
        mSppBluetoothManager.setGetConnectedBluetoothDeviceFromSystem(new GetConnectedBluetoothDeviceFromSystem() {
            @Override
            public void onConnectedBluetoothDevice(BluetoothDevice mDevice) {
                if(null == mDevice){
                    Log.e(TAG,"mDevice 为空");
                    dev_bind_switch.setChecked(false);
                    Toast.makeText(DevBindActivity.this, R.string.unconnect_dev,Toast.LENGTH_SHORT).show();
                }else{
                    Log.e(TAG,"蓝牙连接");
                    mSppBluetoothManager.connect(mDevice);
                    mSppBluetoothReceivedManager.connect(mDevice);
                }
            }
        });


        mSppBluetoothReceivedManager.setBluetoothListener(new SppBluetoothReceivedManager.BluetoothListener() {
            @Override
            public void notifyChangeConnectstate(int mState) {
                mSppBluetoothReceivedManagerState = mState;
                if(mSppBluetoothManagerState == SppBluetoothManager.STATE_CONNECTED && mSppBluetoothReceivedManagerState == SppBluetoothReceivedManager.STATE_CONNECTED){
                    Log.d(TAG,"蓝牙连接state "+mState);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dev_bind_switch.setChecked(true);
                            dev_bind_con_state.setText(R.string.blue_iscon);
                        }
                    });
                }else if(mSppBluetoothManagerState != SppBluetoothManager.STATE_CONNECTED || mSppBluetoothReceivedManagerState != SppBluetoothReceivedManager.STATE_CONNECTED){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dev_bind_switch.setChecked(false);
                            dev_bind_con_state.setText(R.string.blue_uncon);
                        }
                    });
                }
            }

            @Override
            public void foundBluetoothDevice(BluetoothDevice mDevice) {

            }

            @Override
            public void scanBluetoothFinish() {

            }

            @Override
            public void notifyChangeOpenstate(int mState) {

            }
        });

        mSppBluetoothReceivedManager.setGetConnectedBluetoothDeviceFromSystem(new GetConnectedBluetoothDeviceFromSystem() {
            @Override
            public void onConnectedBluetoothDevice(BluetoothDevice mDevice) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.dev_bind_switch:
                Intent intent =  new Intent(Settings.ACTION_BLUETOOTH_SETTINGS);
                startActivityForResult(intent, Constants.SETTING_BLUE);
                break;
            case R.id.img_blue:
                Intent settingIntent =  new Intent(Settings.ACTION_BLUETOOTH_SETTINGS);
                startActivityForResult(settingIntent, Constants.SETTING_BLUE);
                break;
            case R.id.language_select_back:
                finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


            if(requestCode == Constants.SETTING_BLUE){
                if(mSppBluetoothManager.enableBluetooth()){
                    mSppBluetoothManager.getConnectBt();
                    mSppBluetoothReceivedManager.start();
                }else{
                    dev_bind_switch.setChecked(false);
                    dev_bind_con_state.setText(R.string.blue_uncon);
                    Toast.makeText(this, R.string.blue_close,Toast.LENGTH_SHORT).show();
                }
            }

    }
}
