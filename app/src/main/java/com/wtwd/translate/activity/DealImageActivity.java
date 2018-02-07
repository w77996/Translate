package com.wtwd.translate.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.wtwd.translate.R;
import com.wtwd.translate.bean.TranResultBean;
import com.wtwd.translate.utils.Constants;
import com.wtwd.translate.utils.GsonUtils;
import com.wtwd.translate.utils.SpUtils;
import com.wtwd.translate.utils.Utils;
import com.wtwd.translate.utils.keybord.InputTools;
import com.wtwd.translate.utils.photos.BitmapUtils;
import com.wtwd.translate.view.ClipImageView;

import java.io.File;
import java.security.Key;

public class DealImageActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "DealImageActivity";
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

    TextView ocr_text;
    TextView ocr_tran_text;
    LinearLayout linear_result_view;
    /**
     * 标题栏的互译语言和国旗
     */
    TextView text_deal_left_language;
    TextView text_deal_right_language;


    ImageView leftlanguage_head;
    ImageView rightlanguage_head;

    ImageView view_close;
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
        Utils.setWindowStatusBarColor(this, R.color.md_grey_600_color_code);

        img_deal_display = (ClipImageView) findViewById(R.id.img_deal_display);
        btn_deal_capture = (Button) findViewById(R.id.btn_deal_capture);
        btn_deal_all_tran = (Button) findViewById(R.id.btn_deal_all_tran);
        btn_deal_area_tran = (Button) findViewById(R.id.btn_deal_area_tran);
        img_deal_back = (ImageView)findViewById(R.id.img_deal_back);
        ocr_text = (TextView)findViewById(R.id.ocr_text);
        ocr_tran_text = (TextView)findViewById(R.id.ocr_tran_text);
        view_close = (ImageView)findViewById(R.id.view_close);
        linear_result_view = (LinearLayout)findViewById(R.id.linear_result_view);
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
        view_close.setOnClickListener(this);

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
                // TODO: 2018/tran_voice1/11 0011 将整张图片发送到服务器，即mDisplayBitmap
                String bitmapBase64 = BitmapUtils.bitmapToBase64(mDisplayBitmap);
                Log.e(TAG,bitmapBase64);
                if(bitmapBase64 != null ){
                    showProgressDialog();
                    requestTran(bitmapBase64,leftLanguage,rightLanguage);
                }
                break;

            case R.id.btn_deal_area_tran:
                if (showClipFrame) {
                    Bitmap clipBitmap = img_deal_display.getClippedBitmap();
                    // TODO: 2018/tran_voice1/11 0011 将截取的图片发送到服务器
                    String clipBitmapBase64 = BitmapUtils.bitmapToBase64(clipBitmap);
                    Log.e(TAG,clipBitmapBase64);
                    if(clipBitmapBase64 != null){
                        showProgressDialog();
                        requestTran(clipBitmapBase64,leftLanguage,rightLanguage);
                    }

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
                LanguageSelectIntent.putExtra(Constants.DETRECT,0);
                startActivityForResult(LanguageSelectIntent,Constants.LANGUAGE_CHANGE);
                break;
            case R.id.rightlanguage_head:
                LanguageSelectIntent = new Intent(this, LanguageSelectActivity.class);
                LanguageSelectIntent.putExtra(Constants.DETRECT,1);
                startActivityForResult(LanguageSelectIntent,Constants.LANGUAGE_CHANGE);
                break;
            case R.id.img_deal_switch:
                leftLanguage = SpUtils.getString(DealImageActivity.this,Constants.LEFT_LANGUAGE,Constants.zh_CN);
                rightLanguage = SpUtils.getString(DealImageActivity.this,Constants.RIGHT_LANGUAGE,Constants.en_US);
                SpUtils.putString(DealImageActivity.this,Constants.LEFT_LANGUAGE,rightLanguage);
                SpUtils.putString(DealImageActivity.this,Constants.RIGHT_LANGUAGE,leftLanguage);

                leftLanguage = SpUtils.getString(DealImageActivity.this, Constants.LEFT_LANGUAGE, Constants.zh_CN);
                rightLanguage = SpUtils.getString(DealImageActivity.this, Constants.RIGHT_LANGUAGE, Constants.en_US);

                Utils.perseLanguage(DealImageActivity.this, leftLanguage,leftlanguage_head,text_deal_left_language);
                Utils.perseLanguage(DealImageActivity.this,rightLanguage,rightlanguage_head,text_deal_right_language);
                break;
            case R.id.view_close:
                if(linear_result_view.getVisibility() == View.VISIBLE){
                    linear_result_view.setVisibility(View.GONE);
                    view_close.setVisibility(View.GONE);

                }
                break;
        }

    }

    /**
     * 网络请求
     * @param trandata
     * @param from
     * @param to
     */
    private void requestTran(String trandata,String from ,String to){
        OkGo.<String>post(Constants.BASEURL+Constants.OCRTRANSLATE)
                .tag(this)
                .params("image",trandata)
                .params("from",from)
                .params("to",to)
                .retryCount(0)
                .params("guestId",1)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        dismissProgressDialog();
                        Log.e(TAG,response.body().toString());
                        TranResultBean resultBean = GsonUtils.getInstance().GsonToBean(response.body().toString(),TranResultBean.class);
                        if(resultBean.getStatus() == Constants.REQUEST_SUCCESS){
                            Log.e(TAG,"请求成功");

                            String tranText = resultBean.getTranslateResult().getText();
                            String tranAudio = resultBean.getTranslateResult().getAudio();
                            String ocrText = resultBean.getTranslateResult().getOcrText();
                            if(!TextUtils.isEmpty(ocrText)&&!TextUtils.isEmpty(tranText)){
                                Log.e(TAG,ocrText + " "+tranAudio);
                                ocr_text.setText(ocrText);
                                ocr_tran_text.setText(tranText);
                                if(linear_result_view.getVisibility() == View.GONE){
                                    linear_result_view.setVisibility(View.VISIBLE);
                                    view_close.setVisibility(View.VISIBLE);
                                }
                            }
                        }else if(resultBean.getStatus() == Constants.REQUEST_FAIL){
                            Log.e(TAG,"请求失败");
                            Toast.makeText(DealImageActivity.this,resultBean.getMsg(),Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                       Log.e(TAG,"请求错误");
                        Toast.makeText(DealImageActivity.this,R.string.request_error,Toast.LENGTH_SHORT).show();
                       dismissProgressDialog();
                    }
                });
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
   /* @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if(linear_result_view.getVisibility() == View.GONE){
                return false;
            }else if(linear_result_view.getVisibility() == View.VISIBLE){
                linear_result_view.setVisibility(View.GONE);
                return true;
            }
        }
        return false;
    }*/
    /*@Override
    public boolean onTouch(View v, MotionEvent event) {
                Log.e(TAG,"down");
                if(linear_result_view.getVisibility() == View.GONE){
                    return false;
                }else if(linear_result_view.getVisibility() == View.VISIBLE){
                    linear_result_view.setVisibility(View.GONE);
                    return true;
                }
        return  false;
    }*/

   /* @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
        if(event.getAction( == KeyEvent.ACTION_DOWN){

        }
    }*/
}
