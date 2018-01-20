package com.wtwd.translate.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.microsoft.cognitiveservices.speechrecognition.DataRecognitionClient;
import com.microsoft.cognitiveservices.speechrecognition.ISpeechRecognitionServerEvents;
import com.microsoft.cognitiveservices.speechrecognition.MicrophoneRecognitionClient;
import com.microsoft.cognitiveservices.speechrecognition.RecognitionResult;
import com.microsoft.cognitiveservices.speechrecognition.RecognitionStatus;
import com.microsoft.cognitiveservices.speechrecognition.SpeechRecognitionMode;
import com.microsoft.cognitiveservices.speechrecognition.SpeechRecognitionServiceFactory;
import com.wtwd.translate.R;
import com.wtwd.translate.adapter.DevTranListViewAdapter;
import com.wtwd.translate.bean.RecorderBean;
import com.wtwd.translate.utils.Constants;
import com.wtwd.translate.utils.SpUtils;
import com.wtwd.translate.utils.SpeechUtils;
import com.wtwd.translate.utils.Utils;
import com.wtwd.translate.utils.audio.AudioMediaPlayManager;
import com.wtwd.translate.utils.audio.AudioStateChange;
import com.wtwd.translate.utils.permissions.PermissionsActivity;
import com.wtwd.translate.utils.permissions.PermissionsChecker;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


/**
 * time:2017/12/27
 * Created by w77996
 */
public class ChatActivity extends Activity implements View.OnClickListener,AudioStateChange,ISpeechRecognitionServerEvents {

    public final static String TAG  = "ChatActivity";
    /**底部左录音按钮**/
    ImageButton mLeftVoiceImg;
    /**底部右录音按钮**/
    ImageButton mRightVoiceImg;
    /**头部语言选择按钮**/
    ImageView mDownImg;
    /**返回**/
    ImageView mBack;

    /**头部左侧语言选择栏**/
    TextView mLeftText;
    /**头部右侧侧语言选择栏**/
    TextView mRightText;
    /**关闭语音**/
    ImageView mVoiceClose;
    /**语音键**/
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
     *  底部右侧linear
     */
    LinearLayout lin_right;
    /**
     *语言转换按钮
     */
    ImageView img_chat_switch;
    /**
     *语音处理类
     */
    AudioMediaPlayManager mAudioMediaPlayManager;

    /**
     *  录音文件路径
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
    boolean leftIsStartVoice =false;
    boolean rightIsStartVoice =false;

    /**
     * 微软语音状态
     */
    FinalResponseStatus isReceivedResponse = FinalResponseStatus.NotReceived;

    public SpeechRecognitionMode getMode() {
        return SpeechRecognitionMode.ShortPhrase;
    }

    public enum FinalResponseStatus { NotReceived, OK, Timeout }

    /**
     * 微软语音接口
     */
    DataRecognitionClient dataClient = null;
    MicrophoneRecognitionClient micClient = null;

    private static final int PERMISSIONS_REQUEST_CODE = 0111; // 请求码

    private PermissionsChecker mPermissionsChecker; // 权限检测器

    // 所需的全部权限
    static final String[] PERMISSIONS = new String[]{
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        mAudioMediaPlayManager = AudioMediaPlayManager.getAudioMediaPlayManager(this);
        mAudioMediaPlayManager.setAudioStateChange(this);
        initData();
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPermissionsChecker = new PermissionsChecker(this);
        // 缺少权限时, 进入权限配置页面
        if (mPermissionsChecker.lacksPermissions(PERMISSIONS)) {
            startPermissionsActivity();
        }
    }

    private void initData() {
        mRecorderList = new ArrayList<>();
        //获取已选择的默认语言
        leftLanguage = SpUtils.getString(ChatActivity.this, Constants.LEFT_LANGUAGE,Constants.zh_CN);
        rightLanguage = SpUtils.getString(ChatActivity.this,Constants.RIGHT_LANGUAGE,Constants.en_US);

    }

