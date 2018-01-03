package com.wtwd.translate.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.wtwd.translate.R;
import com.wtwd.translate.adapter.LanguageSelectListViewAdapter;
import com.wtwd.translate.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * time:2017/12/27
 * Created by w77996
 */
public class TranslateActivity extends Activity {

    private static final String TAG = "TranslateActivity";
    private EditText mSearchBoxEditText;
    private ImageView mSearchBoxImageView;
    private RecyclerView mTranSelectRecylerView;

    List<String> datas = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translate);
        initDatas();
        initView();

    }

    private void initDatas() {
        datas.add("英汉");
        datas.add("法汉");
        datas.add("日汉");
        datas.add("韩汉");
        datas.add("德汉");
        datas.add("葡汉");
        datas.add("俄汉");

    }


    private void initView(){
        Utils.setWindowStatusBarColor(this, R.color.main_title_color);
        mSearchBoxEditText = (EditText)findViewById(R.id.search_box_ed);
        mSearchBoxImageView = (ImageView)findViewById(R.id.search_box_img);
        mTranSelectRecylerView = (RecyclerView)findViewById(R.id.rl_tran_type);
        mSearchBoxEditText.setFocusable(true);
        mSearchBoxEditText.setFocusableInTouchMode(true);
        mTranSelectRecylerView.setLayoutManager(new LinearLayoutManager(TranslateActivity.this, LinearLayoutManager.HORIZONTAL, true));
        LanguageSelectListViewAdapter mAdapter = new LanguageSelectListViewAdapter(TranslateActivity.this, datas);
        mTranSelectRecylerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickLitener(new LanguageSelectListViewAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(TranslateActivity.this, position + " click",
                        Toast.LENGTH_SHORT).show();

            }
        });


    }
}
