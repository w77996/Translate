package com.wtwd.translate.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wtwd.translate.R;
import com.wtwd.translate.bean.SelectBean;
import com.wtwd.translate.view.BTOpenDialog;

/**
 * time:2018/1/12
 * Created by w77996
 */
public class SplashBTOpenActivity extends Activity implements View.OnClickListener, BTOpenDialog.OnCenterItemClickListener{
    /**
     * 对话框
     */
    private BTOpenDialog mBTOpenDialog;
    /**
     *跳过
     */
    private TextView tv_ignore;
    /**
     * 下一步
     */
    private ImageView img_next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_btopen);
        initView();
        addListener();
        mBTOpenDialog.show();
    }

    private void initView() {
        mBTOpenDialog = new BTOpenDialog(this,R.layout.bt_open_dialog,new int[]{R.id.dialog_cancel, R.id.dialog_sure});
        mBTOpenDialog.setOnCenterItemClickListener(this);
        tv_ignore = (TextView)findViewById(R.id.tv_ignore);
        img_next = (ImageView)findViewById(R.id.img_next);
    }
    private void addListener(){
        tv_ignore.setOnClickListener(this);
        img_next.setOnClickListener(this);
    }
    @Override
    public void OnCenterItemClick(BTOpenDialog dialog, View view) {
        switch (view.getId()){
            case R.id.dialog_sure:
                Toast.makeText(SplashBTOpenActivity.this,"确定按钮",Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.tv_ignore:
                break;
            case R.id.img_next:
                Intent splashDevBindIntent = new Intent(SplashBTOpenActivity.this,SplashDevPairActivity.class);
                startActivity(splashDevBindIntent);
                break;
        }

    }
}
