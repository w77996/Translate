package com.wtwd.translate.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
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
import android.view.inputmethod.InputMethodManager;
import android.view.inputmethod.InputMethodSubtype;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.wtwd.translate.R;
import com.wtwd.translate.adapter.LanguageSelectListViewAdapter;
import com.wtwd.translate.bean.SelectBean;
import com.wtwd.translate.utils.Constants;
import com.wtwd.translate.utils.SpUtils;
import com.wtwd.translate.utils.audio.AudioStateChange;
import com.wtwd.translate.utils.keybord.InputTools;
import com.wtwd.translate.utils.Utils;
import com.wtwd.translate.utils.keybord.SoftKeyBoardListener;
import com.wtwd.translate.utils.micro.MicroRecogitionManager;
import com.wtwd.translate.view.InputEdittext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * time:2017/12/27
 * Created by w77996
 */
public class TranslateActivity extends Activity implements View.OnClickListener,MicroRecogitionManager.MicroRecogitionManagerCallBack{

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
    /**
     * 标题栏按钮文字
     */
    TextView text_tran_left_language;
    TextView text_tran_right_language;

    ImageView leftlanguage_head;
    ImageView rightlanguage_head;
    ImageView img_tran_switch;

    String leftLanguage;
    String rightLanguage;


  //  List<SelectBean> datas = new ArrayList<SelectBean>();

    ImageView tran_back;

    private InputMethodManager mInputMethodManager;


    MicroRecogitionManager mMicroRecogitionManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translate);
        initDatas();
        initView();
        addListener();
        mMicroRecogitionManager = MicroRecogitionManager.getMicroRecogitionManager(this);
        mMicroRecogitionManager.setmicroRecogitionManagerCallBack(this);
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
        mKeybordHight = Integer.valueOf(SpUtils.getString(TranslateActivity.this, "keybord_hight", 0 + ""));
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


        text_tran_left_language = (TextView)findViewById(R.id.text_tran_left_language);
        text_tran_right_language = (TextView)findViewById(R.id.text_tran_right_language);
        leftlanguage_head = (ImageView)findViewById(R.id.leftlanguage_head);
        rightlanguage_head = (ImageView)findViewById(R.id.rightlanguage_head);
        img_tran_switch = (ImageView)findViewById(R.id.img_tran_switch);

        tran_back = (ImageView)findViewById(R.id.tran_back);

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
        lin_tran_text.setOnClickListener(this);
        lin_tran_vocie.setOnClickListener(this);
        img_tran_recro.setOnClickListener(this);
        leftlanguage_head.setOnClickListener(this);
        rightlanguage_head.setOnClickListener(this);
        img_tran_switch.setOnClickListener(this);
        tran_back.setOnClickListener(this);
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
                mMicroRecogitionManager.initSpeechRecognition(leftLanguage);
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
                Utils.perseLanguage(TranslateActivity.this, leftLanguage,leftlanguage_head,text_tran_left_language);
                Utils.perseLanguage(TranslateActivity.this,rightLanguage,rightlanguage_head,text_tran_right_language);
                break;
            case R.id.tran_back:
                finish();
                break;
        }
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


    @Override
    public void onFinalResponseResult(String result) {
        Log.d(TAG,"结果是 ： "+result);
        mSearchBoxEditText.setText(result);
    }

    @Override
    public void ononFinalResponseResultEmtity(String error) {

    }

    @Override
    public void onError(String s) {

    }

   /* @Override
    public void onMicroStartRecoderUseBluetoothEar() {

    }*/

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
    }
}
