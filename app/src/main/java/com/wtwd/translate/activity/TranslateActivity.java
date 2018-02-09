package com.wtwd.translate.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.view.inputmethod.InputMethodSubtype;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.microsoft.cognitiveservices.speechrecognition.ISpeechRecognitionServerEvents;
import com.microsoft.cognitiveservices.speechrecognition.MicrophoneRecognitionClient;
import com.microsoft.cognitiveservices.speechrecognition.RecognitionResult;
import com.microsoft.cognitiveservices.speechrecognition.SpeechRecognitionMode;
import com.microsoft.cognitiveservices.speechrecognition.SpeechRecognitionServiceFactory;
import com.wtwd.translate.R;
import com.wtwd.translate.adapter.LanguageSelectListViewAdapter;
import com.wtwd.translate.bean.SelectBean;
import com.wtwd.translate.bean.TranResultBean;
import com.wtwd.translate.utils.Constants;
import com.wtwd.translate.utils.GsonUtils;
import com.wtwd.translate.utils.SpUtils;
import com.wtwd.translate.utils.audio.AudioMediaPlayManager;
import com.wtwd.translate.utils.audio.AudioStateChange;
import com.wtwd.translate.utils.keybord.InputTools;
import com.wtwd.translate.utils.Utils;
import com.wtwd.translate.utils.keybord.SoftKeyBoardListener;
import com.wtwd.translate.utils.micro.MicroRecogitionManager;
import com.wtwd.translate.utils.permissions.PermissionsActivity;
import com.wtwd.translate.utils.permissions.PermissionsChecker;
import com.wtwd.translate.view.InputEdittext;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * time:2017/12/27
 * Created by w77996
 */
public class TranslateActivity extends Activity implements View.OnClickListener,ISpeechRecognitionServerEvents {

    private static final String TAG = "TranslateActivity";
    /**
     * 输入框
     */
    private InputEdittext mSearchBoxEditText;
    /**
     * 返回按钮
     */
    private TextView mBackTextView;
    /**
     * 点击搜索按钮
     */
    private ImageView mSearchBoxImageView;
    /**
     * 软键盘高度
     */
    private int mKeybordHight;
    /**
     * 底部LinearLayout
     */
    private LinearLayout lin_tran_bottom;
    /**
     * EditText输入框外部linearlayout
     */
    private LinearLayout lin_tran_edit;
    /**
     * 进入类型
     */
    private int mIntentType;
    /**
     * 输入框内的清除按键
     */
    private ImageView mEditClearImageView;

    /**
     * ScrollView界面
     */
    ScrollView mTranScrollView;
    /**
     * 文本翻译
     */
    LinearLayout lin_tran_text;
    /**
     * 语音翻译
     */
    LinearLayout lin_tran_vocie;
    /**
     * 录音按钮
     */
    ImageView img_tran_recro;
    /**
     * 翻译界面Linearlayout
     */
    LinearLayout lin_tran;
    /**
     * 键盘image
     */
    ImageView img_tran_keybord;
    /**
     * 语音image
     */
    ImageView img_tran_voice;
    ImageView img_tran_recro_bg;
    /**
     * 标题栏按钮文字
     */
    TextView text_tran_left_language;
    TextView text_tran_right_language;

    ImageView leftlanguage_head;//左侧国旗
    ImageView rightlanguage_head;//右侧国旗
    ImageView img_tran_switch;//切换

    String leftLanguage;
    String rightLanguage;

    /**
     * 结果显示linear
     */
    LinearLayout lin_tran_result;
    /**
     * 翻译结果
     */
    TextView tv_tran_result;
  //  List<SelectBean> datas = new ArrayList<SelectBean>();

    ImageView tran_back;

    ImageView tran_play;

    private InputMethodManager mInputMethodManager;

    MicrophoneRecognitionClient micClient = null;
    ChatActivity.FinalResponseStatus isReceivedResponse = ChatActivity.FinalResponseStatus.NotReceived;

    String nowLanguage="";
    private boolean isTranslate = false;


