package com.wtwd.translate.bean;

/**
 * time:2018/1/16
 * Created by w77996
 * Github:https://github.com/w77996
 * CSDN:http://blog.csdn.net/w77996?viewmode=contents
 */

public class RecorderBean {
    /**
     * 左边和右边界面判断
     */
    private int type;
    /**
     * 录音文本
     */
    private String mRecorderTxt;
    /**
     * 翻译结果文本
     */
    private String mResultTxt;
    /**
     * 翻译结果语音保存路径
     */
    private String mFilePath;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getmRecorderTxt() {
        return mRecorderTxt;
    }

    public void setmRecorderTxt(String mRecorderTxt) {
        this.mRecorderTxt = mRecorderTxt;
    }

    public String getmResultTxt() {
        return mResultTxt;
    }

    public void setmResultTxt(String mResultTxt) {
        this.mResultTxt = mResultTxt;
    }

    public String getmFilePath() {
        return mFilePath;
    }

    public void setmFilePath(String mFilePath) {
        this.mFilePath = mFilePath;
    }
}