    /**
     * 初始化界面控件
     */
    private void initView() {
        Utils.setWindowStatusBarColor(this,R.color.main_title_color);
       // mLeftVoiceImg = (ImageButton)findViewById(R.id.imgbtn_left);
      //  mRightVoiceImg = (ImageButton)findViewById(R.id.imgbtn_right);
        mDownImg = (ImageView)findViewById(R.id.img_chat_down_row);
        mBack = (ImageView)findViewById(R.id.chat_back);
        mLeftText = (TextView)findViewById(R.id.tv_chat_title_left);
        mRightText = (TextView)findViewById(R.id.tv_chat_title_right);
        mVoiceClose = (ImageView)findViewById(R.id.chat_voice_close);
        mVoice = (ImageView)findViewById(R.id.img_chat_voice);
        img_chat_switch = (ImageView)findViewById(R.id.img_chat_switch);
        chat_left_img =(ImageView)findViewById(R.id.chat_left_img);
        chat_right_img = (ImageView)findViewById(R.id.chat_right_img);
        lin_left = (LinearLayout)findViewById(R.id.lin_left);
        lin_right = (LinearLayout)findViewById(R.id.lin_right);
        Utils.perseLanguage(ChatActivity.this, leftLanguage,chat_left_img,mLeftText);
        Utils.perseLanguage(ChatActivity.this,rightLanguage,chat_right_img,mRightText);

        mListViewChat = (ListView)findViewById(R.id.lv_chat);
        //initSpeechRecognition();
        mAdapter = new DevTranListViewAdapter(this,mRecorderList,new DevTranListViewAdapter.PlayVoceClick(){

            @Override
            public void click(View v) {
               int pos =  (Integer) v.getTag();
               Log.d(TAG,pos+" 点击按钮位置");
//               mAudioMediaPlayManager.startPlayingUsePhone(mRecorderList.get(pos).getmFilePath());
                SpeechUtils.getInstance(ChatActivity.this,Utils.setLocalLanguag(mRecorderList.get(pos).getLanguage_type())).speakText(mRecorderList.get(pos).getmResultTxt());
            }
        });
        mListViewChat.setAdapter(mAdapter);

      //  mLeftVoiceImg.setOnClickListener(this);
     //   mRightVoiceImg.setOnClickListener(this);
        mDownImg.setOnClickListener(this);
        mBack.setOnClickListener(this);
        mLeftText.setOnClickListener(this);
        mRightText.setOnClickListener(this);
        mVoiceClose.setOnClickListener(this);
        mVoice.setOnClickListener(this);
        img_chat_switch.setOnClickListener(this);
        lin_left.setOnClickListener(this);
        lin_right.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.lin_left:
               /* if(rightBtnPress){
                    rightBtnPress = false;
                    mAudioMediaPlayManager.stopRecorderUsePhone();
                    lin_right.setBackground(this.getDrawable(R.drawable.chat_right_btn_selected));
                }*/
               Log.d(TAG,"左侧开始录音");
                lin_left.setBackground(this.getDrawable(R.drawable.chat_left_btn_selected));
                if(leftIsStartVoice){
                   // mAudioMediaPlayManager.stopRecorderUsePhone();
                    /*leftIsStartVoice = false;
                    lin_right.setClickable(true);
                    lin_left.setBackground(this.getDrawable(R.drawable.chat_left_btn_unselect));

                    // TODO: 2018/1/17  停止录音后更新界面处理
                    RecorderBean recorderBean = new RecorderBean();
                    recorderBean.setLanguage_type(leftLanguage);
                    recorderBean.setmFilePath(mVoiceFilePath);
                    //initSpeechRecognition(mVoiceFilePath);
                    recorderBean.setType(Constants.ITEM_LEFT);
                    mRecorderList.add(recorderBean);
                    mAdapter.notifyDataSetChanged();
                    mListViewChat.setSelection(mRecorderList.size()-1);*/
                }else{
                   // leftBtnPress = true;
                    mVoiceFilePath = Utils.getVoiceFilePath();
                    Log.d(TAG,"左侧录音路径 " +mVoiceFilePath);
                  //  mAudioMediaPlayManager.startRecorderUsePhone(mVoiceFilePath);
                    initSpeechRecognition(leftLanguage);
                    leftIsStartVoice = true;
                    lin_right.setClickable(false);
                }

                break;
            case R.id.lin_right:
               /* if(leftBtnPress){
                    leftBtnPress = false;
                    mAudioMediaPlayManager.stopRecorderUsePhone();
                    leftIsStartVoice;
                }*/
                Log.d(TAG,"右侧开始录音");
                lin_right.setBackground(this.getDrawable(R.drawable.chat_right_btn_selected));
                if(rightIsStartVoice){

                   /* rightIsStartVoice = false;
                    lin_left.setClickable(true);
                    lin_right.setBackground(this.getDrawable(R.drawable.chat_right_btn_unselect));

                    // TODO: 2018/1/17  停止录音后更新界面处理
                    RecorderBean recorderBean = new RecorderBean();
                    recorderBean.setLanguage_type(rightLanguage);
                    recorderBean.setmFilePath(mVoiceFilePath);

                    recorderBean.setType(Constants.ITEM_RIGHT);
                    mRecorderList.add(recorderBean);
                    mAdapter.notifyDataSetChanged();
                    mListViewChat.setSelection(mRecorderList.size()-1);*/
                    //mListViewChat.s
                }else{
                    // leftBtnPress = true;
                    mVoiceFilePath = Utils.getVoiceFilePath();
                    Log.d(TAG,"右侧录音路径 " +mVoiceFilePath);
                    //mAudioMediaPlayManager.startRecorderUsePhone(mVoiceFilePath);
                    initSpeechRecognition(rightLanguage);
                    rightIsStartVoice = true;
                    lin_left.setClickable(false);
                }
                break;
            case R.id.img_chat_down_row:
                Intent mLanguageSelectIntent = new Intent(this,LanguageSelectActivity.class);
                mLanguageSelectIntent.putExtra("derect",0);
                startActivityForResult(mLanguageSelectIntent,Constants.LANGUAGE_CHANGE);
                break;
            case R.id.chat_back:
                finish();
                break;
            case R.id.tv_chat_title_left:
                Intent mLanguageSelectLeftIntent = new Intent(this,LanguageSelectActivity.class);
                mLanguageSelectLeftIntent.putExtra("derect",0);
                startActivityForResult(mLanguageSelectLeftIntent,Constants.LANGUAGE_CHANGE);
                break;
            case R.id.tv_chat_title_right:
                Intent mLanguageSelectRightIntent = new Intent(this,LanguageSelectActivity.class);
                mLanguageSelectRightIntent.putExtra("derect",1);
                startActivityForResult(mLanguageSelectRightIntent,Constants.LANGUAGE_CHANGE);
                break;
            case R.id.chat_voice_close:
                break;
            case R.id.img_chat_voice:
                break;
            case R.id.img_chat_switch:
                 leftLanguage = SpUtils.getString(ChatActivity.this,Constants.LEFT_LANGUAGE,Constants.zh_CN);
                 rightLanguage = SpUtils.getString(ChatActivity.this,Constants.RIGHT_LANGUAGE,Constants.en_US);
                SpUtils.putString(ChatActivity.this,Constants.LEFT_LANGUAGE,rightLanguage);
                SpUtils.putString(ChatActivity.this,Constants.RIGHT_LANGUAGE,leftLanguage);
                Utils.perseLanguage(ChatActivity.this, leftLanguage,chat_left_img,mLeftText);
                Utils.perseLanguage(ChatActivity.this,rightLanguage,chat_right_img,mRightText);
                break;
        }
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
        initSpeechRecognition(mVoiceFilePath);
    }

    @Override
    public void onStopRecoderUsePhone() {

    }

    @Override
    public void onStartPlayUsePhone() {

    }

    @Override
    public void onStopPlayUsePhone() {

    }

    @Override
    public void onPlayCompletion() {

    }

    @Override
    public void onPlayError() {

    }


    /**********微软语音合成回调*************/

    int m_waitSeconds = 20;

    /**
     * 初始化微软文件发送语音合成
     */
    private void initSpeechRecognition(){
        /*this.micClient = SpeechRecognitionServiceFactory.createMicrophoneClient(
                this,
                SpeechRecognitionMode.ShortPhrase ,
                "en-us",
                this,
                "c96c2a771c6e4548a24e269884889478");*/
        this.dataClient = SpeechRecognitionServiceFactory.createDataClient(
                this,
                SpeechRecognitionMode.ShortPhrase ,
                "en-us",
                this,
                "c96c2a771c6e4548a24e269884889478");
        this.dataClient.setAuthenticationUri("");
        //SendAudioHelper(filename);

    }
    private void initSpeechRecognition(String mLanguageType){
        this.micClient = SpeechRecognitionServiceFactory.createMicrophoneClient(
                this,
                SpeechRecognitionMode.ShortPhrase ,
                mLanguageType,
                this,
                "c96c2a771c6e4548a24e269884889478");
        this.micClient.setAuthenticationUri("");
        this.micClient.startMicAndRecognition();

    }
    /**
     *
     * @param filename
     */
    private void SendAudioHelper(String filename) {
        RecognitionTask doDataReco = new RecognitionTask(this.dataClient, SpeechRecognitionMode.ShortPhrase, filename);
        Log.d(TAG,"SendAudioHelper : " + filename);
        try
        {
            doDataReco.execute().get(m_waitSeconds, TimeUnit.SECONDS);
        }
        catch (Exception e)
        {
            doDataReco.cancel(true);
            isReceivedResponse = FinalResponseStatus.Timeout;
        }
    }

    /*
    * Speech recognition with data (for example from a file or audio source).
    * The data is broken up into buffers and each buffer is sent to the Speech Recognition Service.
    * No modification is done to the buffers, so the user can apply their
    * own VAD (Voice Activation Detection) or Silence Detection
    *
    * @param dataClient
    * @param recoMode
    * @param filename
    */
    private class RecognitionTask extends AsyncTask<Void, Void, Void> {
        DataRecognitionClient dataClient;
        SpeechRecognitionMode recoMode;
        String filename;


        RecognitionTask(DataRecognitionClient dataClient, SpeechRecognitionMode recoMode, String filename) {
            this.dataClient = dataClient;
            this.recoMode = recoMode;
            this.filename = filename;
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                // Note for wave files, we can just send data from the file right to the server.
                // In the case you are not an audio file in wave format, and instead you have just
                // raw data (for example audio coming over bluetooth), then before sending up any
                // audio data, you must first send up an SpeechAudioFormat descriptor to describe
                // the layout and format of your raw audio data via DataRecognitionClient's sendAudioFormat() method.
                // String filename = recoMode == SpeechRecognitionMode.ShortPhrase ? "whatstheweatherlike.wav" : "batman.wav";
                Log.d(TAG,"RecognitionTask"+filename);
                InputStream fileStream = new FileInputStream(filename);
                int bytesRead = 0;
                byte[] buffer = new byte[1024];

                do {
                    // Get  Audio data to send into byte buffer.
                    bytesRead = fileStream.read(buffer);

                    if (bytesRead > -1) {
                        // Send of audio data to service.
                        dataClient.sendAudio(buffer, bytesRead);
                    }
                } while (bytesRead > 0);

            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
            finally {
                dataClient.endAudio();
            }

            return null;
        }
    }
    @Override
    public void onPartialResponseReceived(String s) {
        Log.e(TAG,"onPartialResponseReceived ： "+s);
    }

    @Override
    public void onFinalResponseReceived(RecognitionResult recognitionResult) {
        boolean isFinalDicationMessage = this.getMode() == SpeechRecognitionMode.LongDictation &&
                (recognitionResult.RecognitionStatus == RecognitionStatus.EndOfDictation ||
                        recognitionResult.RecognitionStatus == RecognitionStatus.DictationEndSilenceTimeout);
        if (null != this.micClient  && (this.getMode() == SpeechRecognitionMode.ShortPhrase) || isFinalDicationMessage) {
            // we got the final result, so it we can end the mic reco.  No need to do this
            // for dataReco, since we already called endAudio() on it as soon as we were done
            // sending all the data.
            this.micClient.endMicAndRecognition();
        }
        Log.d(TAG,"********* Final n-BEST Results *********");
        for (int i = 0; i < recognitionResult.Results.length; i++) {
            Log.d(TAG,"[" + i + "]" + " Confidence=" + recognitionResult.Results[i].Confidence +
                    " Text=\"" + recognitionResult.Results[i].DisplayText + "\"");
        }
       // mAudioMediaPlayManager.stopRecorderUsePhone();
        if(recognitionResult.Results.length <= 0){
            Log.e(TAG,"recognitionResult 长度为0识别失败");
            return;
        }
        //更新界面
        if(rightIsStartVoice){

            rightIsStartVoice = false;
            lin_left.setClickable(true);
            lin_right.setBackground(this.getDrawable(R.drawable.chat_right_btn_unselect));

            // TODO: 2018/1/17  停止录音后更新界面处理
            RecorderBean recorderBean = new RecorderBean();
            recorderBean.setLanguage_type(rightLanguage);
            recorderBean.setmFilePath(mVoiceFilePath);

            recorderBean.setmResultTxt(recognitionResult.Results[0].DisplayText );
            recorderBean.setType(Constants.ITEM_RIGHT);
            mRecorderList.add(recorderBean);
            mAdapter.notifyDataSetChanged();
            mListViewChat.setSelection(mRecorderList.size()-1);
            //mListViewChat.s
        }else if(leftIsStartVoice){
            leftIsStartVoice = false;
            lin_right.setClickable(true);
            lin_left.setBackground(this.getDrawable(R.drawable.chat_left_btn_unselect));

            // TODO: 2018/1/17  停止录音后更新界面处理
            RecorderBean recorderBean = new RecorderBean();
            recorderBean.setLanguage_type(leftLanguage);
            recorderBean.setmFilePath(mVoiceFilePath);
            //initSpeechRecognition(mVoiceFilePath);
            recorderBean.setmResultTxt(recognitionResult.Results[0].DisplayText );
            recorderBean.setType(Constants.ITEM_LEFT);
            mRecorderList.add(recorderBean);
            mAdapter.notifyDataSetChanged();
            mListViewChat.setSelection(mRecorderList.size()-1);
        }
    }

    @Override
    public void onIntentReceived(String s) {
        Log.e(TAG,"onIntentReceived ： "+s);
    }

    @Override
    public void onError(int i, String s) {
        Log.e(TAG,"微软语音合成错误 ： "+s);
    }

    @Override
    public void onAudioEvent(boolean b) {
        Log.e(TAG,"onAudioEvent ： "+b);
    }

    private void startPermissionsActivity() {
        PermissionsActivity.startActivityForResult(this, PERMISSIONS_REQUEST_CODE, PERMISSIONS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.LANGUAGE_CHANGE){
            Log.d(TAG,"语言改变");
            leftLanguage = SpUtils.getString(ChatActivity.this, Constants.LEFT_LANGUAGE,Constants.zh_CN);
            rightLanguage = SpUtils.getString(ChatActivity.this,Constants.RIGHT_LANGUAGE,Constants.en_US);
            Utils.perseLanguage(ChatActivity.this, leftLanguage,chat_left_img,mLeftText);
            Utils.perseLanguage(ChatActivity.this,rightLanguage,chat_right_img,mRightText);
            Log.d(TAG,"左边："+ leftLanguage);
            Log.d(TAG,"右边: "+rightLanguage);
        }
        // 拒绝时, 关闭页面, 缺少主要权限, 无法运行
        if (requestCode == PERMISSIONS_REQUEST_CODE && resultCode == PermissionsActivity.PERMISSIONS_DENIED) {
            finish();
        }
    }

}
