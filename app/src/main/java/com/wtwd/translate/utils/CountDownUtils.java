package com.wtwd.translate.utils;

import android.os.Handler;
import android.os.Message;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.wtwd.translate.R;

import java.lang.ref.WeakReference;

/**
 * 倒计时工具
 * time:2018/1/22
 * Created by w77996
 */

public class CountDownUtils {
    /**
     * 开始倒计时code
     */
    private final int MSG_WHAT_START = 10_010;
    /**
     * 弱引用
     */
    private WeakReference<TextView> mWeakReference;
    /**
     * 倒计时时间
     */
    private int mCountDownMillis = 60;
    /**
     * 提示文字
     */
    private String mHintText = "重新发送";

    /**
     * 剩余倒计时时间
     */
    private long mLastMillis;

    /**
     * 间隔时间差(两次发送handler)
     */
    private int mIntervalMillis = 1000;

    /**
     * 可用状态下字体颜色Id
     */
    private int usableColorId = R.color.main_title_color;
    /**
     * 不可用状态下字体颜色Id
     */
    private int unusableColorId = android.R.color.darker_gray;


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case MSG_WHAT_START:
                    if (mLastMillis > 0) {
                        setUsable(false);
                        mLastMillis = mLastMillis -  1;
                        if (mWeakReference.get() != null) {
                            mHandler.sendEmptyMessageDelayed(MSG_WHAT_START, mIntervalMillis);
                        }
                    } else {
                        setUsable(true);
                    }
                    break;
            }
        }
    };
    private String username = "";

    public CountDownUtils(TextView textView) {
        mWeakReference = new WeakReference<>(textView);
    }

    public CountDownUtils(TextView textView, int countDownMillis) {
        mWeakReference = new WeakReference<>(textView);
        this.mCountDownMillis = countDownMillis;
    }

    public CountDownUtils setCountDownMillis(int countDownMillis) {
        this.mCountDownMillis = countDownMillis;
        return this;
    }

    public void setUserName(String userName){
        this.username = userName;
    }

    /**
     * 设置是否可用
     *
     * @param usable
     */
    public void setUsable(boolean usable) {
        TextView mTextView = mWeakReference.get();
        if (mTextView != null && Utils.isChinaPhoneLegal(username)) {
            if (usable) {
                //可用
                if (!mTextView.isClickable()) {
                    mTextView.setClickable(usable);
                    mTextView.setTextColor(mTextView.getResources().getColor(usableColorId));
                    mTextView.setText(mHintText);
                }
            } else {
                //不可用
                if (mTextView.isClickable()) {
                    mTextView.setClickable(usable);
                    mTextView.setTextColor(mTextView.getResources().getColor(unusableColorId));
                }
                String content = mLastMillis  + "s"  ;
                mTextView.setText(content);

            }
        }
    }

    /**
     * 设置倒计时颜色
     *
     * @param usableColorId   可用状态下的颜色
     * @param unusableColorId 不可用状态下的颜色
     */
    public CountDownUtils setCountDownColor(@ColorRes int usableColorId, @ColorRes int unusableColorId) {
        this.usableColorId = usableColorId;
        this.unusableColorId = unusableColorId;
        return this;
    }

    /**
     * 开始倒计时
     */
    public CountDownUtils start() {
        mLastMillis = mCountDownMillis;
        mHandler.sendEmptyMessage(MSG_WHAT_START);
        return this;
    }

    public CountDownUtils setOnClickListener(@Nullable final View.OnClickListener onClickListener) {
        TextView mTextView = mWeakReference.get();
        if (mTextView != null)
            mTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mHandler.removeMessages(MSG_WHAT_START);
                    start();
                    onClickListener.onClick(v);
                }
            });
        return this;
    }

    /**
     * 重置停止倒计时
     */
    public CountDownUtils reset() {
        mLastMillis = 0;
        mHandler.sendEmptyMessage(MSG_WHAT_START);
        return this;
    }
}
