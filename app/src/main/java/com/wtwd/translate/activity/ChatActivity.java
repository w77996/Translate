package com.wtwd.translate.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.microsoft.cognitiveservices.speechrecognition.DataRecognitionClient;
import com.microsoft.cognitiveservices.speechrecognition.ISpeechRecognitionServerEvents;
import com.microsoft.cognitiveservices.speechrecognition.MicrophoneRecognitionClient;
import com.microsoft.cognitiveservices.speechrecognition.RecognitionResult;
import com.microsoft.cognitiveservices.speechrecognition.RecognitionStatus;
import com.microsoft.cognitiveservices.speechrecognition.SpeechRecognitionMode;
import com.microsoft.cognitiveservices.speechrecognition.SpeechRecognitionServiceFactory;
import com.wtwd.translate.R;
import com.wtwd.translate.adapter.DevTranListViewAdapter;
import com.wtwd.translate.bean.Recorder;
import com.wtwd.translate.bean.RecorderBean;
import com.wtwd.translate.bean.TranResultBean;
import com.wtwd.translate.db.DaoUtils;
import com.wtwd.translate.utils.Constants;
import com.wtwd.translate.utils.GsonUtils;
import com.wtwd.translate.utils.SpUtils;
import com.wtwd.translate.utils.SpeechUtils;
import com.wtwd.translate.utils.Utils;
import com.wtwd.translate.utils.audio.AudioMediaPlayManager;
import com.wtwd.translate.utils.audio.AudioStateChange;
import com.wtwd.translate.utils.micro.MicroRecogitionManager;
import com.wtwd.translate.utils.permissions.PermissionsActivity;
import com.wtwd.translate.utils.permissions.PermissionsChecker;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


/**
 * time:2017/12/27
 * Created by w77996
 */
public class ChatActivity extends Activity implements View.OnClickListener, AudioStateChange,ISpeechRecognitionServerEvents {

    public final static String TAG = "ChatActivity";
    /**
     * 底部左录音按钮
     **/
    ImageButton mLeftVoiceImg;
    /**
     * 底部右录音按钮
     **/
    ImageButton mRightVoiceImg;
    // /**头部语言选择按钮**/
    // ImageView mDownImg;
    /**
     * 返回
     **/
    ImageView mBack;

    /**
     * 头部左侧语言选择栏
     **/
    TextView mLeftText;
    /**
     * 头部右侧侧语言选择栏
     **/
    TextView mRightText;
    /**
     * 头部的语言头像
     **/
    ImageView leftlanguage_head;
    ImageView rightlanguage_head;

    /**
     * 语音键
     **/
    ImageView mVoice;
    /**
     * 底部左侧国旗
     */
    ImageView chat_left_img;
    /**
     * 底部右侧国旗
     */
    ImageView chat_right_img;
    /**
     * 底部左侧linear
     */
    LinearLayout lin_left;
    /**
     * 底部右侧linear
     */
    LinearLayout lin_right;
    /**
     * 语言转换按钮
     */
    ImageView img_chat_switch;
    /**
     * 左侧txt
     */
    TextView chat_left_txt;
    /**
     * 右侧txt
     */
    TextView chat_right_txt;
    /**
     * 语音处理类
     */
    AudioMediaPlayManager mAudioMediaPlayManager;

    /**
     * 录音文件路径
     */
    String mVoiceFilePath;
    List<RecorderBean> mRecorderList;
    ListView mListViewChat;
    DevTranListViewAdapter mAdapter;
    /**
     * 互译的语言
     */
    String leftLanguage;
    String rightLanguage;

    /**
     * 按钮是否按下
     */
    boolean leftIsStartVoice = false;
    boolean rightIsStartVoice = false;
    private AnimationDrawable animationDrawable;
   // private AnimationDrawable animationPlay;
    private int lastPlaypos = 0;
    private boolean itemIsPlaying = false;
    private boolean isRecordering = false;//是否在录音记录
    private boolean isTranslate = false;//是否在翻译

    MicrophoneRecognitionClient micClient = null;
    FinalResponseStatus isReceivedResponse = FinalResponseStatus.NotReceived;

    String nowLanguage="";
    private ImageView itemview;


    public enum FinalResponseStatus { NotReceived, OK, Timeout }

    /**
     * 微软语音状态
     */
    //  FinalResponseStatus isReceivedResponse = FinalResponseStatus.NotReceived;
    private RecorderBean rightRecorderBean;
    private RecorderBean leftRecorderBean;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        mAudioMediaPlayManager = AudioMediaPlayManager.getAudioMediaPlayManager(this);
        mAudioMediaPlayManager.setAudioStateChange(this);
        /*mMicroRecogitionManager =MicroRecogitionManager.getMicroRecogitionManager(this);
        mMicroRecogitionManager.setmicroRecogitionManagerCallBack(this);*/

