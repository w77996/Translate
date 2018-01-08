package com.wtwd.translate.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.wtwd.translate.R;
import com.wtwd.translate.utils.Utils;

/**
 * time:2017/12/27
 * Created by w77996
 */
public class ChatActivity extends Activity implements View.OnClickListener{

    /**底部左录音按钮**/
    ImageButton mLeftVoiceImg;
    /**底部右录音按钮**/
    ImageButton mRightVoiceImg;
    /**头部语言选择按钮**/
    ImageView mDownImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        initView();
    }

    /**
     * 初始化界面控件
     */
    private void initView() {
        Utils.setWindowStatusBarColor(this,R.color.main_title_color);
        mLeftVoiceImg = (ImageButton)findViewById(R.id.imgbtn_left);
        mRightVoiceImg = (ImageButton)findViewById(R.id.imgbtn_right);
        mDownImg = (ImageView)findViewById(R.id.img_chat_down_row);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imgbtn_left:
                break;
            case R.id.imgbtn_right:
                break;
            case R.id.img_chat_down_row:
                break;
        }
    }
}
