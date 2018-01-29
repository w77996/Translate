package com.wtwd.translate.utils;

/**
 * Created by Administrator on 2018/1/10 0010.
 */

public class Constants {


    public static final String BASEURL = "http://192.168.13.34:8080/open/";
    public static final String RIGTSTURL = "registUser.action";//注册用户
    public static final String LOGINURL = "loginUser.action";//用户登陆
    public static final String VERCODEURL = "verificationCodeLogin.action";//验证码登陆
    public static final String CHECKUSERURL = "checkIsRegiste.action";//检测用户是否已经注册
    public static final String TEXTTRANSLATE = "textTranslate.action";//语言翻译接口
    public static final String OCRTRANSLATE = "ocrTranslate.action";//语言翻译接口



    public static final String KEY_HIGHT = "KEY_HIGHT";//键盘高度

    public static final String IMG_PATH_KEY = "IMG_PATH";//照片存储
    public static final String INTENT_TRANT = "INTENT_TRANT";//intent 语言翻译
    public static final String INTENT_VOICE = "INTENT_VOICE";//intent 语音翻译
    public static final String APP_FIRST_START = "INTENT_VOICE";//APP第一次启动

    public static final float KEYBOARY_H = 0.391f;

    public static final int REGIST = 0001;//注册
    public static final int CODELOGIN = 0002;//验证码登录
    /**
     * 对话左右判断
     */
    public static final int ITEM_RIGHT =0;
    public static final int ITEM_LEFT =1;
    /**
     * 语言左右选择
     */
    public static final String LEFT_LANGUAGE = "LEFT_LANGUAGE";//左侧语言
    public static final String RIGHT_LANGUAGE = "RIGHT_LANGUAGE";//右侧语言
    /**
     * 语言选择页
     */
    public static final int LANGUAGE_CHANGE = 1111;//语言选择改变
    public static final String DETRECT = "detrect";
    public static final int  DETRECT_LEFT = 0;
    public static final int  DETRECT_RIGHT = 1;

    public static final int LANGUAGE_SELECT_NORMAL_TYPE = 1144;//语言选择界面（普通语言选择)
    public static final String LANGUAGE_SELECT_TYPE = "LANGUAGE_SELECT_TYPE";
    public static final int LANGUAGE_SELECT_DEV_TYPE = 1155;//语言选择界面（设备)
    /**
     * 蓝牙传递
     */
    public static final String BLUE_DEVICE = "BLUE_DEVICE";

    public static final int BLUE_SCAN = 1122; //蓝牙连接扫描
    public static final int  SETTING_BLUE =1133;//进入设置连接蓝牙

    /**
     * 微软语言转换
     */
    public static final String zh_CN = "zh-CN";//Chinese (Mandarin, simplified)
    public static final String zh_TW = "zh-TW";//Chinese (Mandarin, Taiwanese)
    public static final String en_AU = "en-AU";//English (Australia)
    public static final String en_CA = "en-CA";//English (Canada)
   // public static final String en_GB = "en-GB";//English (United Kingdom)
    public static final String en_IN = "en-IN";//English (India)
    //public static final String en_NZ = "en-NZ";//English (New Zealand)
   // public static final String en_US = "en-US";//English (United States)
    public static final String en_US = "en-GB";//English (United States)
    public static final String ja_JP = "ja-JP";//Japanese (Japan)
    public static final String ko_KR = "ko-KR";//Korean (Korea)
    public static final String hu_HU = "hu-HU";//hu-HU	Hungarian (Hungary)
    public static final String ms_MY = "ms-MY";//ms-MY	Malay (Malaysia)
    public static final String it_IT = "it-IT";//Italian (Italy)
    public static final String nb_NO = "nb-NO";//Norwegian (Bokmål) (Norway)
    public static final String pl_PL = "pl-PL";//Polish (Poland)
    public static final String pt_BR = "pt-BR";//Portuguese (Brazil)
    public static final String pt_PT = "pt-PT";//Portuguese (Portugal)
    public static final String ro_RO = "ro-RO";//ro-RO	Romanian (Romania)
    public static final String ru_RU = "ru-RU";//Russian (Russia)
    public static final String sk_SK = "sk-SK";//sk-SK	Slovak (Slovakia)
    public static final String es_ES = "es-ES";//Spanish (Spain)
    public static final String es_MX = "es-MX";//Spanish (Mexico)
    public static final String sv_SE = "sv-SE";//Swedish (Sweden)
    public static final String th_TH = "th-TH";//th-TH	Thai (Thailand)
    public static final String tr_TR = "tr-TR";//tr-TR	Turkish (Turkey)
    public static final String uk_UA = "uk-UA";//uk-UA	Ukrainian (Ukraine)
    public static final String vi_VN = "vi-VN";//vi-VN	Vietnamese (Vietnam)
    public static final String ar_EG = "ar-EG";//Arabic (Egypt), modern standard
    public static final String id_ID = "id-ID";//Bahasa Indonesia (Indonesia)	id-ID	Indonesian (Indonesia)
    public static final String zh_HK = "zh-HK";//Chinese (Hong Kong SAR) CANTONESE_HONGKONG,
    public static final String ca_ES = "ca-ES";//Catalan (Spain)
    public static final String hr_HR = "hr-HR";//hr-HR	Croatian (Croatia)
    public static final String cs_CZ = "cs-CZ";//cs-CZ	Czech (Czech Republic)
    public static final String da_DK = "da-DK";//Danish (Denmark)
    public static final String nl_NL = "nl-NL";//Dutch (Netherlands)
    public static final String fi_FI = "fi-FI";//Finnish (Finland)
    public static final String fr_CA = "fr-CA";//French (Canada)
    public static final String fr_FR = "fr-FR";//French (France)
    public static final String de_DE = "de-DE";//German (Germany)
    public static final String el_GR = "el-GR";//el-GR	Greek (Greece)
    public static final String he_IL = "he-IL";//he-IL	Hebrew (Israel)

    /**
     *  服务器
     */


    public static final int REQUEST_SUCCESS = 1;
    public static final int REQUEST_FAIL = 0;


    public static final int USER_ALREAD_REGIST = 2;//用户已注册
    public static final int USER_NO_REGIST = 4;//用户未注册

    public static final String GUEST_ID = "GUEST_ID";//用户ID


}
