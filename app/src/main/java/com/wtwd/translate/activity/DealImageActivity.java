package com.wtwd.translate.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.wtwd.translate.R;
import com.wtwd.translate.utils.Constants;
import com.wtwd.translate.utils.SpUtils;
import com.wtwd.translate.utils.Utils;
import com.wtwd.translate.utils.photos.BitmapUtils;
import com.wtwd.translate.view.ClipImageView;

import java.io.File;

public class DealImageActivity extends Activity implements View.OnClickListener {

    /**
     * 显示获取的图片
     */
    private ClipImageView img_deal_display;
    /**
     * 重新拍照按钮
     */
    private Button btn_deal_capture;
    /**
     * 全部翻译按钮
     */
    private Button btn_deal_all_tran;
    /**
     * 区域翻译按钮
     */
    private Button btn_deal_area_tran;
    /**
     * 返回按钮
     */
    private ImageView img_deal_back;
    /**
     * 用来显示的bitmap
     */
    private Bitmap mDisplayBitmap;

    private boolean showClipFrame;

    /**
     * 标题栏的互译语言和国旗
     */
    TextView text_deal_left_language;
    TextView text_deal_right_language;


    ImageView leftlanguage_head;
    ImageView rightlanguage_head;
    /**
     * 切换
     */
    ImageView img_deal_switch;

    String leftLanguage;
    String rightLanguage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deal_image);
        leftLanguage = SpUtils.getString(this,Constants.LEFT_LANGUAGE,Constants.zh_CN);
        rightLanguage = SpUtils.getString(this,Constants.RIGHT_LANGUAGE,Constants.en_US);
        initView();

    }

    /***
     * 初始化界面视图
     */
    private void initView() {
        Utils.setWindowStatusBarColor(this, R.color.main_title_color);

        img_deal_display = (ClipImageView) findViewById(R.id.img_deal_display);
        btn_deal_capture = (Button) findViewById(R.id.btn_deal_capture);
        btn_deal_all_tran = (Button) findViewById(R.id.btn_deal_all_tran);
        btn_deal_area_tran = (Button) findViewById(R.id.btn_deal_area_tran);
        img_deal_back = (ImageView)findViewById(R.id.img_deal_back);

        text_deal_left_language = (TextView)findViewById(R.id.text_deal_left_language);
        text_deal_right_language = (TextView)findViewById(R.id.text_deal_right_language);
        leftlanguage_head = (ImageView)findViewById(R.id.leftlanguage_head);
        rightlanguage_head = (ImageView)findViewById(R.id.rightlanguage_head);
        img_deal_switch = (ImageView)findViewById(R.id.img_deal_switch);

        Utils.perseLanguage(this,leftLanguage,leftlanguage_head,text_deal_left_language);
        Utils.perseLanguage(this,rightLanguage,rightlanguage_head,text_deal_right_language);

        String imgPath = bitmapPath();
        addListener();

        try {
            if (TextUtils.isEmpty(imgPath)) {
                return;
            }
            File file = new File(imgPath);
            mDisplayBitmap = BitmapUtils.getBitemapFromFile(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        img_deal_display.setImageBitmap(mDisplayBitmap);
    }

    private void addListener() {
        btn_deal_area_tran.setOnClickListener(this);
        btn_deal_all_tran.setOnClickListener(this);
        btn_deal_capture.setOnClickListener(this);
        img_deal_back.setOnClickListener(this);
        leftlanguage_head.setOnClickListener(this);
        rightlanguage_head.setOnClickListener(this);
        img_deal_switch.setOnClickListener(this);

    }

    private String bitmapPath() {
        if (getIntent() == null) {
            return null;
        }
        return getIntent().getStringExtra(Constants.IMG_PATH_KEY);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDisplayBitmap != null) {
            if (!mDisplayBitmap.isRecycled()) {
                mDisplayBitmap.recycle();
            }
        }

    }

    @Override
    public void onClick(View view) {
        Intent LanguageSelectIntent;
        switch (view.getId()) {
            case R.id.btn_deal_capture:
                Intent intent = new Intent(DealImageActivity.this, PhotoActivity.class);
                startActivity(intent);
                finish();

                break;

            case R.id.btn_deal_all_tran:
                // TODO: 2018/1/11 0011 将整张图片发送到服务器，即mDisplayBitmap

                break;

            case R.id.btn_deal_area_tran:
                if (showClipFrame) {
                    Bitmap clipBitmap = img_deal_display.getClippedBitmap();
                    // TODO: 2018/1/11 0011 将截取的图片发送到服务器


                    //发送完成后回收bitmap
                    if (!clipBitmap.isRecycled()) {
                        clipBitmap.recycle();
                    }
                } else {
                    showClipFrame = true;
                    img_deal_display.setShowClipFrame(true);
                }
                break;
            case R.id.img_deal_back:
                finish();
                break;
            case R.id.leftlanguage_head:
                LanguageSelectIntent = new Intent(this, LanguageSelectActivity.class);
                LanguageSelectIntent.putExtra("derect",0);
                startActivityForResult(LanguageSelectIntent,Constants.LANGUAGE_CHANGE);
                break;
            case R.id.rightlanguage_head:
                LanguageSelectIntent = new Intent(this, LanguageSelectActivity.class);
                LanguageSelectIntent.putExtra("derect",1);
                startActivityForResult(LanguageSelectIntent,Constants.LANGUAGE_CHANGE);
                break;
            case R.id.img_deal_switch:
                leftLanguage = SpUtils.getString(DealImageActivity.this,Constants.LEFT_LANGUAGE,Constants.zh_CN);
                rightLanguage = SpUtils.getString(DealImageActivity.this,Constants.RIGHT_LANGUAGE,Constants.en_US);
                SpUtils.putString(DealImageActivity.this,Constants.LEFT_LANGUAGE,rightLanguage);
                SpUtils.putString(DealImageActivity.this,Constants.RIGHT_LANGUAGE,leftLanguage);
                Utils.perseLanguage(DealImageActivity.this, leftLanguage,leftlanguage_head,text_deal_left_language);
                Utils.perseLanguage(DealImageActivity.this,rightLanguage,rightlanguage_head,text_deal_right_language);
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Constants.LANGUAGE_CHANGE){
            leftLanguage = SpUtils.getString(this,Constants.LEFT_LANGUAGE,Constants.zh_CN);
            rightLanguage = SpUtils.getString(this,Constants.RIGHT_LANGUAGE,Constants.en_US);
            Utils.perseLanguage(this,leftLanguage,leftlanguage_head,text_deal_left_language);
            Utils.perseLanguage(this,rightLanguage,rightlanguage_head,text_deal_right_language);
        }
    }
}
