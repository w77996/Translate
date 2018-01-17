package com.wtwd.translate.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.wtwd.translate.R;
import com.wtwd.translate.adapter.DevTranListViewAdapter;
import com.wtwd.translate.bean.Recorder;
import com.wtwd.translate.bean.RecorderBean;
import com.wtwd.translate.utils.Constants;
import com.wtwd.translate.utils.Utils;
import com.wtwd.translate.utils.audio.AudioMediaPlayManager;
import com.wtwd.translate.utils.audio.AudioStateChange;

import java.io.File;
import java.security.Key;
import java.util.ArrayList;
import java.util.List;

/**
 * time:2017/12/27
 * Created by w77996
 */
public class DevTranslateActivity extends Activity implements AudioStateChange {

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

    List<RecorderBean> mRecorderList;
    DevTranListViewAdapter mDevTranListViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devtran);
        mAudioMediaPlayManager = AudioMediaPlayManager.getAudioMediaPlayManager(DevTranslateActivity.this);
        mAudioMediaPlayManager.setAudioStateChange(this);
        initView();
        addListener();

    }

    private void addListener() {

    }

    private void initView() {
        mListViewDevTran = (ListView) findViewById(R.id.lv_dev_tran);
        mVoiceImage = (ImageView) findViewById(R.id.dev_voice);

        mRecorderList = new ArrayList<RecorderBean>();
        mDevTranListViewAdapter = new DevTranListViewAdapter(this, mRecorderList);
        mListViewDevTran.setAdapter(mDevTranListViewAdapter);
        // mVoiceFile = Utils.createFile(mVoiceFile, "VOICE_", ".3pg");
        mVoiceImage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Log.d(TAG, "按钮按下");
                        File voiceFile;
                        if (Utils.existSDCard())
                            voiceFile = new File(Environment.getExternalStorageDirectory(), "/voice");
                        else voiceFile = Environment.getDataDirectory();
                        voiceFile = Utils.createFile(voiceFile, "voice_", ".3pg");
                        // mVoiceFilePath = Environment.getExternalStorageState()+ File.separator + System.currentTimeMillis() + ".3pg";
                        mVoiceFilePath = voiceFile.getAbsolutePath();
                        Log.d(TAG, "文件名和路径 " + mVoiceFilePath);
                        mAudioMediaPlayManager.startRecorderUsePhone(voiceFile.getAbsolutePath());
                        break;
                    case MotionEvent.ACTION_UP:
                        Log.d(TAG, "按钮松开");
                        mAudioMediaPlayManager.stopRecorderUsePhone();
                        break;
                }
                return false;
            }
        });
    }

    @Override
    public void onStartRecoderUseBluetoothEar() {

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
       Log.d(TAG, "录音结束");
         RecorderBean recorderBean = new RecorderBean();
        recorderBean.setmFilePath(mVoiceFilePath);
        recorderBean.setType(Constants.ITEM_RIGHT);
        mRecorderList.add(recorderBean);
        mDevTranListViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void onStartPlayUsePhone() {

    }

    @Override
    public void onStopPlayUsePhone() {
        //向服务器发送请求，获取翻译结果及字符返回至手机端
        Log.d(TAG, "录音结束");

    }

    @Override
    public void onPlayCompletion() {

    }

    @Override
    public void onPlayError() {

    }


}
