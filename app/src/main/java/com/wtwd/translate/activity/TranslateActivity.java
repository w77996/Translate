package com.wtwd.translate.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wtwd.translate.R;
import com.wtwd.translate.adapter.LanguageSelectListViewAdapter;
import com.wtwd.translate.bean.SelectBean;
import com.wtwd.translate.utils.Constants;
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
public class TranslateActivity extends Activity implements View.OnClickListener{

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
            mIntentType = 0;
        }else{
            mIntentType = 1;
        }
    }

    /**
     * 初始化界面
     */
    private void initView(){
        Utils.setWindowStatusBarColor(this, R.color.main_title_color);
        mSearchBoxEditText = (EditText)findViewById(R.id.search_box_ed);
        mSearchBoxImageView = (ImageView)findViewById(R.id.search_box_img);
        mTranSelectRecylerView = (RecyclerView)findViewById(R.id.rl_tran_type);
        mEditClearImageView = (ImageView)findViewById(R.id.img_tran_ed_clear);
        lin_tran_bottom = (LinearLayout)findViewById(R.id.lin_tran_bottom);

        mTranSelectRecylerView.setLayoutManager(new LinearLayoutManager(TranslateActivity.this, LinearLayoutManager.HORIZONTAL, true));
        Collections.reverse(datas);
         mAdapter = new LanguageSelectListViewAdapter(TranslateActivity.this, datas);

        mTranSelectRecylerView.setAdapter(mAdapter);

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
        //软键盘的监听，弹出和隐藏
        SoftKeyBoardListener.setListener(this,new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
            @Override
            public void keyBoardShow(int height) {
                Toast.makeText(TranslateActivity.this, "键盘显示 高度" + height, Toast.LENGTH_SHORT).show();
                //  textView.setText(String.valueOf(height));
                mKeybordHight = height;
            }

            @Override
            public void keyBoardHide(int height) {
                Toast.makeText(TranslateActivity.this, "键盘隐藏 高度" + height, Toast.LENGTH_SHORT).show();
                // textView.setText("高度："+String.valueOf(height));
            }
        });


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
            InputTools.ShowKeyboard(mSearchBoxEditText);
        }else{
            //
           /* mSearchBoxEditText.setFocusable(false);
            mSearchBoxEditText.setFocusableInTouchMode(false);*/
            lin_tran_bottom.setVisibility(View.VISIBLE);
            lin_tran_bottom.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,mKeybordHight));
            InputTools.HideKeyboard(mSearchBoxEditText);

        }
    }

    /**
     * 添加监听事件
     */
    private void addListener(){

        mEditClearImageView.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_tran_ed_clear:
                mSearchBoxEditText.setText("");
                break;
        }
    }
}
