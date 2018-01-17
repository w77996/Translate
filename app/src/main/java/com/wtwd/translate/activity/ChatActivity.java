package com.wtwd.translate.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.wtwd.translate.R;
import com.wtwd.translate.adapter.DevTranListViewAdapter;
import com.wtwd.translate.bean.Recorder;
import com.wtwd.translate.bean.RecorderBean;
import com.wtwd.translate.utils.Constants;
import com.wtwd.translate.utils.SpUtils;
import com.wtwd.translate.utils.Utils;
import com.wtwd.translate.utils.audio.AudioMediaPlayManager;
import com.wtwd.translate.utils.audio.AudioStateChange;
import com.wtwd.translate.utils.permissions.PermissionsActivity;
import com.wtwd.translate.utils.permissions.PermissionsChecker;

import java.util.ArrayList;
import java.util.List;


/**
 * time:2017/12/27
 * Created by w77996
 */
public class ChatActivity extends Activity implements View.OnClickListener,AudioStateChange{

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

    String leftLanguage;
    String rightLanguage;

    /**
     * 按钮是否按下
     */
    boolean leftBtnPress = false;
    boolean rightBtnPress = false;
    boolean leftIsStartVoice =false;
    boolean rightIsStartVoice =false;

    private static final int PERMISSIONS_REQUEST_CODE = 0111; // 请求码

    private PermissionsChecker mPermissionsChecker; // 权限检测器

    // 所需的全部权限
    static final String[] PERMISSIONS = new String[]{
            Manifest.permission.RECORD_AUDIO
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
//        final String[] countryText = {"中文[CHS]","英语[ENG]","法语[FRA]","德语[DEU]","韩语[KOR]","日语[JPN]","西班牙语[SPA]","葡萄牙语[POR]",
//                "俄罗斯语[RUS]"};
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

        mAdapter = new DevTranListViewAdapter(this,mRecorderList);
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
                    mAudioMediaPlayManager.stopRecorderUsePhone();
                    leftIsStartVoice = false;
                    lin_right.setClickable(true);
                    lin_left.setBackground(this.getDrawable(R.drawable.chat_left_btn_unselect));

                    // TODO: 2018/1/17  停止录音后更新界面处理
                    RecorderBean recorderBean = new RecorderBean();
                    recorderBean.setmFilePath(mVoiceFilePath);
                    recorderBean.setType(Constants.ITEM_LEFT);
                    mRecorderList.add(recorderBean);
                    mAdapter.notifyDataSetChanged();
                }else{
                   // leftBtnPress = true;
                    mVoiceFilePath = Utils.getVoiceFilePath();
                    Log.d(TAG,"左侧录音路径 " +mVoiceFilePath);
                    mAudioMediaPlayManager.startRecorderUsePhone(mVoiceFilePath);
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
                    mAudioMediaPlayManager.stopRecorderUsePhone();
                    rightIsStartVoice = false;
                    lin_left.setClickable(true);
                    lin_right.setBackground(this.getDrawable(R.drawable.chat_right_btn_unselect));

                    // TODO: 2018/1/17  停止录音后更新界面处理
                    RecorderBean recorderBean = new RecorderBean();
                    recorderBean.setmFilePath(mVoiceFilePath);
                    recorderBean.setType(Constants.ITEM_RIGHT);
                    mRecorderList.add(recorderBean);
                    mAdapter.notifyDataSetChanged();
                }else{
                    // leftBtnPress = true;
                    mVoiceFilePath = Utils.getVoiceFilePath();
                    Log.d(TAG,"右侧录音路径 " +mVoiceFilePath);
                    mAudioMediaPlayManager.startRecorderUsePhone(mVoiceFilePath);
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

    }

    @Override
    public void onPlayCompletion() {

    }

    @Override
    public void onPlayError() {

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
