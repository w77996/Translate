package com.wtwd.translate.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.wtwd.translate.R;

import java.io.File;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * time:2017/12/28
 * Created by w77996
 */
public class Utils {


    public final static String TEXT_TRANSLATE_API_URL = "";
    public final static String SPEECH_TRANSLATE_API_URL = "";

    /**
     * 设置状态栏的颜色
     *
     * @param activity
     * @param colorResId
     */
    public static void setWindowStatusBarColor(Activity activity, int colorResId) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = activity.getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(activity.getResources().getColor(colorResId));

                //底部导航栏
                //window.setNavigationBarColor(activity.getResources().getColor(colorResId));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setWindowStatusBarColor(Dialog dialog, int colorResId) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = dialog.getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(dialog.getContext().getResources().getColor(colorResId));

                //底部导航栏
                //window.setNavigationBarColor(activity.getResources().getColor(colorResId));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取手机屏幕宽度
     *
     * @param context
     * @return
     */
    public static int getDisplayWidth(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        // 获取屏幕信息
        wm.getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }


    /**
     * 获取屏幕高度
     *
     * @return
     */
    public static int getScreenDPI(Context context) {
        int dpi = 0;
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        Class c;
        try {
            c = Class.forName("android.view.Display");
            Method method = c.getMethod("getRealMetrics", DisplayMetrics.class);
            method.invoke(display, displayMetrics);
            dpi = displayMetrics.heightPixels;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return dpi;
    }

    /**
     * 判断SDCard是否可用
     */
    public static boolean existSDCard() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    public static String getVoiceFilePath() {
        File voiceFile;
        if (existSDCard())
            voiceFile = new File(Environment.getExternalStorageDirectory(), "/voice");
        else voiceFile = Environment.getDataDirectory();
        voiceFile = createFile(voiceFile, "voice_", ".wav");
        // mVoiceFilePath = Environment.getExternalStorageState()+ File.separator + System.currentTimeMillis() + ".3pg";

        return voiceFile.getAbsolutePath();
    }

    /**
     * 根据系统时间、前缀、后缀产生一个文件
     */
    public static File createFile(File folder, String prefix, String suffix) {
        if (!folder.exists() || !folder.isDirectory()) folder.mkdirs();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA);
        String filename = prefix + dateFormat.format(new Date(System.currentTimeMillis())) + suffix;
        return new File(folder, filename);
    }

    /**
     * 解析语言类型
     *
     * @param context
     * @param type
     * @param imageView
     * @param textView
     */
    public static void perseLanguage(Context context, String type, ImageView imageView, TextView textView) {
        switch (type) {
            case Constants.zh_CN:
                imageView.setImageDrawable(context.getDrawable(R.drawable.flag_china));
                textView.setText("中文");
                break;
            case Constants.en_US:
                imageView.setImageDrawable(context.getDrawable(R.drawable.flag_eng));
                textView.setText("英语");
                break;
            case Constants.fr_FR:
                imageView.setImageDrawable(context.getDrawable(R.drawable.flag_fra));
                textView.setText("法语");
                break;
            case Constants.de_DE:
                imageView.setImageDrawable(context.getDrawable(R.drawable.flag_deu));
                textView.setText("德语");
                break;
            case Constants.ko_KR:
                imageView.setImageDrawable(context.getDrawable(R.drawable.flag_kor));
                textView.setText("韩语");
                break;
            case Constants.ja_JP:
                imageView.setImageDrawable(context.getDrawable(R.drawable.flag_jpa));
                textView.setText("日语");
                break;
            case Constants.es_ES:
                imageView.setImageDrawable(context.getDrawable(R.drawable.flag_spa));
                textView.setText("西班牙语");
                break;
            case Constants.pt_PT:
                imageView.setImageDrawable(context.getDrawable(R.drawable.flag_por));
                textView.setText("葡萄牙语");
                break;
            case Constants.ru_RU:
                imageView.setImageDrawable(context.getDrawable(R.drawable.language_rus));
                textView.setText("俄罗斯语");
                break;
        }

    }

    /**
     * 设置语言头像
     *
     * @param context
     * @param imageView
     * @param type
     */
    public static void setLanguageHead(Context context, ImageView imageView, String type) {

        switch (type) {
            case Constants.zh_CN:
                imageView.setImageDrawable(context.getDrawable(R.drawable.flag_china));
                break;
            case Constants.en_US:
                imageView.setImageDrawable(context.getDrawable(R.drawable.flag_eng));
                break;
            case Constants.fr_FR:
                imageView.setImageDrawable(context.getDrawable(R.drawable.flag_fra));
                break;
            case Constants.de_DE:
                imageView.setImageDrawable(context.getDrawable(R.drawable.flag_deu));
                break;
            case Constants.ko_KR:
                imageView.setImageDrawable(context.getDrawable(R.drawable.flag_kor));
                break;
            case Constants.ja_JP:
                imageView.setImageDrawable(context.getDrawable(R.drawable.flag_jpa));
                break;
            case Constants.es_ES:
                imageView.setImageDrawable(context.getDrawable(R.drawable.flag_spa));
                break;
            case Constants.pt_PT:
                imageView.setImageDrawable(context.getDrawable(R.drawable.flag_por));
                break;
            case Constants.ru_RU:
                imageView.setImageDrawable(context.getDrawable(R.drawable.language_rus));
                break;
        }
    }

    public static Locale setLocalLanguag(String type) {
        Locale locale = null;
        switch (type) {
            case Constants.zh_CN:
                locale = Locale.CHINESE;
                break;
            case Constants.en_US:
                locale = Locale.ENGLISH;
                break;
            case Constants.fr_FR:
                locale = Locale.FRANCE;
                break;
            case Constants.de_DE:
                locale = Locale.GERMANY;
                break;
            case Constants.ko_KR:
                locale = Locale.KOREA;
                break;
            case Constants.ja_JP:
                locale = Locale.JAPAN;
                break;
           /* case Constants.es_ES:
                return Locale.;
                break;
            case Constants.pt_PT:
                imageView.setImageDrawable(context.getDrawable(R.drawable.flag_por));
                break;
            case Constants.ru_RU:
                imageView.setImageDrawable(context.getDrawable(R.drawable.language_rus));
                break;*/
            default:
                break;

        }
        return locale;

    }

    /**
     * 判断是否联网
     * @param context
     * @return
     */
    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
//mNetworkInfo.isAvailable();
                return true;//有网
            }
        }
        return false;//没有网
    }
}
