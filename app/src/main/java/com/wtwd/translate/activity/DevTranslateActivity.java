package com.wtwd.translate.activity;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.wtwd.translate.R;
import com.wtwd.translate.adapter.DevTranListViewAdapter;
import com.wtwd.translate.bean.Recorder;
import com.wtwd.translate.bean.RecorderBean;
import com.wtwd.translate.utils.Constants;
import com.wtwd.translate.utils.SpUtils;
import com.wtwd.translate.utils.Utils;
import com.wtwd.translate.utils.audio.AudioMediaPlayManager;
import com.wtwd.translate.utils.audio.AudioStateChange;
import com.wtwd.translate.utils.blue.SppBluetoothManager;
import com.wtwd.translate.utils.blue.SppBluetoothMessagerManager;
import com.wtwd.translate.utils.blue.TranProtocalAnalysis;
import com.wtwd.translate.utils.micro.MicroRecogitionManager;
import com.wtwd.translate.view.BTOpenDialog;

import java.io.File;
import java.security.Key;
import java.util.ArrayList;
import java.util.List;

/**
 * time:2017/12/27
 * Created by w77996
 */
public class DevTranslateActivity extends Activity implements AudioStateChange,TranProtocalAnalysis.OnDeviceButtonPressedStateListener,View.OnClickListener,SppBluetoothManager.BluetoothListener,MicroRecogitionManager.MicroRecogitionManagerCallBack{

    public final static String TAG = "DevTransalteActivity";


    /**
     *
     */
    ImageView dev_menu;
    /**
     * 语音处理类
     */
    private AudioMediaPlayManager mAudioMediaPlayManager;
    /**
     * 翻译列表
     */
    private ListView mListViewDevTran;
    /**
     * 手机录音按钮
     */
    private ImageView mVoiceImage;
    /**
     * 手机录音保存路径
     */
    String mVoiceFilePath;
    /**
     * 语言选择
     */
    ImageView mDevMenu;
    /**
     * 对话框
     */
    private BTOpenDialog mBTOpenDialog;


    TranProtocalAnalysis mTranProtocalAnalysis;
    List<RecorderBean> mRecorderList;
    DevTranListViewAdapter mDevTranListViewAdapter;

    SppBluetoothManager mSppBluetoothManager;
    MicroRecogitionManager mMicroRecogitionManager;

    SppBluetoothMessagerManager mSppBluetoothMessagerManager;

    String leftLanguage;
    String rightLanguage;

    private SppBluetoothMessagerManager.BluetoothListener mSppMsgBluetoothListener = new SppBluetoothMessagerManager.BluetoothListener() {
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
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devtran);
        //录音和播放
        mAudioMediaPlayManager = AudioMediaPlayManager.getAudioMediaPlayManager(DevTranslateActivity.this);
        mAudioMediaPlayManager.setAudioStateChange(this);
        //协议解析
        mTranProtocalAnalysis = TranProtocalAnalysis.getTranProtocalAnalysis(this);
        mTranProtocalAnalysis.setOnDevicePressedStateListener(this);
        //spp蓝牙连接
        mSppBluetoothManager = SppBluetoothManager.getInstance(this);
       // mSppBluetoothManager.setBluetoothReceiverMessageListener(this);
        mSppBluetoothManager.setBluetoothListener(this);
        //微软
        mMicroRecogitionManager = MicroRecogitionManager.getMicroRecogitionManager(this);
        mMicroRecogitionManager.setmicroRecogitionManagerCallBack(this);

        //连接spp协议
        mSppBluetoothMessagerManager  = SppBluetoothMessagerManager.getInstance(this);
        mSppBluetoothMessagerManager.setBluetoothListener(mSppMsgBluetoothListener);

        checkBlueConnect();

