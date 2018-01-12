package com.wtwd.translate.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.wtwd.translate.R;
import com.wtwd.translate.utils.Constants;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deal_image);
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
        }

    }

}
