package com.wtwd.translate;

import android.app.Application;

import com.lzy.okgo.OkGo;
import com.mob.MobSDK;
import com.wtwd.translate.bean.dao.DaoMaster;
import com.wtwd.translate.db.DaoManager;
import com.wtwd.translate.db.DaoUtils;
import com.wtwd.translate.utils.blue.SppBluetoothManager;

/**
 * time:2018/1/18
 * Created by w77996
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        SppBluetoothManager.getInstance(getApplicationContext()).bluetoothRegisterReceiver();
       /* SppBluetoothMessagerManager.getInstance(getApplicationContext()).bluetoothRegisterReceiver();*/
        MobSDK.init(this);
       // DaoManager.getInstance().getDaoMaster();
       // DaoMaster.
        DaoUtils.init(this);
       // SppBluetoothManager.getInstance(getApplicationContext()).start();
    }


}
