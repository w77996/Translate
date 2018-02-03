package com.wtwd.translate.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wtwd.translate.R;
import com.wtwd.translate.adapter.LanguageGrideViewAdapter;
import com.wtwd.translate.bean.SelectBean;
import com.wtwd.translate.utils.Constants;
import com.wtwd.translate.utils.SpUtils;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * 母语选择页
 * time:2018/tran_voice1/9
 * Created by w77996
 */
public class SplashLanguageActivity extends Activity implements View.OnClickListener{

    /**
     * 数据bean
     */
    private List<SelectBean>  mSelectBeanList;
    /**
     * 中间的GridView
     */
    private LanguageGrideViewAdapter mLanguageGrideViewAdapter;
    private GridView mGridView;
    /**
     * 跳过
     */
   // private TextView mIgnoreText;
    /**
     * 下一步
     */
    private ImageView mImgNext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashlanguage);
        initView();
        addListener();
    }


    private void initView(){
        final String[] countryText = {getString(R.string.label_china),getString(R.string.label_english),getString(R.string.label_fra),getString(R.string.label_deu),getString(R.string.label_kor),getString(R.string.label_jpn),getString(R.string.label_spa),getString(R.string.lael_por),
                                getString(R.string.label_rus)};
        int[] countryImage = {R.drawable.flag_china,R.drawable.flag_eng,R.drawable.flag_fra,R.drawable.flag_deu,R.drawable.flag_kor,R.drawable.flag_jpa,
                                R.drawable.flag_spa,R.drawable.flag_por,R.drawable.flag_rus};
        String [] languageType = {Constants.zh_CN,Constants.en_US,Constants.fr_FR,Constants.de_DE,Constants.ko_KR,Constants.ja_JP,Constants.es_ES,Constants.pt_PT,Constants.ru_RU};
        mSelectBeanList = new ArrayList<>();
        for(int i = 0; i< countryText.length;i++){

            SelectBean selectBean = new SelectBean();
            selectBean.setData(countryText[i]);
            selectBean.setLanguageType(languageType[i]);
            if(i == 0){
                selectBean.setSelect(true);
            }else{
                selectBean.setSelect(false);
            }
            mSelectBeanList.add(selectBean);
        }
       // mIgnoreText = (TextView)findViewById(R.id.language_ignore);
        mImgNext = (ImageView)findViewById(R.id.img_language_next);
        mGridView = (GridView)findViewById(R.id.gv_language);
        mLanguageGrideViewAdapter = new LanguageGrideViewAdapter(SplashLanguageActivity.this,mSelectBeanList,countryImage);
        mGridView.setAdapter(mLanguageGrideViewAdapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                for (int i = 0; i < mSelectBeanList.size(); i++) {
                    if(i == position){
                        mSelectBeanList.get(i).setSelect(true);
                    }else{
                        mSelectBeanList.get(i).setSelect(false);
                    }
                }
                //保存母语
                SpUtils.putString(SplashLanguageActivity.this,Constants.LEFT_LANGUAGE,mSelectBeanList.get(position).getLanguageType());
                mLanguageGrideViewAdapter.notifyDataSetChanged();

            }
        });
    }

    /**
     * 添加监听事件
     */
    private void addListener() {

       // mIgnoreText.setOnClickListener(this);
        mImgNext.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
           /* case R.id.language_ignore:
                break;*/
            case R.id.img_language_next:
                Intent splashDevBindIntent = new Intent(SplashLanguageActivity.this,SplashDevBindActivity.class);
                startActivity(splashDevBindIntent);
                finish();
                break;
        }
    }
}
