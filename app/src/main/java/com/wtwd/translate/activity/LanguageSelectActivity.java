package com.wtwd.translate.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.print.PrintDocumentInfo;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.wtwd.translate.R;
import com.wtwd.translate.adapter.LanguageSelectAdapter;
import com.wtwd.translate.bean.RecorderBean;
import com.wtwd.translate.bean.SelectBean;
import com.wtwd.translate.utils.Constants;
import com.wtwd.translate.utils.SpUtils;
import com.wtwd.translate.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * time:2018/1/16
 * Created by w77996
 */

public class LanguageSelectActivity extends Activity implements View.OnClickListener{


    private final static String TAG = "LanguageSelectActivity";
    /**
     *左侧选择按钮
     */
    LinearLayout lin_left;
    /**
     * 右侧选择按钮
     */
    LinearLayout lin_right;

    /**
     *  左侧头像
     */
    ImageView language_select_left_head;
    /**
     *右侧头像
     */
    ImageView language_select_right_head;
    /**
     * 左侧语言
     */
    TextView language_select_left_text;
    /**
     *右侧语言
     */
    TextView language_select_right_text;

    /**
     * 返回
     */
    ImageView language_select_back;
    /**
     * 完成
     */
   /* TextView main_title_finish;*/
    List<SelectBean> mLanguageList;

    ListView lv_language_select;
    LanguageSelectAdapter mLanguageSelectAdapter;
    private String leftLanguage;
    private String rightLanguage;
    private String[] countryText =  {"中文[CHS]","英语[ENG]","法语[FRA]","德语[DEU]","韩语[KOR]","日语[JPN]","西班牙语[SPA]","葡萄牙语[POR]",
            "俄罗斯语[RUS]"};
    private int[] countryImage = {R.drawable.flag_china,R.drawable.flag_eng,R.drawable.flag_fra,R.drawable.flag_deu,R.drawable.flag_kor,R.drawable.flag_jpa,
            R.drawable.flag_spa,R.drawable.flag_por,R.drawable.flag_rus};
    private String[] languageType = {Constants.zh_CN,Constants.en_US,Constants.fr_FR,Constants.de_DE,Constants.ko_KR,Constants.ja_JP,Constants.es_ES,Constants.pt_PT,Constants.ru_RU};

