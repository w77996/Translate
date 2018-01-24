package com.wtwd.translate.activity;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Switch;

import com.wtwd.translate.R;
import com.wtwd.translate.utils.blue.SppBluetoothManager;
import com.wtwd.translate.utils.blue.SppBluetoothReceivedManager;

/**
 * time:2018/1/24
 * Created by w77996
 */

public class DevBindActivity extends Activity  {


    Switch mSwitch;

    SppBluetoothReceivedManager mSppBluetoothReceivedManager;
    SppBluetoothManager mSppBluetoothManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devbind);
        mSppBluetoothReceivedManager = SppBluetoothReceivedManager.getInstance(this);
        mSppBluetoothManager = SppBluetoothManager.getInstance(this);

        initBluetooth();
    }

    private void initBluetooth() {
        if(!mSppBluetoothManager.enableBluetooth()){

        }else if(mSppBluetoothManager.getState() == SppBluetoothManager.STATE_CONNECTED && mSppBluetoothReceivedManager.getState() == SppBluetoothReceivedManager.STATE_CONNECTED){

        }else if(mSppBluetoothManager.getState() != SppBluetoothManager.STATE_CONNECTED || mSppBluetoothReceivedManager.getState() != SppBluetoothReceivedManager.STATE_CONNECTED){
            // TODO: 2018/1/24 获取蓝牙对象
        }
    }

    public void initView(){

    }
    private void initListener(){
        mSppBluetoothManager.setBluetoothListener(new SppBluetoothManager.BluetoothListener() {
            @Override
            public void notifyChangeConnectstate(int mState) {

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

        mSppBluetoothReceivedManager.setBluetoothListener(new SppBluetoothReceivedManager.BluetoothListener() {
            @Override
            public void notifyChangeConnectstate(int mState) {

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
    }
}
