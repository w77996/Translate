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

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * 母语选择页
 * time:2018/1/9
 * Created by w77996
 */
public class SplashLanguageActivity extends Activity implements View.OnClickListener{

    private List<SelectBean>  mSelectBeanList;
    private LanguageGrideViewAdapter mLanguageGrideViewAdapter;
    private GridView mGridView;
    private TextView mIgnoreText;
    private ImageView mImgNext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashlanguage);
        initView();
        addListener();
    }


    private void initView(){
        final String[] countryText = {"中文[CHS]","英语[ENG]","法语[FRA]","德语[DEU]","韩语[KOR]","日语[JPN]","西班牙语[SPA]","葡萄牙语[POR]","意大利语[ITA]",
                                "俄罗斯语[RUS]","泰语[THA]","印度语[HIN]"};
        int[] countryImage = {R.drawable.flag_china,R.drawable.flag_eng,R.drawable.flag_fra,R.drawable.flag_deu,R.drawable.flag_kor,R.drawable.flag_jpa,
                                R.drawable.flag_spa,R.drawable.flag_por,R.drawable.flag_ita,R.drawable.flag_rus,R.drawable.flag_tha,R.drawable.flag_hin};

        mSelectBeanList = new ArrayList<>();
        for(int i = 0; i< countryText.length;i++){
            SelectBean selectBean = new SelectBean();
            selectBean.setData(countryText[i]);
            selectBean.setSelect(false);
            mSelectBeanList.add(selectBean);
        }
        mIgnoreText = (TextView)findViewById(R.id.language_ignore);
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

                mLanguageGrideViewAdapter.notifyDataSetChanged();

            }
        });
    }

    /**
     * 添加监听事件
     */
    private void addListener() {

        mIgnoreText.setOnClickListener(this);
        mImgNext.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.language_ignore:
                break;
            case R.id.img_language_next:
                Intent splashDevBindIntent = new Intent(SplashLanguageActivity.this,SplashDevBindActivity.class);
                startActivity(splashDevBindIntent);
                finish();
                break;
        }
    }
}