    int derect ;//点击左边
    int type ;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_languageselect);
        initData();
        initView();
        addListener();
    }

    /**
     * 初始化界面控件
     */
    private void initView() {
        lin_left = (LinearLayout)findViewById(R.id.lin_left);//左侧选择语言linear
        lin_right = (LinearLayout)findViewById(R.id.lin_right);//右侧选择语言linear
        language_select_left_head = (ImageView)findViewById(R.id.language_select_left_head);//左侧选择语言头像
        language_select_right_head = (ImageView)findViewById(R.id.language_select_right_head);//右侧选择语言头像
        language_select_left_text = (TextView)findViewById(R.id.language_select_left_text);
        language_select_right_text = (TextView)findViewById(R.id.language_select_right_text);
        lv_language_select = (ListView)findViewById(R.id.lv_language_select);//listview

        if(derect == 0){
                lin_left.setBackground(getDrawable(R.drawable.language_select_left_selected));
                lin_right.setBackground(getDrawable(R.drawable.language_select_right_unselect));
                language_select_left_text.setTextColor(getApplicationContext().getResources().getColor(R.color.color_white));
                language_select_right_text.setTextColor(getApplicationContext().getResources().getColor(R.color.md_black_color_code));

        }else{
            lin_right.setBackground(getDrawable(R.drawable.language_select_right_selected));
            lin_left.setBackground(getDrawable(R.drawable.language_select_left_unselect));
            language_select_left_text.setTextColor(getApplicationContext().getResources().getColor(R.color.md_black_color_code));
            language_select_right_text.setTextColor(getApplicationContext().getResources().getColor(R.color.color_white));
        }

       // main_title_finish = (TextView)findViewById(R.id.main_title_finish);//完成
        language_select_back = (ImageView)findViewById(R.id.language_select_back);//返回
        if(type == Constants.LANGUAGE_SELECT_NORMAL_TYPE){
            Utils.perseLanguage(LanguageSelectActivity.this,leftLanguage,language_select_left_head,language_select_left_text);
            Utils.perseLanguage(LanguageSelectActivity.this,rightLanguage,language_select_right_head,language_select_right_text);
        }else if (type == Constants.LANGUAGE_SELECT_DEV_TYPE){
            language_select_left_text.setText("设备语言");
            language_select_right_text.setText("手机语言");

            if(derect == 0){
                //左边
                language_select_left_head.setImageDrawable(getDrawable(R.drawable.language_select_dev_select));
                language_select_right_head.setImageDrawable(getDrawable(R.drawable.language_select_mobile_unselect));
               // Utils.perseLanguage(LanguageSelectActivity.this,leftLanguage,language_select_left_head,language_select_left_text);
               // Utils.perseLanguage(LanguageSelectActivity.this,rightLanguage,language_select_right_head,language_select_right_text);
            }else{
                //右边

                language_select_right_head.setImageDrawable(getDrawable(R.drawable.language_select_mobile_select));
                language_select_left_head.setImageDrawable(getDrawable(R.drawable.language_select_dev_unselect));


            }
        }

        mLanguageSelectAdapter = new LanguageSelectAdapter(this,mLanguageList);
        lv_language_select.setAdapter(mLanguageSelectAdapter);
        lv_language_select.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (int i = 0; i < mLanguageList.size(); i++) {
                    if(i == position){
                        mLanguageList.get(i).setSelect(true);
                    }else{
                        mLanguageList.get(i).setSelect(false);
                    }
                }
//                if(type == Constants.LANGUAGE_SELECT_NORMAL_TYPE){
//
//                }
                if(derect == 0){//左侧语言选择
                    SpUtils.putString(LanguageSelectActivity.this,Constants.LEFT_LANGUAGE,mLanguageList.get(position).getLanguageType());
                    Utils.perseLanguage(LanguageSelectActivity.this,mLanguageList.get(position).getLanguageType(),language_select_left_head,language_select_left_text);
                    setResult(Constants.LANGUAGE_CHANGE);
                    //mLanguageList.get(position).setSelect(true);
                }else if(derect == 1){//右侧语言选择
                    SpUtils.putString(LanguageSelectActivity.this,Constants.RIGHT_LANGUAGE,mLanguageList.get(position).getLanguageType());
                    Utils.perseLanguage(LanguageSelectActivity.this,mLanguageList.get(position).getLanguageType(),language_select_right_head,language_select_right_text);
                    setResult(Constants.LANGUAGE_CHANGE);
                }
                finish();
                //mLanguageSelectAdapter.notifyDataSetChanged();

            }
        });
    }

    /**
     * 添加监听器
     */
    private void addListener(){
        lin_right.setOnClickListener(this);
        lin_left.setOnClickListener(this);
        language_select_back.setOnClickListener(this);
       // main_title_finish.setOnClickListener(this);

    }

    /**
     * 初始化数据
     */
    private void initData() {
        mLanguageList = new ArrayList<>();
        Intent derectIntent = getIntent();
        derect = derectIntent.getIntExtra(Constants.DETRECT,Constants.DETRECT_LEFT);
        type = derectIntent.getIntExtra(Constants.LANGUAGE_SELECT_TYPE,Constants.LANGUAGE_SELECT_NORMAL_TYPE);
        leftLanguage = SpUtils.getString(this,Constants.LEFT_LANGUAGE, Constants.zh_CN);
        rightLanguage = SpUtils.getString(this,Constants.RIGHT_LANGUAGE,Constants.en_US);
        for (int i = 0; i < countryText.length; i++) {
            SelectBean selectBean = new SelectBean();
            selectBean.setImg(countryImage[i]);
            selectBean.setData(countryText[i]);
            selectBean.setLanguageType(languageType[i]);
            if(derect == 0){
                if(leftLanguage.equals(languageType[i])){
                    selectBean.setSelect(true);
                }else{
                    selectBean.setSelect(false);
                }
            }else{
                if(rightLanguage.equals(languageType[i])){
                    selectBean.setSelect(true);
                }else{
                    selectBean.setSelect(false);
                }
            }
            mLanguageList.add(selectBean);
        }
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.lin_left:
                Log.d(TAG,"点击左侧语音");
                //mLanguageList.clear();
                mLanguageList.clear();
                if(type == Constants.LANGUAGE_SELECT_NORMAL_TYPE){

                }else if (type == Constants.LANGUAGE_SELECT_DEV_TYPE){
                    language_select_left_head.setImageDrawable(getDrawable(R.drawable.language_select_dev_select));
                    language_select_right_head.setImageDrawable(getDrawable(R.drawable.language_select_mobile_unselect));
                }
                lin_left.setBackground(getDrawable(R.drawable.language_select_left_selected));
                lin_right.setBackground(getDrawable(R.drawable.language_select_right_unselect));
                language_select_left_text.setTextColor(getApplicationContext().getResources().getColor(R.color.color_white));
                language_select_right_text.setTextColor(getApplicationContext().getResources().getColor(R.color.md_black_color_code));


                derect =0;
                leftLanguage = SpUtils.getString(this,Constants.LEFT_LANGUAGE, Constants.zh_CN);
                for (int i = 0; i < countryText.length; i++) {
                    SelectBean selectBean = new SelectBean();
                    selectBean.setImg(countryImage[i]);
                    selectBean.setData(countryText[i]);
                    selectBean.setLanguageType(languageType[i]);
                    if(leftLanguage.equals(languageType[i])){
                        selectBean.setSelect(true);
                    }else{
                        selectBean.setSelect(false);
                    }
                    mLanguageList.add(selectBean);
                }
                mLanguageSelectAdapter.notifyDataSetChanged();
                break;
            case R.id.lin_right:
                Log.d(TAG,"点击右侧语言");

                if(type == Constants.LANGUAGE_SELECT_NORMAL_TYPE){

                }else if (type == Constants.LANGUAGE_SELECT_DEV_TYPE){
                    language_select_right_head.setImageDrawable(getDrawable(R.drawable.language_select_mobile_select));
                    language_select_left_head.setImageDrawable(getDrawable(R.drawable.language_select_dev_unselect));
                }
                   /* language_select_right_head.setImageDrawable(getDrawable(R.drawable.language_select_left_unselect));
                    language_select_left_head.setImageDrawable(getDrawable(R.drawable.language_select_right_selected));*/
                lin_right.setBackground(getDrawable(R.drawable.language_select_right_selected));
                lin_left.setBackground(getDrawable(R.drawable.language_select_left_unselect));
                language_select_left_text.setTextColor(getApplicationContext().getResources().getColor(R.color.md_black_color_code));
                language_select_right_text.setTextColor(getApplicationContext().getResources().getColor(R.color.color_white));

                derect =1;
                mLanguageList.clear();

                rightLanguage = SpUtils.getString(this,Constants.RIGHT_LANGUAGE,Constants.en_US);
               // mLanguageList.clear();
                for (int i = 0; i < countryText.length; i++) {
                    SelectBean selectBean = new SelectBean();
                    selectBean.setImg(countryImage[i]);
                    selectBean.setData(countryText[i]);
                    selectBean.setLanguageType(languageType[i]);
                    if(rightLanguage.equals(languageType[i])){
                        selectBean.setSelect(true);
                    }else{
                        selectBean.setSelect(false);
                    }
                    mLanguageList.add(selectBean);
                }
                mLanguageSelectAdapter.notifyDataSetChanged();
                break;
            /*case R.id.main_title_finish:
                setResult(Constants.LANGUAGE_CHANGE);
                finish();
                break;*/
            case R.id.language_select_back:
                finish();
                break;

        }

    }
}
