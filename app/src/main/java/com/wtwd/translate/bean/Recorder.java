package com.wtwd.translate.bean;

import java.io.Serializable;

/**
 * 录音的格式
 * time:2017/12/27
 * Created by w77996
 */
public class Recorder implements Serializable {

    private int time;
    private String filePath;

    public Recorder() {
    }

    public Recorder(int time, String filePath) {
        this.time = time;
        this.filePath = filePath;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public float getTime() {
        return time;
    }

    public String getFilePath() {
        return filePath;
    }
}
