package com.wtwd.translate.activity;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.wtwd.translate.R;
import com.wtwd.translate.adapter.DevTranListViewAdapter;
import com.wtwd.translate.bean.RecorderBean;
import com.wtwd.translate.bean.TranResultBean;
import com.wtwd.translate.utils.BluetoothSerialString;
import com.wtwd.translate.utils.Constants;
import com.wtwd.translate.utils.GsonUtils;
import com.wtwd.translate.utils.SpUtils;
import com.wtwd.translate.utils.Utils;
import com.wtwd.translate.utils.audio.AudioMediaPlayManager;
import com.wtwd.translate.utils.audio.AudioStateChange;
import com.wtwd.translate.utils.blue.SppBluetoothManager;
import com.wtwd.translate.utils.blue.SppBluetoothReceivedManager;
import com.wtwd.translate.utils.blue.TranProtocalAnalysis;
import com.wtwd.translate.utils.blue.TranProtocalReceivedAnalysis;
import com.wtwd.translate.utils.micro.MicroRecogitionManager;
import com.wtwd.translate.view.BTOpenDialog;

import java.io.File;
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
    TranProtocalReceivedAnalysis mTranProtocalReceivedAnalysis;
    List<RecorderBean> mRecorderList;//语音list
    DevTranListViewAdapter mDevTranListViewAdapter;

    SppBluetoothManager mSppBluetoothManager;
    SppBluetoothReceivedManager mSppBluetoothReceivedManager;
    MicroRecogitionManager mMicroRecogitionManager;

   // SppBluetoothMessagerManager mSppBluetoothMessagerManager;

    String leftLanguage;
    String rightLanguage;
    private String blueVoiceFile = null;

    boolean isDevRecrod;
    boolean isPhoneRecrod;
    private RecorderBean leftRecorderBean;
    private RecorderBean rightRecorderBean;

    private boolean isCreate = false;


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devtran);

        //isCreate = true;
        //录音和播放
        mAudioMediaPlayManager = AudioMediaPlayManager.getAudioMediaPlayManager(this);
        mAudioMediaPlayManager.setAudioStateChange(this);

        //spp蓝牙连接
        mSppBluetoothManager = SppBluetoothManager.getInstance(this);
       // mSppBluetoothManager.setBluetoothReceiverMessageListener(this);
        mSppBluetoothManager.setBluetoothListener(this);

        mSppBluetoothReceivedManager = SppBluetoothReceivedManager.getInstance(this);
        mSppBluetoothManager.setBluetoothListener(this);
        //微软
        mMicroRecogitionManager = MicroRecogitionManager.getMicroRecogitionManager(this);
        mMicroRecogitionManager.setmicroRecogitionManagerCallBack(this);
        //协议解析
        mTranProtocalAnalysis = TranProtocalAnalysis.getTranProtocalAnalysis(this);
        mTranProtocalAnalysis.setOnDevicePressedStateListener(this);

        mTranProtocalReceivedAnalysis = TranProtocalReceivedAnalysis.getTranProtocalAnalysis(this);
        mTranProtocalReceivedAnalysis.setOnDevicePressedStateListener(new TranProtocalReceivedAnalysis.OnDeviceButtonPressedStateListener() {
            @Override
            public void onDevicePressedStateListener(int type) {
                Log.e(TAG,type+"");
                Log.d(TAG,type+" onDevicePressedStateListener");
                /*if(DevTranslateActivity.this.){
                    Log.e(TAG,"界面未显示");
                    return;
                }*/
               /* if(type == TranProtocalAnalysis.BUTTON_F2_PRESSED ){
                    blueVoiceFile = Utils.getVoiceFilePath();
                    leftRecorderBean.setmFilePath(blueVoiceFile);
                    Log.e(TAG,"BUTTON_F2_PRESSED ");
                    mAudioMediaPlayManager.startRecorderUseBluetoothEar(blueVoiceFile);
                   // mMicroRecogitionManager.initSpeechRecognition(Constants.zh_CN);
                }else if(type == TranProtocalAnalysis.BUTTON_F2_RELEASED){
                    mAudioMediaPlayManager.stopRecorderUseBluetoothEar();
                    Log.e(TAG,"BUTTON_F2_RELEASED ");
                    // TODO: 2018/1/23
                    // 1.语音转文字;2.文字发送服务器;3.返回结果显示界面
                    mMicroRecogitionManager.initFileRecognition(leftLanguage,blueVoiceFile);
                }else if(type == TranProtocalAnalysis.BUTTON_F3_PRESSED){
                    Log.e(TAG,"BUTTON_F3_PRESSED ");
                    blueVoiceFile = Utils.getVoiceFilePath();
                    leftRecorderBean.setmFilePath(blueVoiceFile);
                    mAudioMediaPlayManager.startRecorderUseBluetoothEar(blueVoiceFile);
                   // mMicroRecogitionManager.initSpeechRecognition(Constants.zh_CN);
                }else if(type == TranProtocalAnalysis.BUTTON_F3_RELEASED){
                    Log.e(TAG,"BUTTON_F3_RELEASED ");
                    mAudioMediaPlayManager.stopRecorderUseBluetoothEar();
                    // TODO: 2018/1/23
                    mMicroRecogitionManager.initFileRecognition(leftLanguage,blueVoiceFile);
                }*/
              //  blueVoiceFile = Utils.getVoiceFilePath();
                //File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/a.3gp");
                //Log.e(TAG,file.getAbsolutePath());
                if(isPhoneRecrod == true){
                    Toast.makeText(DevTranslateActivity.this,R.string.dev_isrecoder,Toast.LENGTH_SHORT).show();
                    return;
                }
                if(type == TranProtocalAnalysis.BUTTON_F2_RELEASED ){
                   // Log.d(TAG,"蓝牙耳机录音文件路径为："+blueVoiceFile);
                   // leftRecorderBean.setmFilePath(blueVoiceFile);
                    Log.e(TAG,"BUTTON_F2_PRESSED ");
                    //blueVoiceFile = Utils.getVoiceFilePath();
                    isDevRecrod = true;
                    //mAudioMediaPlayManager.startRecorderUseBluetoothEar(mFileName);
                     mMicroRecogitionManager.initSpeechRecognition(leftLanguage);
                     leftRecorderBean=new RecorderBean();
                     leftRecorderBean.setType(Constants.ITEM_LEFT);
                     leftRecorderBean.setLanguage_type(leftLanguage);
                }else if(type == TranProtocalAnalysis.BUTTON_F2_PRESSED){
                    mAudioMediaPlayManager.stopRecorderUseBluetoothEar();
                    Log.e(TAG,"BUTTON_F2_RELEASED ");
                    // TODO: 2018/1/23
                    // 1.语音转文字;2.文字发送服务器;3.返回结果显示界面
                }else if(type == TranProtocalAnalysis.BUTTON_F3_RELEASED){
                    Log.d(TAG,"蓝牙耳机录音文件路径为："+blueVoiceFile);
                    Log.e(TAG,"BUTTON_F3_PRESSED ");
                   // blueVoiceFile = Utils.getVoiceFilePath();
                    isDevRecrod = true;
                   // leftRecorderBean.setmFilePath(blueVoiceFile);
                    //mAudioMediaPlayManager.startRecorderUseBluetoothEar(mFileName);
                     mMicroRecogitionManager.initSpeechRecognition(leftLanguage);
                    leftRecorderBean=new RecorderBean();
                    leftRecorderBean.setType(Constants.ITEM_LEFT);
                    leftRecorderBean.setLanguage_type(leftLanguage);
                }else if(type == TranProtocalAnalysis.BUTTON_F3_PRESSED){
                    Log.e(TAG,"BUTTON_F3_RELEASED ");
                    mAudioMediaPlayManager.stopRecorderUseBluetoothEar();
                    // TODO: 2018/1/23

                }
                Log.d(TAG,"蓝牙耳机录音文件路径为："+blueVoiceFile);
            }

            @Override
            public void onDeviceReceiverTextMessageListener(String msg) {

            }

            @Override
            public void writeByteToOtherDeviceSuccess() {

            }

            @Override
            public void writeByteToOtherDeviceFailed() {

            }
        });




        //连接spp协议
      /*  mSppBluetoothMessagerManager  = SppBluetoothMessagerManager.getInstance(this);
        mSppBluetoothMessagerManager.setBluetoothListener(mSppMsgBluetoothListener);*/
        initView();
        checkBlueConnect();


        addListener();

    }

    private void checkBlueConnect() {
        if(mSppBluetoothManager.getState() != SppBluetoothManager.STATE_CONNECTED || mSppBluetoothReceivedManager.getState() != SppBluetoothReceivedManager.STATE_CONNECTED|| !mSppBluetoothManager.enableBluetooth()){

            Log.d(TAG,"蓝牙设备未连接");
            mVoiceImage.setClickable(false);
            Toast.makeText(DevTranslateActivity.this,R.string.connect_dev,Toast.LENGTH_SHORT).show();
        }else{
            Log.d(TAG,"蓝牙设备已连接");
            /*String str = BluetoothSerialString.getTranslationResultString("zh-CN", "en-US", "今天天气不错。", "Today's weather is fine.", true);
            //String  =" {"src":"zh-CN","des":"en-US","rec":"今天天气不错。","tra":"Today's weather is fine."}";
            Log.e(TAG, "str: " + str);*/
           // mTranProtocalAnalysis.writeToDevice(str);
        }
    }

    /**
     * 添加监听
     */
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
                if (mRecorderList.get(pos).getType() == Constants.ITEM_LEFT){
                    mAudioMediaPlayManager.startPlayingUsePhone(mRecorderList.get(pos).getmFilePath());
                }else if (mRecorderList.get(pos).getType() == Constants.ITEM_RIGHT){
                    mAudioMediaPlayManager.startPlayingUseBluetoothEar(mRecorderList.get(pos).getmFilePath());
                }

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
       // mMicroRecogitionManager.initSpeechRecognition(leftLanguage);
        Log.e(TAG, "onStartRecoderUseBluetoothEar");
       /* RecorderBean recorderBean = new RecorderBean();
        recorderBean.setmFilePath(mVoiceFilePath);
        recorderBean.setType(Constants.ITEM_RIGHT);
        mRecorderList.add(recorderBean);
        mDevTranListViewAdapter.notifyDataSetChanged();*/
    }

    @Override
    public void onStopRecoderUseBluetoothEar() {
        Log.e(TAG, "onStopRecoderUseBluetoothEar");
       // mMicroRecogitionManager.initFileRecognition(leftLanguage,blueVoiceFile);
        //mMicroRecogitionManager.initFileRecognition(leftLanguage,blueVoiceFile);
    }

    @Override
    public void onStartPlayUseBluetoothEar() {
        Log.e(TAG,"onStartPlayUseBluetoothEar");
    }

    @Override
    public void onStopPlayuseBluetoothEar() {
        Log.e(TAG,"onStopPlayuseBluetoothEar");

    }

    @Override
    public void onStartRecoderUsePhone() {
        Log.e(TAG,"onStartRecoderUsePhone");

    }

    @Override
    public void onStopRecoderUsePhone() {
        Log.e(TAG,"onStopRecoderUsePhone");

    }

    @Override
    public void onStartPlayUsePhone() {
        Log.e(TAG,"onStartPlayUsePhone");
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
               Log.e(TAG,"用户录音");
                if(mSppBluetoothManager.getState() != SppBluetoothManager.STATE_CONNECTED || mSppBluetoothReceivedManager.getState() != SppBluetoothReceivedManager.STATE_CONNECTED|| !mSppBluetoothManager.enableBluetooth()){

                    Log.d(TAG,"蓝牙设备未连接");
                    Toast.makeText(DevTranslateActivity.this, R.string.connect_dev,Toast.LENGTH_SHORT).show();
                    return;
                }
                if(isDevRecrod == true){
                    Toast.makeText(DevTranslateActivity.this, R.string.dev_isrecoder,Toast.LENGTH_SHORT).show();
                    return ;
                }
                isPhoneRecrod = true;
                rightRecorderBean = new RecorderBean();
                rightRecorderBean.setType(Constants.ITEM_RIGHT);
                rightRecorderBean.setLanguage_type(rightLanguage);
               // mAudioMediaPlayManager.startRecorderUsePhone();
                mMicroRecogitionManager.initSpeechRecognition(rightLanguage);
                break;
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG,"result : "+resultCode);
        if(resultCode == Constants.LANGUAGE_CHANGE){
            leftLanguage = SpUtils.getString(this,Constants.LEFT_LANGUAGE,Constants.zh_CN);
            rightLanguage = SpUtils.getString(this,Constants.RIGHT_LANGUAGE,Constants.en_US);
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
    /**************微软录音***************/
    @Override
    public void onFinalResponseResult(String result) {
            Log.d(TAG,"微软语音返回结果:"+result);
            if(result == null || "".equals(result)){
                Toast.makeText(this,R.string.record_fail,Toast.LENGTH_SHORT).show();
                return;
            }

            //RecorderBean recorderBean = new RecorderBean();
            if(isDevRecrod == true){
                // TODO: 2018/1/24 1.文字传给服务器2.返回结果显示在界面上
                leftRecorderBean.setmRecorderTxt(result);
                leftRecorderBean.setType(Constants.ITEM_LEFT);
                requestTran(result,leftLanguage,rightLanguage);
            }else if(isPhoneRecrod == true){
                rightRecorderBean.setmRecorderTxt(result);
                rightRecorderBean.setType(Constants.ITEM_RIGHT);
                requestTran(result,rightLanguage,leftLanguage);
                //String str = BluetoothSerialString.getTranslationResultString(leftLanguage,rightLanguage,result,result,true);
               // mTranProtocalAnalysis.writeToDevice(str);
            }

    }

    @Override
    public void ononFinalResponseResultEmtity(String error) {
        //if(result == null || "".equals(result)){
        Toast.makeText(this,R.string.record_fail,Toast.LENGTH_SHORT).show();
        isDevRecrod = false;
        isPhoneRecrod = false;
           // return;
        //}
    }

    @Override
    public void onError(String s) {
        Toast.makeText(this,R.string.record_fail,Toast.LENGTH_SHORT).show();
        isDevRecrod = false;
        isPhoneRecrod = false;
    }
   /* @Override
    public void onMicroStartRecoderUseBluetoothEar(){
        Log.e(TAG,"onMicroStartRecoderUseBluetoothEar");

    }*/
   private void requestTran(String trandata,String from ,String to){
       OkGo.<String>post(Constants.BASEURL+Constants.TEXTTRANSLATE)
               .tag(this)
               .params("text",trandata)
               .params("from",from)
               .params("to",to)
               .params("guestId",1)
               .execute(new StringCallback() {
                   @Override
                   public void onSuccess(Response<String> response) {
                       Log.e(TAG,response.body().toString());
                       TranResultBean resultBean = GsonUtils.getInstance().GsonToBean(response.body().toString(),TranResultBean.class);
                       if(resultBean.getStatus() == Constants.REQUEST_SUCCESS){
                           Log.e(TAG,"请求成功");

                           String tranText = resultBean.getTranslateResult().getText();
                           String tranAudio = resultBean.getTranslateResult().getAudio();
                           if(!TextUtils.isEmpty(tranText)&& !TextUtils.isEmpty(tranAudio)){
                               Log.e(TAG,tranText + " "+tranAudio);
                               if(isDevRecrod){
                                   leftRecorderBean.setmResultTxt(tranText);
                                   leftRecorderBean.setmFilePath(resultBean.getTranslateResult().getAudio());
                                   mRecorderList.add(leftRecorderBean);
                                   isDevRecrod = false;
                               }else if(isPhoneRecrod){
                                   rightRecorderBean.setmResultTxt(tranText);
                                   rightRecorderBean.setmFilePath(resultBean.getTranslateResult().getAudio());
                                   mRecorderList.add(rightRecorderBean);
                                   isPhoneRecrod = false;
                               }
                              // mRecorderList.add(rightRecorderBean);
                               mDevTranListViewAdapter.notifyDataSetChanged();
                               mListViewDevTran.setSelection(mRecorderList.size()-1);
                               if(isPhoneRecrod){
                                   mAudioMediaPlayManager.startPlayingUseBluetoothEar(tranAudio);
                               }else if(isDevRecrod){
                                   mAudioMediaPlayManager.startPlayingUsePhone(tranAudio);
                               }

                               isDevRecrod = false;
                               isPhoneRecrod = false;
                           }
                       }else if(resultBean.getStatus() == Constants.REQUEST_FAIL){
                           Log.e(TAG,"请求失败");
                       }
                   }

                   @Override
                   public void onError(Response<String> response) {
                       super.onError(response);
                       isDevRecrod = false;
                       isPhoneRecrod = false;
                   }
               });
   }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //isCreate = false;
    }
}
