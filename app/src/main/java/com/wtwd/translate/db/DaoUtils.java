package com.wtwd.translate.db;


import android.content.Context;

import com.wtwd.translate.bean.dao.RecorderBeanDao;
import com.wtwd.translate.bean.manager.RecorderBeanDaoManager;
import com.wtwd.translate.bean.manager.UserDaoManager;

public class DaoUtils {


    private static UserDaoManager landUserManager;
    private static RecorderBeanDaoManager recorderBeanDaoManager;
    /*private static NormalMsgManager normalMsgManager;
    private static DeviceFaultMsgManager deviceFaultMsgManager;
    private static SmartDeviceManager smartDeviceManager;
    private static CommonProblemManager commonProblemManager;
    private static LightManager lightManager;
    private static CurtainManager curtainManager;
    private static SmartDeviceAirQManager airQManager;*/

    public static Context context;


    public static void init(Context context) {
        DaoUtils.context = context.getApplicationContext();
    }


    public static UserDaoManager getLandUserManager() {
        if (landUserManager == null) {
            landUserManager = new UserDaoManager(context);
        }
        return landUserManager;
    }

    public static RecorderBeanDaoManager getRecorderBeanDaoManager(){
        if (recorderBeanDaoManager == null) {
            recorderBeanDaoManager = new RecorderBeanDaoManager(context);
        }
        return recorderBeanDaoManager;
    }
  /*  public static NormalMsgManager getNormalMsgManager() {
        if (normalMsgManager == null) {
            normalMsgManager = new NormalMsgManager(context);
        }
        return normalMsgManager;
    }

    public static DeviceFaultMsgManager getDeviceFaultMsgManager() {
        if (deviceFaultMsgManager == null) {
            deviceFaultMsgManager = new DeviceFaultMsgManager(context);
        }
        return deviceFaultMsgManager;
    }


    public static SmartDeviceManager getSmartDeviceManager() {
        if (smartDeviceManager == null) {
            smartDeviceManager = new SmartDeviceManager(context);
        }
        return smartDeviceManager;
    }

    public static CommonProblemManager getCommonProblemManager() {
        if (commonProblemManager == null) {
            commonProblemManager = new CommonProblemManager(context);
        }
        return commonProblemManager;
    }

    public static LightManager getLightManager() {
        if (lightManager == null) {
            lightManager = new LightManager(context);
        }
        return lightManager;
    }

    public static CurtainManager getCurtainManager() {
        if (curtainManager == null) {
            curtainManager = new CurtainManager(context);
        }
        return curtainManager;
    }

    public static SmartDeviceAirQManager getAirQManager() {
        if (airQManager == null) {
            airQManager = new SmartDeviceAirQManager(context);
        }
        return airQManager;
    }*/

}
