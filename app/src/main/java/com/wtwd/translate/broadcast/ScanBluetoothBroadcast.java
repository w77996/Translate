package com.wtwd.translate.broadcast;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * time:2018/1/3
 * Created by w77996
 * Github:https://github.com/w77996
 * CSDN:http://blog.csdn.net/w77996?viewmode=contents
 */
public class ScanBluetoothBroadcast extends BroadcastReceiver{
    private final static String TAG = "ScanBluetoothBroadcast";
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if(BluetoothDevice.ACTION_FOUND.equals(action)){
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_NAME);
            if(device.getBondState() != BluetoothDevice.BOND_BONDED){
                Log.d(TAG,device.getName() +":"+device.getAddress());
            }
        }

    }
}
