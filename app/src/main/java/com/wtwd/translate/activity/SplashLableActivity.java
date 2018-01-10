package com.wtwd.translate.activity;

import android.app.Activity;
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
public class SplashLableActivity extends Activity {

    private RecyclerView mLabelSelectRecycleView;
    LabelRecycleViewAdapter mAdapter;
    private GridView mGridView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashlable);
        initView();
    }

    private void initView(){
        final List<SelectBean> data = new ArrayList<>();
        String[] tvdata = {"打工族","数码控","财经迷","八卦党","二次元","白富美","运动达人","爱车一族","宝妈宝爸","成熟中年","80后","90后"};
        for(int i =0;i<tvdata.length;i++){
            SelectBean selectBean = new SelectBean();
            selectBean.data = tvdata[i];
            selectBean.isSelect = false;
            data.add(selectBean);
        }
        mGridView = (GridView)findViewById(R.id.mygridview);
        LabelGrideViewAdapter mAdapter = new LabelGrideViewAdapter(getApplicationContext(),data);
        mGridView.setAdapter(mAdapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //LabelGrideViewAdapter.ViewHolder holder = mGridView.getAdapter().getView(position,view,null);

                LinearLayout linearLayout=(LinearLayout) mGridView.getAdapter().getView(position,view,null);

               // RelativeLayout relativeLayout=(RelativeLayout)linearLayout.getChildAt(0);
               // TextView textView=(TextView)linearLayout.getChildAt(0);
              //  ImageView imageView = (ImageView)linearLayout.getChildAt(0);

                TextView textView =(TextView)view.findViewById(R.id.rl_item_tv);
                ImageView imageView =(ImageView)view.findViewById(R.id.rl_item_btn);

                //textView.setText("test");

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
       /* mLabelSelectRecycleView = (RecyclerView)findViewById(R.id.rl_label);
        mLabelSelectRecycleView.setLayoutManager(new GridLayoutManager(this,4));
         mAdapter = new LabelRecycleViewAdapter(SplashLableActivity.this, data);
        mLabelSelectRecycleView.setAdapter(mAdapter);
        mAdapter.setOnItemClickLitener(new LabelRecycleViewAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position, boolean select) {
                   // data.get(position).isSelect = !select;
                *//*
                view.mitemText.setTextColor(Color.parseColor("#FFFFFF"));
                holder.mitemImage.setBackground(context.getResources().getDrawable(R.drawable.rl_item_normal));
                datas.get(pos).isSelect = false;*//*
               //data.get(position).se
                LabelRecycleViewAdapter.LabelRecycleViewHolder holder = (LabelRecycleViewAdapter.LabelRecycleViewHolder)mLabelSelectRecycleView.getChildViewHolder(view);
               // boolean select = datas.get(pos).getisSelect();
                // holder.isSelect = true;
               // mOnItemClickLitener.onItemClick(holder.itemView, pos,select);

                Log.d("select","s"+ select);
                if(select){
                    holder.mitemText.setTextColor(Color.parseColor("#7a7a7a"));
                    holder.mitemImage.setBackground(getApplication().getResources().getDrawable(R.drawable.rl_item_normal));
                    data.get(position).isSelect = false;
                    Log.d("444", "ture: ");
                }else{
                    holder.mitemText.setTextColor(Color.parseColor("#FFFFFF"));
                    holder.mitemImage.setBackground(getApplication().getResources().getDrawable(R.drawable.rl_item_select));
                    data.get(position).isSelect = true;
                    Log.d("444", "false");
                }
               // mAdapter.notifyDataSetChanged();
            }
        });*/
    }
}