        initData();
        initView();
    }

  /*  @Override
    protected void onResume() {
        super.onResume();
        mPermissionsChecker = new PermissionsChecker(this);
        // 缺少权限时, 进入权限配置页面
        if (mPermissionsChecker.lacksPermissions(PERMISSIONS)) {
            startPermissionsActivity();
        }
    }*/

    private void initData() {

        //获取已选择的默认语言
        leftLanguage = SpUtils.getString(ChatActivity.this, Constants.LEFT_LANGUAGE, Constants.zh_CN);
        rightLanguage = SpUtils.getString(ChatActivity.this, Constants.RIGHT_LANGUAGE, Constants.en_US);

    }

    /**
     * 初始化界面控件
     */
    private void initView() {
        Utils.setWindowStatusBarColor(this, R.color.main_title_color);
        // mLeftVoiceImg = (ImageButton)findViewById(R.id.imgbtn_left);
        //  mRightVoiceImg = (ImageButton)findViewById(R.id.imgbtn_right);
        // mDownImg = (ImageView)findViewById(R.id.img_chat_down_row);
        mBack = (ImageView) findViewById(R.id.chat_back);
        mLeftText = (TextView) findViewById(R.id.text_chat_left_language);
        mRightText = (TextView) findViewById(R.id.text_chat_right_language);
        mVoice = (ImageView) findViewById(R.id.img_chat_voice);
        mVoice.setImageResource(R.drawable.chat_voice);
        chat_left_txt = (TextView) findViewById(R.id.chat_left_txt);
        chat_right_txt = (TextView) findViewById(R.id.chat_right_txt);

        if (leftLanguage.equals(Constants.zh_CN)) {
            chat_left_txt.setText("按下说话");
        } else {
            chat_left_txt.setText("Hold to talk");
        }
        if (rightLanguage.equals(Constants.zh_CN)) {
            chat_right_txt.setText("按下说话");
        } else {
            chat_right_txt.setText("Hold to talk");
        }
        mRecorderList = new ArrayList<>();
        mRecorderList.addAll(DaoUtils.getRecorderBeanDaoManager().QueryAll(RecorderBean.class));

        img_chat_switch = (ImageView) findViewById(R.id.img_chat_switch);
        chat_left_img = (ImageView) findViewById(R.id.chat_left_img);
        chat_right_img = (ImageView) findViewById(R.id.chat_right_img);
        lin_left = (LinearLayout) findViewById(R.id.lin_left);
        lin_right = (LinearLayout) findViewById(R.id.lin_right);

        leftlanguage_head = (ImageView) findViewById(R.id.leftlanguage_head);
        rightlanguage_head = (ImageView) findViewById(R.id.rightlanguage_head);
        Utils.perseLanguage(ChatActivity.this, leftLanguage, chat_left_img, mLeftText);
        Utils.perseLanguage(ChatActivity.this, rightLanguage, chat_right_img, mRightText);
        Utils.setLanguageHead(ChatActivity.this, leftlanguage_head, leftLanguage);
        Utils.setLanguageHead(ChatActivity.this, rightlanguage_head, rightLanguage);

        mListViewChat = (ListView) findViewById(R.id.lv_chat);
        //initSpeechRecognition();
        mAdapter = new DevTranListViewAdapter(this, mRecorderList, new DevTranListViewAdapter.PlayVoceClick() {

            @Override
            public void click(View v) {
                final int pos = (Integer) v.getTag();
               if(isRecordering){
                   Log.e(TAG,"itemIsPlaying isRecordering ");
                   return;
               }
                Log.d(TAG, pos + " 点击按钮位置");

                if (!mRecorderList.get(pos).getmFilePath().endsWith(".mp3") || "".equals(mRecorderList.get(pos).getmFilePath()) || mRecorderList.get(pos).getmFilePath() == null) {
                    Log.e(TAG, "语音合成出错！！！！！！");
                    return;
                } else {

                    Log.e(TAG, mRecorderList.get(pos).getmFilePath());

                    mAudioMediaPlayManager.startPlayingUsePhone(mRecorderList.get(pos).getmFilePath());


                }
                // mAudioMediaPlayManager.startPlayingUsePhone(mRecorderList.get(pos).getmFilePath());
                //SpeechUtils.getInstance(ChatActivity.this,Utils.setLocalLanguag(mRecorderList.get(pos).getLanguage_type())).speakText(mRecorderList.get(pos).getmResultTxt());
            }
        });

        mListViewChat.setAdapter(mAdapter);
        if(mRecorderList.size() <= 0 || mRecorderList == null ){
            Log.e(TAG,mRecorderList+" 为空");
        }else{
            Log.e(TAG,"不为空 size"+mRecorderList.size());

            mListViewChat.setSelection(mRecorderList.size() - 1);
        }
      /* mListViewChat.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {


                DaoUtils.getRecorderBeanDaoManager().deleteObject(mRecorderList.get(position));
                mRecorderList.remove(position);
                mAdapter.notifyDataSetChanged();
                Log.e(TAG,"删除对象");
                return false;
            }
        });*/
        //  mLeftVoiceImg.setOnClickListener(this);
        //   mRightVoiceImg.setOnClickListener(this);
        //mDownImg.setOnClickListener(this);
        mBack.setOnClickListener(this);
        mLeftText.setOnClickListener(this);
        mRightText.setOnClickListener(this);
        mVoice.setOnClickListener(this);
        img_chat_switch.setOnClickListener(this);
        lin_left.setOnClickListener(this);
        lin_right.setOnClickListener(this);
        leftlanguage_head.setOnClickListener(this);
        rightlanguage_head.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        Intent mLanguageSelectLeftIntent;
        Intent mLanguageSelectRightIntent;
        switch (v.getId()) {
            case R.id.lin_left:
               /* if(rightBtnPress){
                    rightBtnPress = false;
                    mAudioMediaPlayManager.stopRecorderUsePhone();
                    lin_right.setBackground(this.getDrawable(R.drawable.chat_right_btn_selected));
                }*/

               if (!Utils.isNetworkConnected(this)) {
                    Toast.makeText(this, R.string.no_network, Toast.LENGTH_SHORT).show();
                    return;
                }
                if(isRecordering == true){
                    Log.e(TAG,"左侧点击，正在录音");
                    Toast.makeText(ChatActivity.this,R.string.dev_isrecoder,Toast.LENGTH_SHORT).show();
                    return;
                }
                if(isTranslate == true){
                    Log.e(TAG,"左侧翻译中");
                    Toast.makeText(ChatActivity.this,R.string.translating,Toast.LENGTH_SHORT).show();
                    return;
                }
                if (leftIsStartVoice) {
                    // mAudioMediaPlayManager.stopRecorderUsePhone();
                    /*leftIsStartVoice = false;
                    lin_right.setClickable(true);
                    lin_left.setBackground(this.getDrawable(R.drawable.chat_left_btn_unselect));

                    // TODO: 2018/tran_voice1/17  停止录音后更新界面处理
                    RecorderBean recorderBean = new RecorderBean();
                    recorderBean.setLanguage_type(leftLanguage);
                    recorderBean.setmFilePath(mVoiceFilePath);
                    //initSpeechRecognition(mVoiceFilePath);
                    recorderBean.setType(Constants.ITEM_LEFT);
                    mRecorderList.add(recorderBean);
                    mAdapter.notifyDataSetChanged();
                    mListViewChat.setSelection(mRecorderList.size()-tran_voice1);*/
                } else {
                    // leftBtnPress = true;
                    //mVoiceFilePath = Utils.getVoiceFilePath();
                    //Log.d(TAG, "左侧录音 " );
                    //mAudioMediaPlayManager.startRecorderUsePhone();;
                    //  mAudioMediaPlayManager.startRecorderUsePhone(mVoiceFilePath);
                    lin_right.setClickable(false);
                    lin_left.setClickable(false);
                    initSpeechRecognition(leftLanguage);
                    isRecordering = true;
                    mVoice.setImageResource(R.drawable.voice_tran_animation);
                    animationDrawable = (AnimationDrawable) mVoice.getDrawable();
                    animationDrawable.start();
                    Log.d(TAG, "左侧开始录音");
                    lin_left.setBackground(this.getDrawable(R.drawable.chat_left_btn_selected));

                    leftIsStartVoice = true;
                    Log.e(TAG,"leftIsStartVoice");

                }

                break;
            case R.id.lin_right:
               /* if(leftBtnPress){
                    leftBtnPress = false;
                    mAudioMediaPlayManager.stopRecorderUsePhone();
                    leftIsStartVoice;
                }*/
                if (!Utils.isNetworkConnected(this)) {
                    Toast.makeText(this, R.string.no_network, Toast.LENGTH_SHORT).show();
                    return;
                }
                if(isRecordering==true){
                    Log.e(TAG,"右侧正在录音");
                    Toast.makeText(ChatActivity.this,R.string.dev_isrecoder,Toast.LENGTH_SHORT).show();
                    return;
                }
                if(isTranslate == true){
                    Log.e(TAG,"右侧翻译中");
                    Toast.makeText(ChatActivity.this,R.string.translating,Toast.LENGTH_SHORT).show();
                    return;
                }

                if (rightIsStartVoice == true) {

                   /* rightIsStartVoice = false;
                    lin_left.setClickable(true);
                    lin_right.setBackground(this.getDrawable(R.drawable.chat_right_btn_unselect));

                    // TODO: 2018/tran_voice1/17  停止录音后更新界面处理
                    RecorderBean recorderBean = new RecorderBean();
                    recorderBean.setLanguage_type(rightLanguage);
                    recorderBean.setmFilePath(mVoiceFilePath);

                    recorderBean.setType(Constants.ITEM_RIGHT);
                    mRecorderList.add(recorderBean);
                    mAdapter.notifyDataSetChanged();
                    mListViewChat.setSelection(mRecorderList.size()-tran_voice1);*/
                    //mListViewChat.s
                } else {
                    // leftBtnPress = true;
                   // mVoiceFilePath = Utils.getVoiceFilePath();
                   // Log.d(TAG, "右侧录音路径 " + mVoiceFilePath);
                    //mAudioMediaPlayManager.startRecorderUsePhone(mVoiceFilePath);
                    Log.e(TAG,"right click");
                    isRecordering = true;
                    lin_right.setClickable(false);
                    lin_left.setClickable(false);
                   initSpeechRecognition(rightLanguage);
                    mVoice.setImageResource(R.drawable.voice_tran_animation);
                    animationDrawable = (AnimationDrawable) mVoice.getDrawable();
                    animationDrawable.start();
                    Log.d(TAG, "右侧开始录音");
                    lin_right.setBackground(this.getDrawable(R.drawable.chat_right_btn_selected));
                    rightIsStartVoice = true;

                }
                break;
            /*case R.id.img_chat_down_row:
                Intent mLanguageSelectIntent = new Intent(this,LanguageSelectActivity.class);
                mLanguageSelectIntent.putExtra("derect",0);
                startActivityForResult(mLanguageSelectIntent,Constants.LANGUAGE_CHANGE);
                break;*/
            case R.id.chat_back:
                finish();
                break;
            case R.id.text_chat_left_language:
                mLanguageSelectLeftIntent = new Intent(this, LanguageSelectActivity.class);
                mLanguageSelectLeftIntent.putExtra(Constants.LANGUAGE_SELECT_TYPE, Constants.LANGUAGE_SELECT_NORMAL_TYPE);
                mLanguageSelectLeftIntent.putExtra(Constants.DETRECT, Constants.DETRECT_LEFT);
                startActivityForResult(mLanguageSelectLeftIntent, Constants.LANGUAGE_CHANGE);
                break;
            case R.id.text_chat_right_language:
                mLanguageSelectRightIntent = new Intent(this, LanguageSelectActivity.class);
                mLanguageSelectRightIntent.putExtra(Constants.LANGUAGE_SELECT_TYPE, Constants.LANGUAGE_SELECT_NORMAL_TYPE);
                mLanguageSelectRightIntent.putExtra(Constants.DETRECT, Constants.DETRECT_RIGHT);
                startActivityForResult(mLanguageSelectRightIntent, Constants.LANGUAGE_CHANGE);
                break;
            case R.id.leftlanguage_head:
                mLanguageSelectLeftIntent = new Intent(this, LanguageSelectActivity.class);
                mLanguageSelectLeftIntent.putExtra(Constants.LANGUAGE_SELECT_TYPE, Constants.LANGUAGE_SELECT_NORMAL_TYPE);
                mLanguageSelectLeftIntent.putExtra(Constants.DETRECT, Constants.DETRECT_LEFT);
                startActivityForResult(mLanguageSelectLeftIntent, Constants.LANGUAGE_CHANGE);
                break;
            case R.id.rightlanguage_head:
                mLanguageSelectRightIntent = new Intent(this, LanguageSelectActivity.class);
                mLanguageSelectRightIntent.putExtra(Constants.LANGUAGE_SELECT_TYPE, Constants.LANGUAGE_SELECT_NORMAL_TYPE);
                mLanguageSelectRightIntent.putExtra(Constants.DETRECT, Constants.DETRECT_RIGHT);
                startActivityForResult(mLanguageSelectRightIntent, Constants.LANGUAGE_CHANGE);
                break;
            case R.id.img_chat_voice:
                break;
            case R.id.img_chat_switch:
                leftLanguage = SpUtils.getString(ChatActivity.this, Constants.LEFT_LANGUAGE, Constants.zh_CN);
                rightLanguage = SpUtils.getString(ChatActivity.this, Constants.RIGHT_LANGUAGE, Constants.en_US);
                Log.e(TAG,"leftLanguage"+leftLanguage);
                Log.e(TAG,"rightLanguage"+rightLanguage);
                SpUtils.putString(ChatActivity.this, Constants.LEFT_LANGUAGE, rightLanguage);
                SpUtils.putString(ChatActivity.this, Constants.RIGHT_LANGUAGE, leftLanguage);

                leftLanguage = SpUtils.getString(ChatActivity.this, Constants.LEFT_LANGUAGE, Constants.zh_CN);
                rightLanguage = SpUtils.getString(ChatActivity.this, Constants.RIGHT_LANGUAGE, Constants.en_US);

                Log.e(TAG,"l "+leftLanguage);
                Log.e(TAG,"r "+rightLanguage);
                Utils.perseLanguage(ChatActivity.this, leftLanguage, chat_left_img, mLeftText);
                Utils.perseLanguage(ChatActivity.this, rightLanguage, chat_right_img, mRightText);
                Utils.setLanguageHead(ChatActivity.this, leftlanguage_head, leftLanguage);
                Utils.setLanguageHead(ChatActivity.this, rightlanguage_head, rightLanguage);
                break;
        }
    }

    /**
     * 微软语音
     * @param mLanguage
     */
    public void initSpeechRecognition(String mLanguage) {
        Log.e(TAG,"进入 initSpeechRecognition "+ mLanguage);
        if(!nowLanguage.equals(mLanguage)&&this.micClient == null){
            Log.e(TAG,"!nowLanguage.equals(mLanguage)&&this.micClient == null");
            this.micClient = SpeechRecognitionServiceFactory.createMicrophoneClient(
                    this,
                    this.getMode(),
                    mLanguage,
                    this,
                    "91401e4bf24e4312bd5d50dd3a93628a");
            nowLanguage = mLanguage;
            this.micClient.setAuthenticationUri("");
        }else if(!nowLanguage.equals(mLanguage)&&this.micClient !=null){
            Log.e(TAG,"!nowLanguage.equals(mLanguage)&&this.micClient !=null");
            try {
                this.micClient.finalize();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
            this.micClient = null;
            this.micClient = SpeechRecognitionServiceFactory.createMicrophoneClient(
                    this,
                    this.getMode(),
                    mLanguage,
                    this,
                    "91401e4bf24e4312bd5d50dd3a93628a");
            nowLanguage = mLanguage;
            this.micClient.setAuthenticationUri("");
        }else if(nowLanguage.equals(mLanguage)&&this.micClient ==null){
            Log.e(TAG,"nowLanguage.equals(mLanguage)&&this.micClient ==null");
            this.micClient = SpeechRecognitionServiceFactory.createMicrophoneClient(
                    this,
                    this.getMode(),
                    mLanguage,
                    this,
                    "91401e4bf24e4312bd5d50dd3a93628a");
            nowLanguage = mLanguage;
            this.micClient.setAuthenticationUri("");
        }


        this.micClient.startMicAndRecognition();

    }

    private SpeechRecognitionMode getMode() {
        return SpeechRecognitionMode.ShortPhrase;
    }
    /**********蓝牙与本地录音回调*************/
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
        //initSpeechRecognition(mVoiceFilePath);

    }

    @Override
    public void onStopRecoderUsePhone() {

    }

    @Override
    public void onStartPlayUsePhone() {

    }

    @Override
    public void onStopPlayUsePhone() {
       /* itemIsPlaying = false;
        if(animationPlay !=null){
            animationPlay.stop();
            itemview.setImageResource(R.drawable.item_voice1);
        }*/

    }

    @Override
    public void onPlayCompletion() {
       /* itemIsPlaying = false;
        if(animationPlay !=null){
            animationPlay.stop();
            itemview.setImageResource(R.drawable.item_voice1);
        }*/


    }

    @Override
    public void onPlayError() {
       /* itemIsPlaying = false;
        if(animationPlay !=null){
            animationPlay.stop();
            itemview.setImageResource(R.drawable.item_voice1);
        }*/

    }


    /**********微软语音合成回调*************/

    int m_waitSeconds = 20;

    /**
     * 初始化微软文件发送语音合成
     */
   /* private void initSpeechRecognition() {
        *//*this.micClient = SpeechRecognitionServiceFactory.createMicrophoneClient(
                this,
                SpeechRecognitionMode.ShortPhrase ,
                "en-us",
                this,
                "c96c2a771c6e4548a24e269884889478");*//*
        this.dataClient = SpeechRecognitionServiceFactory.createDataClient(
                this,
                SpeechRecognitionMode.ShortPhrase,
                "en-us",
                this,
                "6d5a91fa9c614a33a681731279f2450c");
        this.dataClient.setAuthenticationUri("");
        //SendAudioHelper(filename);

    }*/

   /* private void initSpeechRecognition(String mLanguageType) {
        this.micClient = SpeechRecognitionServiceFactory.createMicrophoneClient(
                this,
                SpeechRecognitionMode.ShortPhrase,
                mLanguageType,
                this,
                "6d5a91fa9c614a33a681731279f2450c");
        this.micClient.setAuthenticationUri("");
        this.micClient.startMicAndRecognition();
        // this.micClient.
        // mHandler.sendEmptyMessageAtTime(MSG_AUDIO_START,2000);

    }*/


  /*  private void SendAudioHelper(String filename) {
        RecognitionTask doDataReco = new RecognitionTask(this.dataClient, SpeechRecognitionMode.ShortPhrase, filename);
        Log.d(TAG, "SendAudioHelper : " + filename);
        try {
            doDataReco.execute().get(m_waitSeconds, TimeUnit.SECONDS);
        } catch (Exception e) {
            doDataReco.cancel(true);
            isReceivedResponse = FinalResponseStatus.Timeout;
        }
    }*/




  /*  @Override
    public void onPartialResponseReceived(String s) {
        Log.e(TAG, "onPartialResponseReceived ： " + s);
    }

    @Override
    public void onFinalResponseReceived(RecognitionResult recognitionResult) {
        boolean isFinalDicationMessage = this.getMode() == SpeechRecognitionMode.LongDictation &&
                (recognitionResult.RecognitionStatus == RecognitionStatus.EndOfDictation ||
                        recognitionResult.RecognitionStatus == RecognitionStatus.DictationEndSilenceTimeout);
      *//*  if (null != this.micClient && (this.getMode() == SpeechRecognitionMode.ShortPhrase) || isFinalDicationMessage) {
            // we got the final result, so it we can end the mic reco.  No need to do this
            // for dataReco, since we already called endAudio() on it as soon as we were done
            // sending all the data.
            this.micClient.endMicAndRecognition();
        }*//*
      if(null!=this.micClient){
          Log.e(TAG,"micClient end");
          this.micClient.endMicAndRecognition();
          this.micClient = null;
      }
        Log.d(TAG, "********* Final n-BEST Results *********");
        for (int i = 0; i < recognitionResult.Results.length; i++) {
            Log.d(TAG, "[" + i + "]" + " Confidence=" + recognitionResult.Results[i].Confidence +
                    " Text=\"" + recognitionResult.Results[i].DisplayText + "\"");
        }
        // mAudioMediaPlayManager.stopRecorderUsePhone();

        animationDrawable.stop();
        //mVoice.setBackground(getResources().getDrawable(R.drawable.voice_img3));
        mVoice.setImageResource(R.drawable.chat_voice);
        if (recognitionResult.Results.length <= 0) {
            Log.e(TAG, "recognitionResult 长度为0识别失败");
            lin_right.setBackground(this.getDrawable(R.drawable.chat_right_btn_unselect));
            lin_left.setBackground(this.getDrawable(R.drawable.chat_left_btn_unselect));
            lin_left.setClickable(true);
            lin_right.setClickable(true);
            leftIsStartVoice = false;
            rightIsStartVoice = false;
            Toast.makeText(this, R.string.record_fail, Toast.LENGTH_SHORT).show();
            return;
        }
        String userRecoderTxt = recognitionResult.Results[0].DisplayText;
        //更新界面
        if (rightIsStartVoice) {


            lin_left.setClickable(true);
            lin_right.setBackground(this.getDrawable(R.drawable.chat_right_btn_unselect));

            // TODO: 2018/tran_voice1/17  停止录音后更新界面处理
            rightRecorderBean = new RecorderBean();
            rightRecorderBean.setLanguage_type(rightLanguage);
            //rightRecorderBean.setmFilePath(mVoiceFilePath);

            rightRecorderBean.setmRecorderTxt(userRecoderTxt);
            rightRecorderBean.setType(Constants.ITEM_RIGHT);
            requestTran(userRecoderTxt, rightLanguage, leftLanguage);
            //mListViewChat.s
        } else if (leftIsStartVoice) {

            lin_right.setClickable(true);
            lin_left.setBackground(this.getDrawable(R.drawable.chat_left_btn_unselect));

            // TODO: 2018/tran_voice1/17  停止录音后更新界面处理
            leftRecorderBean = new RecorderBean();
            leftRecorderBean.setLanguage_type(leftLanguage);
            //leftRecorderBean.setmFilePath(mVoiceFilePath);
            //initSpeechRecognition(mVoiceFilePath);

            leftRecorderBean.setmRecorderTxt(userRecoderTxt);
            leftRecorderBean.setType(Constants.ITEM_LEFT);
            requestTran(userRecoderTxt, leftLanguage, rightLanguage);

        }
        *//*lin_left.setBackground(ChatActivity.this.getDrawable(R.drawable.chat_left_btn_unselect));
        lin_right.setBackground(ChatActivity.this.getDrawable(R.drawable.chat_right_btn_unselect));
        animationDrawable.stop();*//*
    }*/


 /*   @Override
    public void onIntentReceived(String s) {
        Log.e(TAG, "onIntentReceived ： " + s);
    }*/

   /* @Override
    public void onError(int i, String s) {
        Log.e(TAG, "微软语音合成错误 ： " + s.toString()+" "+i);
        lin_left.setBackground(this.getDrawable(R.drawable.chat_left_btn_unselect));
        lin_right.setBackground(this.getDrawable(R.drawable.chat_right_btn_unselect));
        lin_left.setClickable(true);
        lin_right.setClickable(true);
        mVoice.setImageResource(R.drawable.chat_voice);

        animationDrawable.stop();
        leftIsStartVoice = false;
        rightIsStartVoice = false;
        Toast.makeText(ChatActivity.this, "录音失败", Toast.LENGTH_SHORT).show();
    }*/

   /* @Override
    public void onAudioEvent(boolean b) {
        Log.e(TAG, "onAudioEvent ： " + b);
    }*/

    /**
     * 请求翻译
     *
     * @param trandata
     * @param from
     * @param to
     */
    private void requestTran(String trandata, String from, String to) {
        isTranslate = true;
        int guestId = SpUtils.getInt(ChatActivity.this, Constants.GUEST_ID, 1);
        OkGo.<String>post(Constants.BASEURL + Constants.TEXTTRANSLATE)
                .tag(this)
                .params("text", trandata)
                .params("from", from)
                .params("to", to)
                .retryCount(0)
                .params("guestId", guestId)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.e(TAG, response.body().toString());
                        TranResultBean resultBean = GsonUtils.getInstance().GsonToBean(response.body().toString(), TranResultBean.class);
                        if (resultBean.getStatus() == Constants.REQUEST_SUCCESS) {
                            Log.e(TAG, "" + (Looper.getMainLooper() == Looper.myLooper()));
                            Log.e(TAG, "请求成功");

                            String tranText = resultBean.getTranslateResult().getText();
                            final String tranAudio = resultBean.getTranslateResult().getAudio();
                            if (!TextUtils.isEmpty(tranText) && !TextUtils.isEmpty(tranAudio)) {
                                Log.e(TAG, tranText + " " + tranAudio);

                                if (rightIsStartVoice) {
                                    rightRecorderBean.setmResultTxt(tranText);
                                   //  rightRecorderBean.setmFilePath(resultBean.getTranslateResult().getAudio());
                                    // mRecorderList.add(rightRecorderBean);


                                } else if (leftIsStartVoice) {
                                    leftRecorderBean.setmResultTxt(tranText);
                                    //leftRecorderBean.setmFilePath(resultBean.getTranslateResult().getAudio());
                                    // mRecorderList.add(leftRecorderBean);


                                }
                                // List<RecorderBean> list = DaoUtils.getRecorderBeanDaoManager().QueryAll(RecorderBean.class);
                                /*if(list.size() > 0){
                                    //list.forEach(RecorderBean.class);
                                    for (int i = 0;i <list.size();i++){
                                        Log.e(TAG,list.get(i).toString());
                                    }
                                }else{
                                    Log.e(TAG,"list 为0");
                                }*/
                                if (!tranAudio.endsWith(".mp3")) {
                                    Log.e(TAG, "语音合成出错！！！！！！");
                                  /* if (rightIsStartVoice) {
                                        //rightRecorderBean.setmFilePath("");
                                       // mRecorderList.add(rightRecorderBean);
                                       DaoUtils.getRecorderBeanDaoManager().insertObject(rightRecorderBean);
                                    } else if (leftIsStartVoice) {
                                       // leftRecorderBean.setmFilePath("");
                                       // mRecorderList.add(leftRecorderBean);
                                       DaoUtils.getRecorderBeanDaoManager().insertObject(leftRecorderBean);

                                    }*/

                                    leftIsStartVoice = false;
                                    rightIsStartVoice = false;
                                    isTranslate = false;
                                    Toast.makeText(ChatActivity.this,R.string.request_error,Toast.LENGTH_SHORT);
                                 /*   mAdapter.notifyDataSetChanged();
                                    mListViewChat.setSelection(mRecorderList.size() - tran_voice1);*/
                                    //return;
                                } else {
                                    //下载音频
                                    requestAudio(tranAudio);
                                }
                            }
                        } else if (resultBean.getStatus() == Constants.REQUEST_FAIL) {
                            Log.e(TAG, "请求失败");
                            rightIsStartVoice = false;
                            leftIsStartVoice = false;
                            isTranslate = false;
                            isRecordering = false;
                            animationDrawable.stop();
                            lin_left.setBackground(ChatActivity.this.getDrawable(R.drawable.chat_left_btn_unselect));
                            lin_right.setBackground(ChatActivity.this.getDrawable(R.drawable.chat_right_btn_unselect));
                            Toast.makeText(ChatActivity.this, R.string.tran_error, Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        leftIsStartVoice = false;
                        rightIsStartVoice = false;
                        isTranslate = false;
                        isRecordering = false;
                        animationDrawable.stop();
                        lin_left.setBackground(ChatActivity.this.getDrawable(R.drawable.chat_left_btn_unselect));
                        lin_right.setBackground(ChatActivity.this.getDrawable(R.drawable.chat_right_btn_unselect));
                        Toast.makeText(ChatActivity.this, R.string.tran_error, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * 请求录音并播放
     *
     * @param tranAudio
     */
    private void requestAudio(String tranAudio) {

        OkGo.<File>get(tranAudio)
                .retryCount(0)
                .execute(new FileCallback() {
                    @Override
                    public void onSuccess(Response<File> response) {
                        if (response.body().isFile()) {
                            Log.e(TAG, "file");
                            // InputStream inputStream = new FileInputStream(response.body());
                            final File file = response.body().getAbsoluteFile();
                            Log.e(TAG, file.getAbsolutePath());
                            if (rightIsStartVoice) {
                                // rightRecorderBean.setmFilePath(resultBean.getTranslateResult().getAudio());
                                rightRecorderBean.setmFilePath(file.getAbsolutePath());
                                mRecorderList.add(rightRecorderBean);

                                DaoUtils.getRecorderBeanDaoManager().insertObject(rightRecorderBean);
                            } else if (leftIsStartVoice) {
                                // leftRecorderBean.setmFilePath(resultBean.getTranslateResult().getAudio());
                                leftRecorderBean.setmFilePath(file.getAbsolutePath());
                                mRecorderList.add(leftRecorderBean);

                                 DaoUtils.getRecorderBeanDaoManager().insertObject(leftRecorderBean);
                            }
                            mAdapter.notifyDataSetChanged();
                            mListViewChat.setSelection(mRecorderList.size() - 1);
                            /*  LinearLayout linearLayout = (LinearLayout) mListViewChat.getChildAt(mRecorderList.size()-1);
                          LinearLayout linearLayout1 = (LinearLayout)linearLayout.getChildAt(0);
                            LinearLayout linearLayout2 = (LinearLayout)linearLayout.getChildAt(1);
                            ImageView v = (ImageView) linearLayout2.findViewById(R.id.img_chat_play);
                            v.setImageResource(R.drawable.item_voice_play_animation);
                            animationPlay = (AnimationDrawable)v.getDrawable();
                            animationPlay.start();*/
                               /* new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mAudioMediaPlayManager.startPlayingUsePhone(file.getAbsolutePath());
                                    }
                                }).start();*/
                            //Log.e(TAG,mRecorderList.get(pos).getmFilePath());
                            rightIsStartVoice = false;
                            leftIsStartVoice = false;
                            isRecordering = false;
                             mAudioMediaPlayManager.startPlayingUsePhone(file.getAbsolutePath());
                            isTranslate = false;
                        }
                    }

                    @Override
                    public void onError(Response<File> response) {
                        super.onError(response);
                        rightIsStartVoice = false;
                        leftIsStartVoice = false;
                        isRecordering = false;
                        isTranslate = false;
                    }
                });
    }

   /* private void startPermissionsActivity() {
        PermissionsActivity.startActivityForResult(this, PERMISSIONS_REQUEST_CODE, PERMISSIONS);
    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.LANGUAGE_CHANGE) {
            Log.d(TAG, "语言改变");
            leftLanguage = SpUtils.getString(ChatActivity.this, Constants.LEFT_LANGUAGE, Constants.zh_CN);
            rightLanguage = SpUtils.getString(ChatActivity.this, Constants.RIGHT_LANGUAGE, Constants.en_US);
            Utils.perseLanguage(ChatActivity.this, leftLanguage, chat_left_img, mLeftText);
            Utils.perseLanguage(ChatActivity.this, rightLanguage, chat_right_img, mRightText);
            Utils.setLanguageHead(ChatActivity.this, leftlanguage_head, leftLanguage);
            Utils.setLanguageHead(ChatActivity.this, rightlanguage_head, rightLanguage);
            if (leftLanguage.equals(Constants.zh_CN)) {
                chat_left_txt.setText("按下说话");
            } else {
                chat_left_txt.setText("Hold to talk");
            }
            if (rightLanguage.equals(Constants.zh_CN)) {
                chat_right_txt.setText("按下说话");
            } else {
                chat_right_txt.setText("Hold to talk");
            }
            Log.d(TAG, "左边：" + leftLanguage);
            Log.d(TAG, "右边: " + rightLanguage);
        }
        // 拒绝时, 关闭页面, 缺少主要权限, 无法运行
       /* if (requestCode == PERMISSIONS_REQUEST_CODE && resultCode == PermissionsActivity.PERMISSIONS_DENIED) {
            finish();
        }*/
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(null != this.micClient){
            Log.e(TAG,"onDestroy micClient");
            try {
                this.micClient.finalize();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
            this.micClient = null;
        }
     /*   mMicroRecogitionManager.releseClient();
        if(mMicroRecogitionManager !=null){
            Log.e(TAG,"MicroRecogitionManager !=null");
            mMicroRecogitionManager = null;
        }else {
            Log.e(TAG,"MicroRecogitionManager null");
        }*/
//        if(mMicroRecogitionManager !=null){
//            Log.e(TAG,"MicroRecogitionManager !=null");
//            mMicroRecogitionManager = null;
//        }else {
//            Log.e(TAG,"MicroRecogitionManager null");
//        }
      /*  if(null != this.micClient){
            Log.e(TAG,"        if(this.micClient){\n");
            this.micClient = null;
        }else{
            Log.e(TAG,"        if(null == this.micClient){\n");
        }*/
        OkGo.getInstance().cancelTag(this);
    }

    @Override
    public void onPartialResponseReceived(String s) {
        Log.e(TAG,"onPartialResponseReceived "+s);
    }

    @Override
    public void onFinalResponseReceived(RecognitionResult recognitionResult) {
        //mVoice.setBackground(getResources().getDrawable(R.drawable.voice_img3));

       if (recognitionResult.Results.length <= 0) {
           lin_left.setBackground(this.getDrawable(R.drawable.chat_left_btn_unselect));
           lin_right.setBackground(this.getDrawable(R.drawable.chat_right_btn_unselect));
           lin_left.setClickable(true);
           lin_right.setClickable(true);
           mVoice.setImageResource(R.drawable.chat_voice);
           isRecordering = false;
           animationDrawable.stop();
           leftIsStartVoice = false;
           rightIsStartVoice = false;
           Toast.makeText(ChatActivity.this, R.string.recognition_failed, Toast.LENGTH_SHORT).show();
        return;
    }
        animationDrawable.stop();
        mVoice.setImageResource(R.drawable.chat_voice);
        lin_left.setBackground(ChatActivity.this.getDrawable(R.drawable.chat_left_btn_unselect));
        lin_right.setBackground(ChatActivity.this.getDrawable(R.drawable.chat_right_btn_unselect));

        String result = recognitionResult.Results[0].DisplayText;
            //更新界面
            lin_right.setClickable(true);
        lin_left.setClickable(true);
        if (rightIsStartVoice) {



       // lin_right.setBackground(this.getDrawable(R.drawable.chat_right_btn_unselect));

        // TODO: 2018/tran_voice1/17  停止录音后更新界面处理
        rightRecorderBean = new RecorderBean();
        rightRecorderBean.setLanguage_type(rightLanguage);
        //rightRecorderBean.setmFilePath(mVoiceFilePath);

        rightRecorderBean.setmRecorderTxt(result);
        rightRecorderBean.setType(Constants.ITEM_RIGHT);
        requestTran(result, rightLanguage, leftLanguage);
        //mListViewChat.s
    } else if (leftIsStartVoice) {
       // lin_left.setBackground(this.getDrawable(R.drawable.chat_left_btn_unselect));

        // TODO: 2018/tran_voice1/17  停止录音后更新界面处理
        leftRecorderBean = new RecorderBean();
        leftRecorderBean.setLanguage_type(leftLanguage);
        //leftRecorderBean.setmFilePath(mVoiceFilePath);
        //initSpeechRecognition(mVoiceFilePath);

        leftRecorderBean.setmRecorderTxt(result);
        leftRecorderBean.setType(Constants.ITEM_LEFT);
        requestTran(result, leftLanguage, rightLanguage);

    }


        isRecordering = false;
    }

    @Override
    public void onIntentReceived(String s) {
        Log.e(TAG,"onIntentReceived");
    }

    @Override
    public void onError(int i, String s) {
        Log.e(TAG, "微软语音合成错误 ： " + s.toString());
        lin_left.setBackground(this.getDrawable(R.drawable.chat_left_btn_unselect));
        lin_right.setBackground(this.getDrawable(R.drawable.chat_right_btn_unselect));
        lin_left.setClickable(true);
        lin_right.setClickable(true);
        mVoice.setImageResource(R.drawable.chat_voice);
        isRecordering = false;
        animationDrawable.stop();
        leftIsStartVoice = false;
        rightIsStartVoice = false;
        Toast.makeText(ChatActivity.this, R.string.record_fail, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAudioEvent(boolean b) {
        Log.e(TAG,"onAudioEvent "+b);
    }
}