        initView();
        addListener();

    }

    private void checkBlueConnect() {
        if(mSppBluetoothManager.getState() != SppBluetoothManager.STATE_CONNECTED){
            Log.d(TAG,"蓝牙设备未连接");
            Toast.makeText(DevTranslateActivity.this,"请连接设备",Toast.LENGTH_SHORT).show();

        }else{
            Log.d(TAG,"蓝牙设备已连接");
        }
    }

    private void addListener() {
        mDevMenu.setOnClickListener(this);
        mVoiceImage.setOnClickListener(this);
    }

    private void initView() {
        mListViewDevTran = (ListView) findViewById(R.id.lv_dev_tran);
        mVoiceImage = (ImageView) findViewById(R.id.dev_voice);
        mDevMenu = (ImageView)findViewById(R.id.dev_menu);
        mRecorderList = new ArrayList<RecorderBean>();
        mDevTranListViewAdapter = new DevTranListViewAdapter(this, mRecorderList, new DevTranListViewAdapter.PlayVoceClick() {
            @Override
            public void click(View v) {
                int pos =  (Integer) v.getTag();
                Log.d(TAG,pos+" 点击按钮位置");
                mAudioMediaPlayManager.startPlayingUsePhone(mRecorderList.get(pos).getmFilePath());
            }
        });
        mListViewDevTran.setAdapter(mDevTranListViewAdapter);
        leftLanguage = SpUtils.getString(this,Constants.LEFT_LANGUAGE,Constants.zh_CN);
        rightLanguage = SpUtils.getString(this,Constants.RIGHT_LANGUAGE,Constants.en_US);
        // mVoiceFile = Utils.createFile(mVoiceFile, "VOICE_", ".3pg");
       /* mVoiceImage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Log.d(TAG, "按钮按下");
                       *//* File voiceFile;
                        if (Utils.existSDCard())
                            voiceFile = new File(Environment.getExternalStorageDirectory(), "/voice");
                        else voiceFile = Environment.getDataDirectory();
                        voiceFile = Utils.createFile(voiceFile, "voice_", ".3pg");
                        // mVoiceFilePath = Environment.getExternalStorageState()+ File.separator + System.currentTimeMillis() + ".3pg";*//*
                        // TODO: 2018/1/17 判断是否连接？
                        mVoiceFilePath = Utils.getVoiceFilePath();
                        Log.d(TAG, "文件名和路径 " + mVoiceFilePath);
                       // mAudioMediaPlayManager.star(mVoiceFilePath);
                        break;
                    case MotionEvent.ACTION_UP:
                        Log.d(TAG, "按钮松开");
                        mAudioMediaPlayManager.stopRecorderUsePhone();
                        break;
                }
                return false;
            }
        });*/
        /*mBTOpenDialog = new BTOpenDialog(this,R.layout.bt_open_dialog,new int[]{R.id.dialog_cancel, R.id.dialog_sure});
        mBTOpenDialog.setOnCenterItemClickListener(new BTOpenDialog.OnCenterItemClickListener() {
            @Override
            public void OnCenterItemClick(BTOpenDialog dialog, View view) {
                switch (view.getId()) {
                    case R.id.dialog_sure:
                        Toast.makeText(DevTranslateActivity.this, "确定按钮", Toast.LENGTH_SHORT).show();
                        //mSppBluetoothManager.openBluetooth();
                        Intent intent = new Intent(Settings.ACTION_BLUETOOTH_SETTINGS);
                        startActivityForResult(intent, Constants.SETTING_BLUE);
                        break;
                    default:
                        break;
                }
            }
        });*/
    }

    @Override
    public void onStartRecoderUseBluetoothEar() {

        Log.d(TAG, "录音结束");
       /* RecorderBean recorderBean = new RecorderBean();
        recorderBean.setmFilePath(mVoiceFilePath);
        recorderBean.setType(Constants.ITEM_RIGHT);
        mRecorderList.add(recorderBean);
        mDevTranListViewAdapter.notifyDataSetChanged();*/
    }

    @Override
    public void onStopRecoderUseBluetoothEar() {

    }

    @Override
    public void onStartPlayUseBluetoothEar() {

    }

    @Override
    public void onStopPlayuseBluetoothEar() {

    }

    @Override
    public void onStartRecoderUsePhone() {

    }

    @Override
    public void onStopRecoderUsePhone() {

    }

    @Override
    public void onStartPlayUsePhone() {

    }

    @Override
    public void onStopPlayUsePhone() {
        //向服务器发送请求，获取翻译结果及字符返回至手机端
        Log.d(TAG, "录音结束");

    }

    //指令接收
    @Override
    public void onPlayCompletion() {
        Log.d(TAG," onPlayCompletion");
    }

    @Override
    public void onPlayError() {
        Log.d(TAG," onPlayError");
    }


    @Override
    public void onDevicePressedStateListener(int type) {
        Log.d(TAG,type+" onDevicePressedStateListener");
    }

    @Override
    public void onDeviceReceiverTextMessageListener(String msg) {
        Log.d(TAG,msg+" onDeviceReceiverTextMessageListener");
    }


    @Override
    public void writeByteToOtherDeviceSuccess() {

    }

    @Override
    public void writeByteToOtherDeviceFailed() {

    }

    @Override
    public void onClick(View v) {
        int id =v.getId();
        switch (id){
            case R.id.dev_menu:
                Intent LanguageSelectIntent = new Intent(DevTranslateActivity.this,LanguageSelectActivity.class);
                LanguageSelectIntent.putExtra(Constants.LANGUAGE_SELECT_TYPE,Constants.LANGUAGE_SELECT_DEV_TYPE);
                LanguageSelectIntent.putExtra(Constants.DETRECT,Constants.DETRECT_LEFT);
                startActivityForResult(LanguageSelectIntent,Constants.LANGUAGE_CHANGE);
                break;
            case R.id.dev_voice:

                mMicroRecogitionManager.initSpeechRecognition(rightLanguage);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG,"result : "+resultCode);
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
    /**************微软录音***************/
    @Override
    public void onFinalResponseResult(String result) {
            Log.d(TAG,"微软语音返回结果:"+result);
    }

    @Override
    public void ononFinalResponseResultEmtity(String error) {

    }
}