    public enum FinalResponseStatus { NotReceived, OK, Timeout }
   // MicroRecogitionManager mMicroRecogitionManager;
    private Animation mAnimation = null;
    private AudioMediaPlayManager mAudioMediaPlayManager;

    private static final int PERMISSIONS_REQUEST_CODE = 0111; // 请求码
    private AnimationDrawable animationDrawable;
    private String mPlayPath;
   /* private PermissionsChecker mPermissionsChecker; // 权限检测器

    // 所需的全部权限
    static final String[] PERMISSIONS = new String[]{
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };*/

   /* @Override
    protected void onResume() {
        super.onResume();
        mPermissionsChecker = new PermissionsChecker(this);
        // 缺少权限时, 进入权限配置页面
        if (mPermissionsChecker.lacksPermissions(PERMISSIONS)) {
            startPermissionsActivity();
        }
    }
    private void startPermissionsActivity() {
        PermissionsActivity.startActivityForResult(this, PERMISSIONS_REQUEST_CODE, PERMISSIONS);
    }*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translate);
        initDatas();
        initView();
        addListener();
       /* mMicroRecogitionManager = MicroRecogitionManager.getMicroRecogitionManager(this);
        mMicroRecogitionManager.setmicroRecogitionManagerCallBack(this);*/

        mAudioMediaPlayManager = AudioMediaPlayManager.getAudioMediaPlayManager(this);
        mAudioMediaPlayManager.setAudioStateChange(new AudioStateChange() {
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

            }

            @Override
            public void onStartPlayUsePhone() {

            }

            @Override
            public void onStopPlayUsePhone() {
                Log.e(TAG,"onStopPlayUsePhone");
                animationDrawable.stop();
                tran_play.setImageResource(R.drawable.tran_voice1);
            }

            @Override
            public void onPlayCompletion() {
                if(null != animationDrawable){
                    animationDrawable.stop();
                    tran_play.setImageResource(R.drawable.tran_voice1);
                }
            }

            @Override
            public void onPlayError() {
                if(null != animationDrawable){
                    animationDrawable.stop();
                    tran_play.setImageResource(R.drawable.tran_voice1);
                }

            }
        });
    }

    /**
     * 初始化数据
     */
    private void initDatas() {
       /* final String[] countryText = {"中文[CHS]", "英语[ENG]", "法语[FRA]", "德语[DEU]", "韩语[KOR]", "日语[JPN]", "西班牙语[SPA]", "葡萄牙语[POR]", "意大利语[ITA]",
                "俄罗斯语[RUS]", "泰语[THA]", "印度语[HIN]"};
        datas = new ArrayList<>();
        for (int i = 0; i < countryText.length; i++) {
            SelectBean selectBean = new SelectBean();
            selectBean.setData(countryText[i]);
            selectBean.setSelect(false);
            datas.add(selectBean);
        }*/
        mKeybordHight = SpUtils.getInt(this,Constants.KEY_HIGHT,0);
        Intent intent = getIntent();
        String intent_type = intent.getStringExtra("intent_type");
        Log.e(TAG, "intent_type " + intent_type);
        if (intent_type.equals(Constants.INTENT_TRANT)) {
            mIntentType = 0;//文字翻译

        } else {
            mIntentType = 1;//语音翻译
        }
        leftLanguage = SpUtils.getString(this,Constants.LEFT_LANGUAGE,Constants.zh_CN);
        rightLanguage = SpUtils.getString(this,Constants.RIGHT_LANGUAGE,Constants.en_US);
    }

    /**
     * 初始化界面
     */
    private void initView() {
        mInputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        Utils.setWindowStatusBarColor(this, R.color.main_title_color);
        lin_tran_edit = (LinearLayout) findViewById(R.id.lin_edit);
        lin_tran_bottom = (LinearLayout) findViewById(R.id.lin_tran_bottom);
        lin_tran_bottom.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, mKeybordHight));
        lin_tran = (LinearLayout) findViewById(R.id.lin_tran);
        img_tran_recro = (ImageView)findViewById(R.id.img_tran_recro);
        img_tran_recro_bg = (ImageView)findViewById(R.id.img_tran_recro_bg);
        lin_tran_result = (LinearLayout)findViewById(R.id.lin_tran_result);
        text_tran_left_language = (TextView)findViewById(R.id.text_tran_left_language);
        text_tran_right_language = (TextView)findViewById(R.id.text_tran_right_language);
        leftlanguage_head = (ImageView)findViewById(R.id.leftlanguage_head);
        rightlanguage_head = (ImageView)findViewById(R.id.rightlanguage_head);
        img_tran_switch = (ImageView)findViewById(R.id.img_tran_switch);
        tv_tran_result = (TextView)findViewById(R.id.tv_tran_result);
        tran_back = (ImageView)findViewById(R.id.tran_back);
        tran_play = (ImageView)findViewById(R.id.tran_play);
        Utils.perseLanguage(this,leftLanguage,leftlanguage_head,text_tran_left_language);
        Utils.perseLanguage(this,rightLanguage,rightlanguage_head,text_tran_right_language);

        img_tran_keybord = (ImageView)findViewById(R.id.img_tran_keybord);
        img_tran_voice = (ImageView)findViewById(R.id.img_tran_voice);
        mSearchBoxEditText = (InputEdittext) findViewById(R.id.search_box_ed);
        if (mIntentType == 1) {
            //语音翻译
//            lin_tran_edit.setFocusable(true);
            //InputTools.KeyBoard(mSearchBoxEditText,"close");
            InputTools.KeyBoard(mSearchBoxEditText, "close");
            //mSearchBoxEditText.setFocusableInTouchMode(false);
            // InputTools.HideKeyboard(lin_tran_edit);
            Log.d(TAG, "hide");
            lin_tran_bottom.setVisibility(View.VISIBLE);

            img_tran_keybord.setImageDrawable(this.getDrawable(R.drawable.tran_keybord_normal));
            img_tran_voice.setImageDrawable(this.getDrawable(R.drawable.tran_voicebtn_select));

        } else {
            //文字翻译
            InputTools.KeyBoard(mSearchBoxEditText, "open");
            img_tran_keybord.setImageDrawable(this.getDrawable(R.drawable.tran_keybord_select));
            img_tran_voice.setImageDrawable(this.getDrawable(R.drawable.tran_voicebtn_normal));
//            lin_tran_bottom.setVisibility(View.GONE);
        }

        mSearchBoxImageView = (ImageView) findViewById(R.id.search_box_img);
       // mTranSelectRecylerView = (RecyclerView) findViewById(R.id.rl_tran_type);
        mEditClearImageView = (ImageView) findViewById(R.id.img_tran_ed_clear);

        mTranScrollView = (ScrollView) findViewById(R.id.scrol_tran);
        lin_tran_text = (LinearLayout) findViewById(R.id.lin_tran_keybord);
        lin_tran_vocie = (LinearLayout) findViewById(R.id.lin_tran_voice);

        //EditText输入框的监听，显示清除按钮
        mSearchBoxEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //清除按钮，若EditText中内容为空则清除内容
                String keyword = s.toString();
                if (!TextUtils.isEmpty(keyword)) {
                    mEditClearImageView.setVisibility(View.VISIBLE);
                } else {
                    mEditClearImageView.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mSearchBoxEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Log.e(TAG,"focus : "+hasFocus);
            }
        });
    }


    /**
     * 显示键盘
     */
    private void showKeyBord() {
//        lin_tran_bottom.setVisibility(View.GONE);
        mSearchBoxEditText.setFocusable(true);
        InputTools.KeyBoard(mSearchBoxEditText, "open");
        img_tran_keybord.setImageDrawable(this.getDrawable(R.drawable.tran_keybord_select));
        img_tran_voice.setImageDrawable(this.getDrawable(R.drawable.tran_voicebtn_normal));
    }

    /**
     * 显示音量
     */
    private void showVoice() {
        lin_tran_edit.setFocusable(true);
        InputTools.KeyBoard(mSearchBoxEditText, "close");
        lin_tran_bottom.setVisibility(View.VISIBLE);
        img_tran_keybord.setImageDrawable(this.getDrawable(R.drawable.tran_keybord_normal));
        img_tran_voice.setImageDrawable(this.getDrawable(R.drawable.tran_voicebtn_select));
    }

    /**
     * 添加监听事件
     */
    private void addListener() {

        mEditClearImageView.setOnClickListener(this);
        mSearchBoxImageView.setOnClickListener(this);
        lin_tran_text.setOnClickListener(this);
        lin_tran_vocie.setOnClickListener(this);
        img_tran_recro.setOnClickListener(this);
        leftlanguage_head.setOnClickListener(this);
        rightlanguage_head.setOnClickListener(this);
        img_tran_switch.setOnClickListener(this);
        tran_back.setOnClickListener(this);
        tran_play.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent LanguageSelectIntent;
        switch (v.getId()) {
            case R.id.img_tran_ed_clear:
                mSearchBoxEditText.setText("");
                break;
            case R.id.lin_tran_keybord:
                Log.d(TAG, "选择文本翻译");
                mIntentType = 0;
                //switchKeyBord();
                showKeyBord();
                break;
            case R.id.lin_tran_voice:
                Log.d(TAG, "选择语音翻译");
                mIntentType = 1;
                //switchKeyBord();
                showVoice();
                break;
            case R.id.img_tran_recro:
                if(isTranslate){
                    Log.e(TAG,"翻译中");
                    Toast.makeText(TranslateActivity.this,R.string.translating,Toast.LENGTH_SHORT).show();
                    return;
                }
                mAnimation = AnimationUtils.loadAnimation(this,R.anim.voice_bg_anim);
                img_tran_recro_bg.startAnimation(mAnimation);
                //mAnimation.start();
                img_tran_recro.setClickable(false);
                initSpeechRecognition(leftLanguage);
                break;
            case R.id.leftlanguage_head:
                LanguageSelectIntent = new Intent(this, LanguageSelectActivity.class);
                LanguageSelectIntent.putExtra(Constants.LANGUAGE_SELECT_TYPE,Constants.LANGUAGE_SELECT_NORMAL_TYPE);
                LanguageSelectIntent.putExtra(Constants.DETRECT,Constants.DETRECT_LEFT);
                startActivityForResult(LanguageSelectIntent,Constants.LANGUAGE_CHANGE);
                break;
            case R.id.rightlanguage_head:
                LanguageSelectIntent = new Intent(this,LanguageSelectActivity.class);
                LanguageSelectIntent.putExtra(Constants.LANGUAGE_SELECT_TYPE,Constants.LANGUAGE_SELECT_NORMAL_TYPE);
                LanguageSelectIntent.putExtra(Constants.DETRECT,Constants.DETRECT_RIGHT);
                startActivityForResult(LanguageSelectIntent,Constants.LANGUAGE_CHANGE);
                break;
            case R.id.img_tran_switch:
                leftLanguage = SpUtils.getString(TranslateActivity.this,Constants.LEFT_LANGUAGE,Constants.zh_CN);
                rightLanguage = SpUtils.getString(TranslateActivity.this,Constants.RIGHT_LANGUAGE,Constants.en_US);
                SpUtils.putString(TranslateActivity.this,Constants.LEFT_LANGUAGE,rightLanguage);
                SpUtils.putString(TranslateActivity.this,Constants.RIGHT_LANGUAGE,leftLanguage);
                leftLanguage = SpUtils.getString(TranslateActivity.this, Constants.LEFT_LANGUAGE, Constants.zh_CN);
                rightLanguage = SpUtils.getString(TranslateActivity.this, Constants.RIGHT_LANGUAGE, Constants.en_US);
                Utils.perseLanguage(TranslateActivity.this, leftLanguage,leftlanguage_head,text_tran_left_language);
                Utils.perseLanguage(TranslateActivity.this,rightLanguage,rightlanguage_head,text_tran_right_language);

                break;
            case R.id.tran_back:
                if (InputTools.KeyBoard(mSearchBoxEditText)) {
                    mInputMethodManager.hideSoftInputFromWindow(mSearchBoxEditText.getWindowToken(), 0);
                }
                finish();
                break;
            case R.id.search_box_img:
                String tranData = mSearchBoxEditText.getText().toString();
                if(TextUtils.isEmpty(mSearchBoxEditText.getText().toString())){
                    Toast.makeText(TranslateActivity.this,"请输入翻译语句",Toast.LENGTH_SHORT).show();
                    return;
                }
                requestTran(tranData);
                break;
            case R.id.tran_play:
                if(null == mPlayPath || !mPlayPath.endsWith(".mp3")){
                    return;
                }
                tran_play.setImageResource(R.drawable.tran_voice_play);
                animationDrawable = (AnimationDrawable) tran_play.getDrawable();
                animationDrawable.start();

                mAudioMediaPlayManager.startPlayingUsePhone(mPlayPath);
                break;
        }
    }

    /**
     * 请求翻译
     * @param tranData
     */
    private void requestTran(String tranData) {
        isTranslate = true;
        int guestId = SpUtils.getInt(TranslateActivity.this,Constants.GUEST_ID,1);
        OkGo.<String>post(Constants.BASEURL+Constants.TEXTTRANSLATE)
                .params("text",tranData)
                .params("from",leftLanguage)
                .params("to",rightLanguage)
                .retryCount(1)
                .params("guestId",guestId)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.e(TAG,response.body().toString());
                        TranResultBean resultBean = GsonUtils.getInstance().GsonToBean(response.body().toString(),TranResultBean.class);
                        if(resultBean.getStatus() == Constants.REQUEST_SUCCESS){
                            Log.e(TAG,"请求成功");
                            if(lin_tran_result.getVisibility() == View.GONE){
                                lin_tran_result.setVisibility(View.VISIBLE);
                            }

                            String tranText = resultBean.getTranslateResult().getText();
                            String tranAudio = resultBean.getTranslateResult().getAudio();
                            if(!TextUtils.isEmpty(tranText)){
                                tv_tran_result.setText(tranText);
                            }
                            if (!tranAudio.endsWith(".mp3")) {
                                Log.e(TAG, "语音合成出错！！！！！！");
                                   /* if (rightIsStartVoice) {
                                        rightRecorderBean.setmFilePath("");
                                        mRecorderList.add(rightRecorderBean);
                                    } else if (leftIsStartVoice) {
                                        leftRecorderBean.setmFilePath("");
                                        mRecorderList.add(leftRecorderBean);
                                    }*/
                                Toast.makeText(TranslateActivity.this,R.string.request_error,Toast.LENGTH_SHORT);
                                 /*   mAdapter.notifyDataSetChanged();
                                    mListViewChat.setSelection(mRecorderList.size() - tran_voice1);*/
                                //return;
                            } else {
                                //下载音频
                                requestAudio(tranAudio);
                            }
                            img_tran_recro.setClickable(true);
                            isTranslate = false;
                        }else if(resultBean.getStatus() == Constants.REQUEST_FAIL){
                            Log.e(TAG,"请求失败");
                            isTranslate = false;
                            img_tran_recro.setClickable(true);
                            Toast.makeText(TranslateActivity.this,R.string.request_error,Toast.LENGTH_SHORT).show();

                        }/*else if(resultBean.getStatus() == Constants.TRAN_ERROR){
                            Toast.makeText(TranslateActivity.this,R.string.tran_error,Toast.LENGTH_SHORT).show();
                        }*/
                        //img_tran_recro_bg.clearAnimation();
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        Log.e(TAG,"请求错误");
                        img_tran_recro.setClickable(true);
                        isTranslate = false;
                        Toast.makeText(TranslateActivity.this,R.string.request_error,Toast.LENGTH_SHORT).show();
                       // img_tran_recro_bg.clearAnimation();
                    }
                });
    }

    /**
     * 请求下载音频
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
                               /* new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mAudioMediaPlayManager.startPlayingUsePhone(file.getAbsolutePath());
                                    }
                                }).start();*/
                            //Log.e(TAG,mRecorderList.get(pos).getmFilePath());

                            mPlayPath = file.getAbsolutePath();
                           // mAudioMediaPlayManager.startPlayingUsePhone(file.getAbsolutePath());

                        }
                    }

                    @Override
                    public void onError(Response<File> response) {
                        super.onError(response);
                      Log.e(TAG,"播放错误");
                    }
                });
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            Log.e(TAG, "key back");
            if (InputTools.KeyBoard(mSearchBoxEditText)) {
                mInputMethodManager.hideSoftInputFromWindow(mSearchBoxEditText.getWindowToken(), 0);
            }
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

