package com.wtwd.translate.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wtwd.translate.R;
import com.wtwd.translate.adapter.LabelGrideViewAdapter;
import com.wtwd.translate.adapter.LabelRecycleViewAdapter;
import com.wtwd.translate.adapter.LanguageSelectListViewAdapter;
import com.wtwd.translate.adapter.SpaceItemDecoration;
import com.wtwd.translate.bean.SelectBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 标签选择页
 * time:2018/1/9
 * Created by w77996
 */
public class SplashLableActivity extends Activity implements View.OnClickListener{

    /**标签列表**/
    private GridView mGridView;

    LabelGrideViewAdapter mAdapter;

    /**跳过**/
   // private TextView tv_ignore;
    private ImageView img_next;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashlable);
        initView();
        addLisener();
    }

    /**
     * 初始化界面控件
     */
    private void initView(){
        final List<SelectBean> data = new ArrayList<>();
        String[] tvdata = {"打工族","数码控","财经迷","八卦党","二次元","白富美","运动达人","爱车一族","宝妈宝爸","成熟中年","80后","90后"};
        for(int i =0;i<tvdata.length;i++){
            SelectBean selectBean = new SelectBean();
            selectBean.data = tvdata[i];
            selectBean.isSelect = false;
            data.add(selectBean);
        }
        //tv_ignore = (TextView)findViewById(R.id.label_ignore);
        img_next = (ImageView)findViewById(R.id.img_label_next);
        mGridView = (GridView)findViewById(R.id.mygridview);
         mAdapter = new LabelGrideViewAdapter(getApplicationContext(),data);
        mGridView.setAdapter(mAdapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                TextView textView =(TextView)view.findViewById(R.id.rl_item_tv);
                ImageView imageView =(ImageView)view.findViewById(R.id.rl_item_btn);


                boolean select = data.get(position).getisSelect();

                if(select){
                    textView.setTextColor(Color.parseColor("#7a7a7a"));
                    imageView.setBackground(getApplication().getResources().getDrawable(R.drawable.rl_item_normal));
                    data.get(position).isSelect = false;
                    Log.d("444", "ture: ");
                }else{
                    textView.setTextColor(Color.parseColor("#FFFFFF"));
                    imageView.setBackground(getApplication().getResources().getDrawable(R.drawable.rl_item_select));
                    data.get(position).isSelect = true;
                    Log.d("444", "false");
                }
            }
        });
    }
    private void addLisener(){
       // tv_ignore.setOnClickListener(this);
        img_next.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            /*case R.id.label_ignore:
                break;*/
            case R.id.img_label_next:
                Intent splashIntent = new Intent(SplashLableActivity.this,SplashLanguageActivity.class);
                startActivity(splashIntent);
                finish();
                break;
        }
    }
}
