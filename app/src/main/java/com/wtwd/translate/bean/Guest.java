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
    private Date createDate = new Date();//用户创建时间
    private String openId;//第三方唯一标识
    private String openAppName;//第三方app名称
    private String headImg; //头像
    private String hardwareInfo;//固件版本信息字符串
    private String macAddress;//绑定的设备mac地址
    private String hardwareUUID;//绑定的设备唯一码
}