/*

    @Override
    public void onFinalResponseResult(String result) {
        Log.d(TAG,"结果是 ： "+result);
        mSearchBoxEditText.setText(result);
        requestTran(result);
        img_tran_recro_bg.clearAnimation();
    }

    @Override
    public void ononFinalResponseResultEmtity(String error) {
        img_tran_recro_bg.clearAnimation();
        img_tran_recro.setClickable(true);
    }

    @Override
    public void onError(String s) {
        img_tran_recro_bg.clearAnimation();
        img_tran_recro.setClickable(true);

    }

    @Override
    public void startInitSpeechRecognition() {

    }

    @Override
    public void getOnAudioEvent(boolean b) {

    }
*/

   /* @Override
    public void onMicroStartRecoderUseBluetoothEar() {

    }*/
    /**
     * 微软语音
     * @param mLanguage
     */
    public void initSpeechRecognition(String mLanguage) {

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
    @Override
    public void onPartialResponseReceived(String s) {
        Log.e(TAG,"onPartialResponseReceived "+s);
    }

    @Override
    public void onFinalResponseReceived(RecognitionResult recognitionResult) {
        Log.e(TAG,"onFinalResponseReceived ");
        if (recognitionResult.Results.length <= 0) {
            img_tran_recro_bg.clearAnimation();
            img_tran_recro.setClickable(true);
            Toast.makeText(this,R.string.recognition_failed,Toast.LENGTH_SHORT).show();
            return;
        }
        String result = recognitionResult.Results[0].DisplayText;
        mSearchBoxEditText.setText(result);
        requestTran(result);
        img_tran_recro_bg.clearAnimation();
    }

    @Override
    public void onIntentReceived(String s) {
        Log.e(TAG,"onIntentReceived"+s);
    }

    @Override
    public void onError(int i, String s) {
        Log.e(TAG,"onError"+s+" i "+i);
        img_tran_recro_bg.clearAnimation();
        img_tran_recro.setClickable(true);
        Toast.makeText(this,R.string.record_fail,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAudioEvent(boolean b) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Constants.LANGUAGE_CHANGE){
            leftLanguage = SpUtils.getString(this,Constants.LEFT_LANGUAGE,Constants.zh_CN);
            rightLanguage = SpUtils.getString(this,Constants.RIGHT_LANGUAGE,Constants.en_US);
            Utils.perseLanguage(this,leftLanguage,leftlanguage_head,text_tran_left_language);
            Utils.perseLanguage(this,rightLanguage,rightlanguage_head,text_tran_right_language);
            Log.d(TAG,"mIntentType : "+mIntentType);
            if(mIntentType == 0){
                showKeyBord();
            }
        }
        // 拒绝时, 关闭页面, 缺少主要权限, 无法运行
        /*if (requestCode == PERMISSIONS_REQUEST_CODE && resultCode == PermissionsActivity.PERMISSIONS_DENIED) {
            finish();
        }*/
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
       /* mMicroRecogitionManager.releseClient();
        if(null != mMicroRecogitionManager){
            Log.e(TAG," mMicroRecogitionManager not null ");
            mMicroRecogitionManager = null;
       }else{
            Log.e(TAG," mMicroRecogitionManager null{\n");
        }*/
    }
}
