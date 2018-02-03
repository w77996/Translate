package com.wtwd.translate.bean;

import android.widget.TextView;

/**
 * time:2018/tran_voice1/9
 * Created by w77996
 */
public class SelectBean {
    public int img;
    public String data;
    public String languageType;
    public int languageTypeText;
    public boolean isSelect;

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public boolean getisSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public String getLanguageType() {
        return languageType;
    }

    public void setLanguageType(String languageType) {
        this.languageType = languageType;
    }

    public int getLanguageTypeText() {
        return languageTypeText;
    }

    public void setLanguageTypeText(int languageTypeText) {
        this.languageTypeText = languageTypeText;
    }

    public boolean isSelect() {
        return isSelect;
    }
}
