package com.wtwd.translate.activity;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wtwd.translate.MainActivity;
import com.wtwd.translate.R;
import com.wtwd.translate.utils.Constants;
import com.wtwd.translate.utils.blue.SppBluetoothManager;
import com.wtwd.translate.utils.permissions.PermissionsActivity;
import com.wtwd.translate.utils.permissions.PermissionsChecker;
import com.wtwd.translate.view.BTOpenDialog;

/**
 * time:2018/1/11
 * Created by w77996
 */
public class SplashDevBindActivity extends Activity implements View.OnClickListener,SppBluetoothManager.BluetoothListener,BTOpenDialog.OnCenterItemClickListener,SppBluetoothManager.BluetoothReceiverMessageListener {


    public static final String TAG = "SplashDevBindActivity";

    /**
     * 跳过
     */
    private TextView tv_ignore;
    /**
     * 下一步
     */
    private ImageView img_next;

    /**
     * 蓝牙处理类
     */
    SppBluetoothManager mSppBluetoothManager;
    /**
     * 对话框
     */
    private BTOpenDialog mBTOpenDialog;

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
        setContentView(R.layout.activity_dev_bind);
        mSppBluetoothManager = SppBluetoothManager.getInstance(this);
        mSppBluetoothManager.setBluetoothListener(this);
        mSppBluetoothManager.setBluetoothReceiverMessageListener(this);
        initView();
        mBTOpenDialog.show();
        addListener();
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
     * 权限开启跳转
     */
    private void startPermissionsActivity() {
        PermissionsActivity.startActivityForResult(this, PERMISSIONS_REQUEST_CODE, PERMISSIONS);
    }
    /**
     * 初始化界面
     */
    private void initView() {
        mBTOpenDialog = new BTOpenDialog(this,R.layout.bt_open_dialog,new int[]{R.id.dialog_cancel, R.id.dialog_sure});
        mBTOpenDialog.setOnCenterItemClickListener(this);
        tv_ignore = (TextView)findViewById(R.id.devbind_ignore);
        img_next  = (ImageView)findViewById(R.id.img_devbind_next);
    }

    /**
     * 添加监听事件
     */
    private void addListener() {
        tv_ignore.setOnClickListener(this);
        img_next.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.devbind_ignore:
                Intent MainIntent = new Intent(SplashDevBindActivity.this,MainActivity.class);
                startActivity(MainIntent);
                finish();
                break;
            case R.id.img_devbind_next:
                if(!mSppBluetoothManager.enableBluetooth() && mSppBluetoothManager.getState() == SppBluetoothManager.STATE_NONE){
                    mBTOpenDialog.show();
                }else{
                    Intent splashBTOpenIntent = new Intent(SplashDevBindActivity.this,SplashDevPairActivity.class);
                    startActivity(splashBTOpenIntent);
                }
                break;

               // Intent splashBTOpenIntent = new Intent(SplashDevBindActivity.this,SplashBTOpenActivity.class);
                //startActivityForResult(splashBTOpenIntent,Constants.BLUE_SCAN);
               // startActivity(splashBTOpenIntent);
                /*Intent splashBTOpenIntent = new Intent(SplashDevBindActivity.this,SplashDevPairActivity.class);
                startActivity(splashBTOpenIntent);*/
                //finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG,"rquestCode" + requestCode);
        Log.d(TAG,"resultCode" + resultCode);

        if(resultCode == Constants.BLUE_SCAN){
            Log.d(TAG,"返回成功");
            BluetoothDevice bluetoothDevice = data.getParcelableExtra(Constants.BLUE_DEVICE);
            if(bluetoothDevice != null){
                Intent SplashDevBindActivityIntent = new Intent(SplashDevBindActivity.this,SplashDevPairActivity.class);
                SplashDevBindActivityIntent.putExtra(Constants.BLUE_DEVICE,bluetoothDevice);
                startActivity(SplashDevBindActivityIntent);
                finish();
            }else{
                Log.d(TAG,"bluetooth null");
            }
        }
        // 拒绝时, 关闭页面, 缺少主要权限, 无法运行
        if (requestCode == PERMISSIONS_REQUEST_CODE && resultCode == PermissionsActivity.PERMISSIONS_DENIED) {
            finish();
        }
        if(requestCode == Constants.SETTING_BLUE){
            if(mSppBluetoothManager.enableBluetooth()){

            }else{
                Toast.makeText(SplashDevBindActivity.this, R.string.blue_close,Toast.LENGTH_SHORT).show();
            }
        }
    }

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

    @Override
    public void OnCenterItemClick(BTOpenDialog dialog, View view) {
        switch (view.getId()){
            case R.id.dialog_sure:
                Toast.makeText(SplashDevBindActivity.this,"确定按钮",Toast.LENGTH_SHORT).show();
                //mSppBluetoothManager.openBluetooth();
                Intent intent =  new Intent(Settings.ACTION_BLUETOOTH_SETTINGS);
                startActivityForResult(intent,Constants.SETTING_BLUE);
                break;
            default:
                break;
        }
    }

    @Override
    public void readByteFromOtherDevice(byte[] reads) {
        Log.d(TAG,reads.length+"");
    }

    @Override
    public void writeByteToOtherDeviceSuccess() {

    }

    @Override
    public void writeByteToOtherDeviceFailed() {

    }
}
