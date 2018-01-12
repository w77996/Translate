package com.wtwd.translate.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.wtwd.translate.MainActivity;
import com.wtwd.translate.R;

import org.w3c.dom.Text;

/**
 * time:2018/1/10
 * Created by w77996
 */
public class SplashDevPairActivity extends Activity implements View.OnClickListener {

    /**
     * 跳过
     */
    private TextView tv_ignore;
    /**
     * 底部图片
     */
    private ImageView img_pair_state;
    /**
     * 配对旋转进度
     */
    private ImageView img_bind_outside_circle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devpair);
        initView();
        addListener();
    }


    /**
     * 初始化界面控件
     */
    private void initView() {
        tv_ignore = (TextView)findViewById(R.id.devpair_ignore);
        img_pair_state = (ImageView)findViewById(R.id.img_devpair_next);
        img_bind_outside_circle = (ImageView)findViewById(R.id.img_bind_outside_circle);
        Animation circle_anim = AnimationUtils.loadAnimation(SplashDevPairActivity.this, R.anim.anim_round_rotate);
        LinearInterpolator interpolator = new LinearInterpolator();  //设置匀速旋转，在xml文件中设置会出现卡顿
        circle_anim.setInterpolator(interpolator);
        if (circle_anim != null) {
            img_bind_outside_circle.startAnimation(circle_anim);  //开始动画
        }

    }

    /**
     * 添加监听事件
     */
    private void addListener() {
        tv_ignore.setOnClickListener(this);
        img_pair_state.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.devpair_ignore:
                break;
            case R.id.img_devpair_next:
                Intent mianIntent = new Intent(SplashDevPairActivity.this, MainActivity.class);
                startActivity(mianIntent);
                break;
        }
    }
}
