package com.wtwd.translate.bean;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Property;

import java.util.Date;
import org.greenrobot.greendao.annotation.Generated;

/**
 * time:2018/1/22
 * Created by w77996
 */
@Entity
public class Guest implements Parcelable {
    @Property
    private int guestId;//用户的ID
    @Property
    private String nickName;  //用户昵称
    @Property
    private String userName;//用户的账号
    @Property
    private String password;//用户密码
    @Property
    private int flag; //0 安卓   1  苹果
    @Property
    private String userLabel;//用户标签
    @Property
    private String userMotherLanguage;//用户母语
    @Property
    private long createDate;//用户创建时间
    @Property
    private String openId;//第三方唯一标识
    @Property
    private String openAppName;//第三方app名称
    @Property
    private String headImg; //头像
    @Property
    private String hardwareInfo;//固件版本信息字符串
    @Property
    private String macAddress;//绑定的设备mac地址
    @Property
    private String hardwareUUID;//绑定的设备唯一码

    protected Guest(Parcel in) {
        guestId = in.readInt();
        nickName = in.readString();
        userName = in.readString();
        password = in.readString();
        flag = in.readInt();
        userLabel = in.readString();
        userMotherLanguage = in.readString();
        createDate = in.readLong();
        openId = in.readString();
        openAppName = in.readString();
        headImg = in.readString();
        hardwareInfo = in.readString();
        macAddress = in.readString();
        hardwareUUID = in.readString();
    }

    @Generated(hash = 2020690308)
    public Guest(int guestId, String nickName, String userName, String password,
            int flag, String userLabel, String userMotherLanguage, long createDate,
            String openId, String openAppName, String headImg, String hardwareInfo,
            String macAddress, String hardwareUUID) {
        this.guestId = guestId;
        this.nickName = nickName;
        this.userName = userName;
        this.password = password;
        this.flag = flag;
        this.userLabel = userLabel;
        this.userMotherLanguage = userMotherLanguage;
        this.createDate = createDate;
        this.openId = openId;
        this.openAppName = openAppName;
        this.headImg = headImg;
        this.hardwareInfo = hardwareInfo;
        this.macAddress = macAddress;
        this.hardwareUUID = hardwareUUID;
    }

    @Generated(hash = 769508006)
    public Guest() {
    }

    public static final Creator<Guest> CREATOR = new Creator<Guest>() {
        @Override
        public Guest createFromParcel(Parcel in) {
            return new Guest(in);
        }

        @Override
        public Guest[] newArray(int size) {
            return new Guest[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(guestId);
        dest.writeString(nickName);
        dest.writeString(userName);
        dest.writeString(password);
        dest.writeInt(flag);
        dest.writeString(userLabel);
        dest.writeString(userMotherLanguage);
        dest.writeLong(createDate);
        dest.writeString(openId);
        dest.writeString(openAppName);
        dest.writeString(headImg);
        dest.writeString(hardwareInfo);
        dest.writeString(macAddress);
        dest.writeString(hardwareUUID);
    }
}
