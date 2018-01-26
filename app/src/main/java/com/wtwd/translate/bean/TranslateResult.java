package com.wtwd.translate.bean;

/**
 * time:2018/1/22
 * Created by w77996
 */

public class TranslateResult {
    private String text;  //翻译后的字符串
    private String audio;  //翻译后的音频下载地址
    private String ocrText;


    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    public String getOcrText() {
        return ocrText;
    }

    public void setOcrText(String ocrText) {
        this.ocrText = ocrText;
    }
}
