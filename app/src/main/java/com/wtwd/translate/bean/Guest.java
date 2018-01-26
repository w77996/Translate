package com.wtwd.translate.bean;

import java.util.Date;

/**
 * time:2018/1/22
 * Created by w77996
 */

public class Guest {
    private int guestId;//用户的ID
    private String nickName;  //用户昵称
    private String userName;//用户的账号
    private String password;//用户密码
    private int flag; //0 安卓   1  苹果
    private String userLabel;//用户标签
    private String userMotherLanguage;//用户母语
    private long createDate;//用户创建时间
    private String openId;//第三方唯一标识
    private String openAppName;//第三方app名称
    private String headImg; //头像
    private String hardwareInfo;//固件版本信息字符串
    private String macAddress;//绑定的设备mac地址
    private String hardwareUUID;//绑定的设备唯一码

    public int getGuestId() {
        return guestId;
    }

    public void setGuestId(int guestId) {
        this.guestId = guestId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getUserLabel() {
        return userLabel;
    }

    public void setUserLabel(String userLabel) {
        this.userLabel = userLabel;
    }

    public String getUserMotherLanguage() {
        return userMotherLanguage;
    }

    public void setUserMotherLanguage(String userMotherLanguage) {
        this.userMotherLanguage = userMotherLanguage;
    }

    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getOpenAppName() {
        return openAppName;
    }

    public void setOpenAppName(String openAppName) {
        this.openAppName = openAppName;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getHardwareInfo() {
        return hardwareInfo;
    }

    public void setHardwareInfo(String hardwareInfo) {
        this.hardwareInfo = hardwareInfo;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getHardwareUUID() {
        return hardwareUUID;
    }

    public void setHardwareUUID(String hardwareUUID) {
        this.hardwareUUID = hardwareUUID;
    }
}
