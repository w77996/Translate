package com.wtwd.translate.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wtwd.translate.R;

/**
 * time:2018/1/11
 * Created by w77996
 */
public class SplashDevBindActivity extends Activity implements View.OnClickListener {
    /**
     * 跳过
     */
    private TextView tv_ignore;
    /**
     * 下一步
     */
    private ImageView img_next;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dev_bind);
        initView();
        addListener();
    }

    /**
     * 初始化界面
     */
    private void initView() {
        tv_ignore = (TextView)findViewById(R.id.devbind_ignore);
        img_next  = (ImageView)findViewById(R.id.img_devbind_next);
    }

    /**
     * 添加监听事件
     */
    private void addListener() {
        tv_ignore.setOnClickListener(this);
        img_next.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.devbind_ignore:
                break;
            case R.id.img_devbind_next:
                Intent splashBTOpenIntent = new Intent(SplashDevBindActivity.this,SplashBTOpenActivity.class);
                startActivity(splashBTOpenIntent);
                finish();
                break;
        }

    }
}
