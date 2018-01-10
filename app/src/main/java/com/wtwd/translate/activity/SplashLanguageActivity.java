package com.wtwd.translate.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.wtwd.translate.R;
import com.wtwd.translate.adapter.LanguageGrideViewAdapter;
import com.wtwd.translate.bean.SelectBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 母语选择页
 * time:2018/1/9
 * Created by w77996
 */
public class SplashLanguageActivity extends Activity {

    private List<SelectBean>  mSelectBeanList;
    private LanguageGrideViewAdapter mLanguageGrideViewAdapter;
    private GridView mGridView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashlanguage);
        initView();
    }

    private void initView(){
        final String[] countryText = {"中文[CHS]","英语[ENG]","法语[FRA]","德语[DEU]","韩语[KOR]","日语[JPN]","西班牙语[SPA]","葡萄牙语[POR]","意大利语[ITA]",
                                "俄罗斯语[RUS]","泰语[THA]","印度语[HIN]"};
        int[] countryImage = {R.drawable.flag_china,R.drawable.flag_eng,R.drawable.flag_fra,R.drawable.flag_deu,R.drawable.flag_kor,R.drawable.flag_jpn,
                                R.drawable.flag_spa,R.drawable.flag_por,R.drawable.flag_ita,R.drawable.flag_rus,R.drawable.flag_tha,R.drawable.flag_hin};

        mSelectBeanList = new ArrayList<>();
        for(int i = 0; i< countryText.length;i++){
            SelectBean selectBean = new SelectBean();
            selectBean.setData(countryText[i]);
            selectBean.setSelect(false);
            mSelectBeanList.add(selectBean);
        }

        mGridView = (GridView)findViewById(R.id.gv_language);
        mLanguageGrideViewAdapter = new LanguageGrideViewAdapter(SplashLanguageActivity.this,mSelectBeanList,countryImage);
        mGridView.setAdapter(mLanguageGrideViewAdapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //mGridView.setBackgroundColor(getResources().getColor(R.color.color_white));

               //LinearLayout linearLayout=(LinearLayout) mGridView.getAdapter().get(position);
               /* for(int i = 0;i < mSelectBeanList.size();i++){
                        //if(mSelectBeanList.get(i).getisSelect()){
                            mSelectBeanList.get(i).setSelect(false);
                            LinearLayout linearLayoutItem=(LinearLayout) mGridView.getAdapter().getView(i,view,null);
                            linearLayoutItem.setBackgroundColor(getResources().getColor(R.color.color_white));
                    mLanguageGrideViewAdapter.notifyDataSetChanged();
                      //  }else{

                      //  }
                }*/
               LinearLayout linearLayout = (LinearLayout)view.findViewById(R.id.linear_flag);
               /* for(int i = 0;i < mSelectBeanList.size();i++){
                    //if(mSelectBeanList.get(i).getisSelect()){
                    mSelectBeanList.get(i).setSelect(false);
                    //LinearLayout linearLayoutItem=(LinearLayout) mGridView.getAdapter().getView(i,view,null);
                    linearLayout.setBackgroundColor(getResources().getColor(R.color.color_white));

                    //  }else{

                    //  }
                }
                mLanguageGrideViewAdapter.notifyDataSetChanged();*/
               if(!mSelectBeanList.get(position).isSelect){

                    linearLayout.setBackgroundColor(getResources().getColor(R.color.main_title_color));
                    mSelectBeanList.get(position).setSelect(true);
                }else{
                    linearLayout.setBackgroundColor(getResources().getColor(R.color.color_white));
                    mSelectBeanList.get(position).setSelect(false);
                }
                mLanguageGrideViewAdapter.notifyDataSetChanged();



                //LinearLayout linearLayout=(LinearLayout) mGridView.getAdapter().getView(position,view,null);
               // mSelectBeanList.get(position).setSelect(true);
               // linearLayout.setBackgroundColor(getResources().getColor(R.color.main_title_color));
               // mLanguageGrideViewAdapter.notifyDataSetChanged();
            }
        });
    }
}
