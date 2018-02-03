package com.wtwd.translate.bean;

import com.google.gson.annotations.SerializedName;

/**
 * time:2018/tran_voice1/26
 * Created by w77996
 */

public class TranResultBean {
    private String msg;
    private int status;
    private int errCode;
    @SerializedName("object")
    TranslateResult translateResult;

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

    public TranslateResult getTranslateResult() {
        return translateResult;
    }

    public void setTranslateResult(TranslateResult translateResult) {
        this.translateResult = translateResult;
    }
}
