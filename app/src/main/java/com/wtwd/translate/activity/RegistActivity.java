package com.wtwd.translate.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.wtwd.translate.R;
import com.wtwd.translate.bean.GuestResultBean;
import com.wtwd.translate.utils.Constants;
import com.wtwd.translate.utils.CountDownUtils;
import com.wtwd.translate.utils.GsonUtils;
import com.wtwd.translate.utils.Utils;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

/**
 * time:2018/1/22
 * Created by w77996
 */

public class RegistActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "RegistActivity";
    /**
     * 用户名
     */
    EditText ed_username;
    /**
     * 用户密码
     */
    EditText ed_pwd;
    /**
     * 验证码
     */
    EditText ed_code;
    /**
     * 登录按钮
     */
    ImageView img_regist_btn;
    /**
     * 获取验证码
     */
    TextView tv_getcode;
    ImageView img_getcode;

    /**
     * 用户名
     */
    String username;
    /**
     * 密码
     */
    String userpwd;
    /**
     * 验证码
     */
    String code;
    private CountDownUtils countDownUtils;
    private static final int CODE_ERROR = 0x11;
    private static final int SEND_CODE = 0x12;
    private static final int SEND_CODE_SUCCESS = 0x13;
    private static final int REGIST_UESR = 0x14;
    private static final int CODE_REDUCE =0x15;
    private int agin_get_sms_verification_code_time = 60;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case SEND_CODE:
                    Log.e(TAG,"验证码发送");
                   // sendCode("86", ed_username.getText().toString());
                    break;
                case SEND_CODE_SUCCESS:
                    Toast.makeText(RegistActivity.this, R.string.tips_code_send_success, Toast.LENGTH_SHORT).show();

                    break;
                case CODE_ERROR:
                    Toast.makeText(RegistActivity.this, R.string.tips_code_error, Toast.LENGTH_SHORT).show();
                    break;
                case REGIST_UESR:
                    showProgressDialog();
                    rigstUser();
                    break;
                case CODE_REDUCE:
                    if (agin_get_sms_verification_code_time > 1) {
                        tv_getcode.setText( (agin_get_sms_verification_code_time--)+"s");
                        mHandler.sendEmptyMessageDelayed(CODE_REDUCE, 1000);
                    } else {
                        agin_get_sms_verification_code_time = 60;
                        tv_getcode.setText(R.string.text_agin_get);
                        tv_getcode.setClickable(true);
                    }
                    break;
            }
        }
    };



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        initView();
        addListener();
    }

    private void initView() {
        ed_username = (EditText) findViewById(R.id.ed_username);
        ed_pwd = (EditText) findViewById(R.id.ed_pwd);
        ed_code = (EditText) findViewById(R.id.ed_code);
        img_regist_btn = (ImageView) findViewById(R.id.img_regist_btn);
        tv_getcode = (TextView) findViewById(R.id.tv_getcode);
        img_getcode = (ImageView) findViewById(R.id.img_getcode);
        /*countDownUtils = new CountDownUtils(tv_getcode, 60);
        countDownUtils.setUsable(false);
        countDownUtils.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "验证码按下");
                username = ed_username.getText().toString().trim();
                countDownUtils.setUserName(username);
               *//* if (!Utils.isChinaPhoneLegal(username)) {
                    //Toast.makeText(RegistActivity.this, R.string.tips_input_sure_number, Toast.LENGTH_SHORT).show();
                    mHandler.sendEmptyMessage(CODE_ERROR);
                }else{
                    mHandler.sendEmptyMessage(SEND_CODE);
                    countDownUtils.start();
                }*//*
               countDownUtils.start();
                //tv_getcode.setOnClickListener(v);
            }
        });*/

    }

    private void addListener() {
        img_regist_btn.setOnClickListener(this);
        tv_getcode.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.img_getcode:

                break;
            case R.id.img_regist_btn:
                code = ed_code.getText().toString().trim();
                if (TextUtils.isEmpty(code)) {
                    Toast.makeText(RegistActivity.this, R.string.tips_input_code, Toast.LENGTH_SHORT).show();
                    return;
                }
                userpwd = ed_pwd.getText().toString().trim();
                if (TextUtils.isEmpty(userpwd)) {
                    Toast.makeText(RegistActivity.this, R.string.tips_input_pwd, Toast.LENGTH_SHORT).show();
                    return;
                }
                username = ed_username.getText().toString().trim();
                if(TextUtils.isEmpty(username)){
                    Toast.makeText(RegistActivity.this, R.string.tips_input_username, Toast.LENGTH_SHORT).show();
                    return;
                }
               // rigstUser();
                submitCode("86", username, code);
                break;
            case R.id.tv_getcode:

                username = ed_username.getText().toString().trim();
                if(TextUtils.isEmpty(username)){
                    Toast.makeText(RegistActivity.this, R.string.tips_input_username, Toast.LENGTH_SHORT).show();
                    return;
                }
                tv_getcode.setClickable(false);
                sendCode("86",username);
                break;
        }
    }


    // 请求验证码，其中country表示国家代码，如“86”；phone表示手机号码，如“13800138000”
    public void sendCode(String country, String phone) {
        // 注册一个事件回调，用于处理发送验证码操作的结果
        SMSSDK.registerEventHandler(new EventHandler() {
            public void afterEvent(int event, int result, Object data) {
                if (result == SMSSDK.RESULT_COMPLETE) {
                    // TODO 处理成功得到验证码的结果
                    // 请注意，此时只是完成了发送验证码的请求，验证码短信还需要几秒钟之后才送达

                    mHandler.sendEmptyMessage(SEND_CODE_SUCCESS);
                    mHandler.sendEmptyMessage(CODE_REDUCE);
                } else {
                    // TODO 处理错误的结果
                    Log.e(TAG,"验证码测试,发送失败");
                }
            }
        });
        // 触发操作
        SMSSDK.getVerificationCode(country, phone);
    }

    /**
     * 提交验证码后请求服务器，将用户名和密码提交
     *
     * @param country
     * @param phone
     * @param code
     */
    // 提交验证码，其中的code表示验证码，如“1357”
    public void submitCode(String country, String phone, String code) {
        // 注册一个事件回调，用于处理提交验证码操作的结果
        SMSSDK.registerEventHandler(new EventHandler() {
            public void afterEvent(int event, int result, Object data) {
                if (result == SMSSDK.RESULT_COMPLETE) {
                    // TODO 处理验证成功的结果
                    mHandler.sendEmptyMessage(REGIST_UESR);

                } else {
                    // TODO 处理错误的结果
                   // Toast.makeText(RegistActivity.this, R.string.tips_code_error, Toast.LENGTH_SHORT).show();
                    mHandler.sendEmptyMessage(CODE_ERROR);
                }
            }
        });
        // 触发操作
        SMSSDK.submitVerificationCode(country, phone, code);
    }

    private void rigstUser(){
        /**判断用户名是否存在**/
        OkGo.<String>post(Constants.BASEURL + Constants.CHECKUSERURL)
                .params("userName", username)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        dismissProgressDialog();
                        Log.d(TAG,"请求返回结果为: "+response.body().toString());
                        //String result = GsonUtils.getInstance().GsonString(response.body().toString());
                        //Log.d(TAG, "请求返回结果为: " + result);

                        GuestResultBean guestResultBean = GsonUtils.getInstance().GsonToBean(response.body().toString(), GuestResultBean.class);
                        if(guestResultBean == null){
                            Log.e(TAG,"GSON解析错误");
                        }
                       /* Gson gson = new Gson();
                        GuestResultBean person = gson.fromJson(response.body().toString(), GuestResultBean.class);*/
                        Log.e(TAG,guestResultBean.toString());
                        if (guestResultBean.getErrCode() == Constants.USER_ALREAD_REGIST) {
                            Log.d(TAG, "用户已经注册");
                            Toast.makeText(RegistActivity.this, R.string.user_alread_regist,Toast.LENGTH_SHORT).show();
                            return;
                        }else{
                            Log.d(TAG, "用户未注册");
                        }
                        Log.e(TAG,guestResultBean.toString());
                        if (guestResultBean.getErrCode() == Constants.USER_NO_REGIST) {
                            Log.d(TAG, "该用户不存在");
                            OkGo.<String>post(Constants.BASEURL + Constants.RIGTSTURL)
                                    .params("userName", username)
                                    .params("password", userpwd)
                                    .execute(new StringCallback() {

                                        @Override
                                        public void onSuccess(Response<String> response) {

                                            GuestResultBean guestResultBean = GsonUtils.getInstance().GsonToBean(response.body().toString(), GuestResultBean.class);
                                            if(guestResultBean.getStatus() == 1){
                                                Log.e(TAG,"用户注册请求成功");
                                            }else{
                                                Log.e(TAG,"用户注册请求失败");
                                            }
                                            Log.d(TAG,"注册成功"+response.toString());
                                            Toast.makeText(RegistActivity.this, R.string.regist_success,Toast.LENGTH_SHORT).show();
                                            setResult(Constants.REGIST);
                                            finish();
                                        }
                                    });
                        }
                    }
                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        dismissProgressDialog();
                        Log.e(TAG,"错误"+response.body().toString());
                    }
                });
    }
    protected void onDestroy() {
        super.onDestroy();
        //用完回调要注销掉，否则可能会出现内存泄露
        SMSSDK.unregisterAllEventHandler();
    }

    ;
}

