package com.wtwd.translate.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * time:2017/12/27
 * Created by w77996
 */
public class TranslateActivity extends Activity implements View.OnClickListener,AudioStateChange{

    private static final String TAG = "TranslateActivity";
    /**
     * 输入框
     */
    private EditText mSearchBoxEditText;
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
     *底部LinearLayout
     */
    private LinearLayout lin_tran_bottom;
    /**
     *
     * EditText输入框外部linearlayout
     */
    private LinearLayout lin_tran_edit;
    /**
     * 进入类型
     */
    private  int mIntentType;
    /**
     * 输入框内的清除按键
     */
    private ImageView mEditClearImageView;
    /**
     * 横向的语言选择
     */
    private RecyclerView mTranSelectRecylerView;
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
     * 是否按下返回键
     */
    boolean isBack = false;
    /**
     * 翻译界面Linearlayout
     */
    LinearLayout lin_tran;
    LanguageSelectListViewAdapter mAdapter;

    List<SelectBean> datas = new ArrayList<SelectBean>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translate);
        initDatas();
        initView();
        addListener();

    }

    /**
     * 初始化数据
     */
    private void initDatas() {
        final String[] countryText = {"中文[CHS]","英语[ENG]","法语[FRA]","德语[DEU]","韩语[KOR]","日语[JPN]","西班牙语[SPA]","葡萄牙语[POR]","意大利语[ITA]",
                "俄罗斯语[RUS]","泰语[THA]","印度语[HIN]"};
        datas = new ArrayList<>();
        for(int i = 0; i< countryText.length;i++){
            SelectBean selectBean = new SelectBean();
            selectBean.setData(countryText[i]);
            selectBean.setSelect(false);
            datas.add(selectBean);
        }
        Intent intent = getIntent();
        String intent_type = intent.getStringExtra("intent_type");
        Log.d(TAG,"intent_type "+ intent_type);
        if(intent_type.equals(Constants.INTENT_TRANT)){
            mIntentType = 0;//文字翻译
        }else{
            mIntentType = 1;//语音翻译
        }
    }

    /**
     * 初始化界面
     */
    private void initView(){
        Utils.setWindowStatusBarColor(this, R.color.main_title_color);
        lin_tran_edit = (LinearLayout)findViewById(R.id.lin_edit);
        lin_tran_bottom = (LinearLayout)findViewById(R.id.lin_tran_bottom);
        lin_tran = (LinearLayout)findViewById(R.id.lin_tran);
        mSearchBoxEditText = (EditText)findViewById(R.id.search_box_ed);
        if(mIntentType == 1){
            lin_tran_edit.setFocusable(true);
            //InputTools.KeyBoard(mSearchBoxEditText,"close");
            InputTools.KeyBoard(mSearchBoxEditText,"close");
           //mSearchBoxEditText.setFocusableInTouchMode(false);
           // InputTools.HideKeyboard(lin_tran_edit);
            Log.d(TAG,"hide");
            lin_tran_bottom.setVisibility(View.VISIBLE);
            lin_tran_bottom.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,mKeybordHight));
        }

        mSearchBoxImageView = (ImageView)findViewById(R.id.search_box_img);
        mTranSelectRecylerView = (RecyclerView)findViewById(R.id.rl_tran_type);
        mEditClearImageView = (ImageView)findViewById(R.id.img_tran_ed_clear);



        mTranScrollView = (ScrollView)findViewById(R.id.scrol_tran);
        lin_tran_text = (LinearLayout)findViewById(R.id.lin_tran_keybord) ;
        lin_tran_vocie = (LinearLayout) findViewById(R.id.lin_tran_voice);
        mTranSelectRecylerView.setLayoutManager(new LinearLayoutManager(TranslateActivity.this, LinearLayoutManager.HORIZONTAL, true));
        Collections.reverse(datas);
         mAdapter = new LanguageSelectListViewAdapter(TranslateActivity.this, datas);

        mTranSelectRecylerView.setAdapter(mAdapter);
        mKeybordHight = Integer.valueOf(SpUtils.getString(TranslateActivity.this,"keybord_hight",0+""));
        //InputTools.ShowKeyboard(mSearchBoxEditText);
        //横向的语言选择
        mAdapter.setOnItemClickLitener(new LanguageSelectListViewAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(TranslateActivity.this, position + " click",
                        Toast.LENGTH_SHORT).show();
                for (int i = 0; i < datas.size(); i++) {
                    if(i == position){
                        datas.get(i).setSelect(true);
                    }else{
                        datas.get(i).setSelect(false);
                    }
                }
                mAdapter.notifyDataSetChanged();
                mTranSelectRecylerView.scrollToPosition(position);

            }
        });
        //EditText输入框的监听，显示清除按钮
        mSearchBoxEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //清除按钮，若EditText中内容为空则清除内容
                String keyword = s.toString();
                if(!TextUtils.isEmpty(keyword)){
                    mEditClearImageView.setVisibility(View.VISIBLE);
                }else{
                    mEditClearImageView.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //输入框焦点监听
        mSearchBoxEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    Toast.makeText(TranslateActivity.this,"得到焦点",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(TranslateActivity.this,"失去焦点",Toast.LENGTH_SHORT).show();
                }
            }
        });
        mSearchBoxEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    finish();
                }
                Log.d(TAG,"action down "+keyCode);
                return false;
            }
        });
       //软键盘的监听，弹出和隐藏
        SoftKeyBoardListener.setListener(this,new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
            @Override
            public void keyBoardShow(int height) {
                Toast.makeText(TranslateActivity.this, "键盘显示 高度" + height, Toast.LENGTH_SHORT).show();


               lin_tran_bottom.setVisibility(View.GONE);
                mSearchBoxEditText.setFocusable(true);
                Log.d(TAG,"键盘高度为"+height);
            }

            @Override
            public void keyBoardHide(int height) {
                Toast.makeText(TranslateActivity.this, "键盘隐藏 高度" + height, Toast.LENGTH_SHORT).show();
                // textView.setText("高度："+String.valueOf(height));
               /* if(mIntentType == 0){
                    InputTools.ShowKeyboard(mSearchBoxEditText);
                }*/
               // if(mIntentType == 1){
                    lin_tran_edit.setFocusable(true);
                    lin_tran_bottom.setVisibility(View.VISIBLE);
                    lin_tran_bottom.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,mKeybordHight));
              //  }
               // mSearchBoxEditText.setFocusable(false);


            }
        });
       /* final View rootView = ((Activity) TranslateActivity.this).getWindow().getDecorView();
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                rootView.getWindowVisibleDisplayFrame(r);
                int visibleHeight = r.height() + r.top;
                if (visibleHeight < rootView.getHeight()) {
                    // 软键盘显示ing
                    Toast.makeText(TranslateActivity.this,"显示",Toast.LENGTH_SHORT).show();
                }else{
                    // 软键盘收起,点输入法的收起后会走到这边
                    Toast.makeText(TranslateActivity.this,"收起",Toast.LENGTH_SHORT).show();
                }
            }
        });*/
      //  switchKeyBord();
       // InputTools.HideKeyboard(mSearchBoxEditText);

        //Log.d(TAG,Utils.getBottomStatusHeight(TranslateActivity.this)+"");
        //View child = new View(this);
       /* LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) lin_tran_bottom.getLayoutParams();
        layoutParams.width = mKeybordHight;*/
        switchKeyBord();
        mTranSelectRecylerView.scrollToPosition(11);

    }

    /**
     * 是否弹出软键盘
     */
    private void switchKeyBord() {
        if( mIntentType == 0){
            lin_tran_bottom.setVisibility(View.GONE);
            mSearchBoxEditText.setFocusable(true);
            InputTools.KeyBoard(mSearchBoxEditText,"open");

        }else{
            //
           /* mSearchBoxEditText.setFocusable(false);
            mSearchBoxEditText.setFocusableInTouchMode(false);*/
            lin_tran_edit.setFocusable(true);
            InputTools.KeyBoard(mSearchBoxEditText,"close");

        }
    }

    private void showKeyBord(){
        lin_tran_bottom.setVisibility(View.GONE);
        mSearchBoxEditText.setFocusable(true);
        InputTools.KeyBoard(mSearchBoxEditText,"open");
    }
    private void showVoice(){
        lin_tran_edit.setFocusable(true);
        InputTools.KeyBoard(mSearchBoxEditText,"close");
    }
    /**
     * 添加监听事件
     */
    private void addListener(){

        mEditClearImageView.setOnClickListener(this);
        lin_tran_text.setOnClickListener(this);
        lin_tran_vocie.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_tran_ed_clear:
                mSearchBoxEditText.setText("");
                break;
            case R.id.lin_tran_keybord:
                Log.d(TAG,"选择文本翻译");
                mIntentType = 0;
                //switchKeyBord();
                showKeyBord();
                break;
            case R.id.lin_tran_voice:
                Log.d(TAG,"选择语音翻译");
                mIntentType= 1;
                //switchKeyBord();
                showVoice();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        isBack = true;
        finish();
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
}
