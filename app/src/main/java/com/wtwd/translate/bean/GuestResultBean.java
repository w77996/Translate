package com.wtwd.translate.bean;

import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.annotation.Entity;

/**
 * time:2018/1/25
 * Created by w77996
 */
public class GuestResultBean {
    private String msg;
    private int status;
    private int errCode;
    @SerializedName("object")
    private Guest guest;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getErrCode() {
        return errCode;
    }

    public void setErrCode(int errCode) {
        this.errCode = errCode;
    }

    public Guest getGuest() {
        return guest;
    }

    public void setGuest(Guest guest) {
        this.guest = guest;
    }

    @Override
    public String toString() {
        return "GuestResultBean{" +
                "msg='" + msg + '\'' +
                ", status=" + status +
                ", errCode=" + errCode +
                ", guest=" + guest +
                '}';
    }
}
