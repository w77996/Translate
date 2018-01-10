package com.wtwd.translate.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.wtwd.translate.R;
import com.wtwd.translate.utils.Utils;

import org.w3c.dom.Text;

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
    /**返回**/
    ImageView mBack;

    /**头部左侧语言选择栏**/
    TextView mLeftText;
    /**头部右侧侧语言选择栏**/
    TextView mRightText;
    /**关闭语音**/
    ImageView mVoiceClose;
    /**语音键**/
    ImageView mVoice;

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
        mBack = (ImageView)findViewById(R.id.chat_back);
        mLeftText = (TextView)findViewById(R.id.tv_chat_title_left);
        mRightText = (TextView)findViewById(R.id.tv_chat_title_right);
        mVoiceClose = (ImageView)findViewById(R.id.chat_voice_close);
        mVoice = (ImageView)findViewById(R.id.img_chat_voice);

        mLeftVoiceImg.setOnClickListener(this);
        mRightVoiceImg.setOnClickListener(this);
        mDownImg.setOnClickListener(this);
        mBack.setOnClickListener(this);
        mLeftText.setOnClickListener(this);
        mRightText.setOnClickListener(this);
        mVoiceClose.setOnClickListener(this);
        mVoice.setOnClickListener(this);
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
            case R.id.chat_back:
                finish();
                break;
            case R.id.tv_chat_title_left:
                break;
            case R.id.tv_chat_title_right:
                break;
            case R.id.chat_voice_close:
                break;
            case R.id.img_chat_voice:
                break;
        }
    }
}
