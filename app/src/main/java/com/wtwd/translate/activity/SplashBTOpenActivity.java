package com.wtwd.translate.activity;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wtwd.translate.R;
import com.wtwd.translate.adapter.BlueScanListAdapter;
import com.wtwd.translate.utils.Constants;
import com.wtwd.translate.utils.blue.SppBluetoothManager;
import com.wtwd.translate.utils.permissions.PermissionsActivity;
import com.wtwd.translate.utils.permissions.PermissionsChecker;
import com.wtwd.translate.view.BTOpenDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * time:2018/1/12
 * Created by w77996
 */
public class SplashBTOpenActivity extends Activity implements View.OnClickListener, BTOpenDialog.OnCenterItemClickListener,SppBluetoothManager.BluetoothListener{
    /**
     * 对话框
     */
    private BTOpenDialog mBTOpenDialog;
    /**
     *跳过
     */
    private TextView tv_refresh;
    /**
     * 下一步
     */
    private RelativeLayout rel_next;
    /**
     * 蓝牙处理类
     */
    SppBluetoothManager mSppBluetoothManager;
    /**
     * 蓝牙列表
     */
    ListView mBlueScanListView;
    BlueScanListAdapter mBlueScanListAdapter;
    /**
     * 搜索的蓝牙集
     */
    List<BluetoothDevice> blueDeviceList = new ArrayList<>();

    private static final String TAG = "SplashBtOpenActivity";

    private static final int PERMISSIONS_REQUEST_CODE = 0111; // 请求码
    private static final int GPS_REQUEST_CODE = 0112; // 请求码

    private PermissionsChecker mPermissionsChecker; // 权限检测器

    // 所需的全部权限
    static final String[] PERMISSIONS = new String[]{
            Manifest.permission.ACCESS_COARSE_LOCATION
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_btopen);

        mSppBluetoothManager = SppBluetoothManager.getInstance(this);
        mSppBluetoothManager.setBluetoothListener(this);
        if(!mSppBluetoothManager.enableBluetooth()&& mSppBluetoothManager.getState() == SppBluetoothManager.STATE_NONE){
            mBTOpenDialog.show();
        }
       // mSppBluetoothManager.getConnectBt();
        //mSppBluetoothManager.bluetoothRegisterReceiver();
        //mSppBluetoothManager.bluetoothStatusRegisterReceiver();//注册蓝牙开关状态
       // mSppBluetoothManager.bluetoothRegisterReceiver();//注册蓝牙搜索

        initView();
        addListener();


    }


    /**
     * 蓝牙初始化
     */
    private void blueToothInit(){

        if(mSppBluetoothManager.enableBluetooth()){

            mSppBluetoothManager.startDiscovery();
        }else{
            mBTOpenDialog.show();
        }
       // mSppBluetoothManager.openBluetooth();

    }
    @Override
    protected void onResume() {
        super.onResume();
        mPermissionsChecker = new PermissionsChecker(this);
        // 缺少权限时, 进入权限配置页面
        if (mPermissionsChecker.lacksPermissions(PERMISSIONS)) {
            startPermissionsActivity();
        }else{
            Log.d(TAG,"OnResume 拥有权限");
           // blueToothInit();
        }
    }

    /**
     * 初始化界面
     */
    private void initView() {
        mBTOpenDialog = new BTOpenDialog(this,R.layout.bt_open_dialog,new int[]{R.id.dialog_cancel, R.id.dialog_sure});
        mBTOpenDialog.setOnCenterItemClickListener(this);
        //蓝牙没有打开，提示用户打开蓝牙

        mBlueScanListView = (ListView)findViewById(R.id.lv_scan_blue);
        mBlueScanListAdapter = new BlueScanListAdapter(this,blueDeviceList);
        mBlueScanListView.setAdapter(mBlueScanListAdapter);
       /* mBlueScanListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG,"点击的mac为: "+blueDeviceList.get(position).getAddress());
                //传递bluedevice至下个连接界面
                Intent intent = new Intent(*//*SplashBTOpenActivity.this,SplashDevPairActivity.class*//*);
                intent.putExtra(Constants.BLUE_DEVICE, blueDeviceList.get(position));
                setResult(Constants.BLUE_SCAN,intent);
                finish();
            }
        });*/

        tv_refresh = (TextView)findViewById(R.id.tv_refresh);
        //rel_next = (RelativeLayout) findViewById(R.id.rel_next);
        //rel_next.set(false);
    }

    /**
     * 监听事件
     */
    private void addListener(){
        tv_refresh.setOnClickListener(this);
//        rel_next.setOnClickListener(this);
    }
    @Override
    public void OnCenterItemClick(BTOpenDialog dialog, View view) {
        switch (view.getId()){
            case R.id.dialog_sure:
                Toast.makeText(SplashBTOpenActivity.this,"确定按钮",Toast.LENGTH_SHORT).show();
                //mSppBluetoothManager.openBluetooth();
                Intent intent =  new Intent(Settings.ACTION_BLUETOOTH_SETTINGS);
                startActivityForResult(intent,Constants.SETTING_BLUE);
                break;
            default:
                break;
        }

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.tv_refresh:
                blueDeviceList.clear();
                mBlueScanListAdapter.notifyDataSetChanged();
                blueToothInit();
                break;
            /*case R.id.rel_next:
                Intent splashDevBindIntent = new Intent(SplashBTOpenActivity.this,SplashDevPairActivity.class);
                startActivity(splashDevBindIntent);
                break;*/
        }

    }

    /**
     * 权限开启跳转
     */
    private void startPermissionsActivity() {
        PermissionsActivity.startActivityForResult(this, PERMISSIONS_REQUEST_CODE, PERMISSIONS);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 拒绝时, 关闭页面, 缺少主要权限, 无法运行
        if (requestCode == PERMISSIONS_REQUEST_CODE && resultCode == PermissionsActivity.PERMISSIONS_DENIED) {
            finish();
        }
        /*if(requestCode == GPS_REQUEST_CODE){
            if(!mSppBluetoothManager.isGpsEnable(this)){
                Toast.makeText(this,"请打开GPS,否则无法搜索到蓝牙",Toast.LENGTH_SHORT).show();
            }
        }*/
        if(requestCode == Constants.SETTING_BLUE){
            if(mSppBluetoothManager.enableBluetooth()){

            }else{
                Toast.makeText(SplashBTOpenActivity.this,"蓝牙未打开",Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void notifyChangeConnectstate(int mState) {
            Log.d(TAG,"蓝牙状态 ：" +mState);
    }

    @Override
    public void foundBluetoothDevice(BluetoothDevice mDevice) {
        Log.d(TAG,""+mDevice.getAddress());
        blueDeviceList.add(mDevice);
        mBlueScanListAdapter.notifyDataSetChanged();
    }

    @Override
    public void scanBluetoothFinish() {
//        rel_next.setClickable(true);
    }

    @Override
    public void notifyChangeOpenstate(int mState) {
        if(mState == BluetoothAdapter.STATE_ON){
            Log.d(TAG,"蓝牙打开了");
           // mSppBluetoothManager.bluetoothRegisterReceiver();
            blueDeviceList.clear();
            mBlueScanListAdapter.notifyDataSetChanged();
            mSppBluetoothManager.startDiscovery();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
       // mSppBluetoothManager.bluetoothUnregisterReceiver();
        //mSppBluetoothManager.bluetoothUnregisterReceiver();
    }
}
