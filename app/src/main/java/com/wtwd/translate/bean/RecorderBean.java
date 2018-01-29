package com.wtwd.translate.bean;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Property;

/**
 * time:2018/1/16
 * Created by w77996
 */
@Entity
public class RecorderBean implements Parcelable{
    @Property
    private String language_type;
    /**
     * 左边和右边界面判断
     */
    @Property
    private int type;
    /**
     * 录音文本
     */
    @Property
    private String mRecorderTxt;
    /**
     * 翻译结果文本
     */
    @Property
    private String mResultTxt;
    /**
     * 翻译结果语音保存路径
     */
    @Property
    private String mFilePath;


    protected RecorderBean(Parcel in) {
        language_type = in.readString();
        type = in.readInt();
        mRecorderTxt = in.readString();
        mResultTxt = in.readString();
        mFilePath = in.readString();
    }

    @Generated(hash = 247504301)
    public RecorderBean(String language_type, int type, String mRecorderTxt,
            String mResultTxt, String mFilePath) {
        this.language_type = language_type;
        this.type = type;
        this.mRecorderTxt = mRecorderTxt;
        this.mResultTxt = mResultTxt;
        this.mFilePath = mFilePath;
    }

    @Generated(hash = 1403160276)
    public RecorderBean() {
    }

    public static final Creator<RecorderBean> CREATOR = new Creator<RecorderBean>() {
        @Override
        public RecorderBean createFromParcel(Parcel in) {
            return new RecorderBean(in);
        }

        @Override
        public RecorderBean[] newArray(int size) {
            return new RecorderBean[size];
        }
    };

    public String getLanguage_type() {
        return language_type;
    }

    public void setLanguage_type(String language_type) {
        this.language_type = language_type;
    }

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(language_type);
        dest.writeInt(type);
        dest.writeString(mRecorderTxt);
        dest.writeString(mResultTxt);
        dest.writeString(mFilePath);
    }

    public String getMRecorderTxt() {
        return this.mRecorderTxt;
    }

    public void setMRecorderTxt(String mRecorderTxt) {
        this.mRecorderTxt = mRecorderTxt;
    }

    public String getMResultTxt() {
        return this.mResultTxt;
    }

    public void setMResultTxt(String mResultTxt) {
        this.mResultTxt = mResultTxt;
    }

    public String getMFilePath() {
        return this.mFilePath;
    }

    public void setMFilePath(String mFilePath) {
        this.mFilePath = mFilePath;
    }
}
