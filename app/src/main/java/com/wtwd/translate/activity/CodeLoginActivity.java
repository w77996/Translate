package com.wtwd.translate.activity;

import android.app.Activity;
import android.content.Intent;
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
 * time:2018/tran_voice1/22
 * Created by w77996
 */

public class CodeLoginActivity extends BaseActivity implements View.OnClickListener{


    private static final String TAG = "CodeLoginActivity";

    /**
     * 用户名
     */
    EditText ed_username;
    /**
     * 验证码
     */
    EditText ed_code;
    /**
     * 注册
     */
    ImageView img_regist;
    /**
     * 登录
     */
    ImageView img_login;
    /**
     * 获取验证码
     */
    TextView tv_getcode;
    /**
     * 密码登录
     */
    TextView login_pwd;

    String username;
    String code;

    private static final int CODE_ERROR = 0x11;
    private static final int SEND_CODE = 0x12;
    private static final int SEND_CODE_SUCCESS = 0x13;
    private static final int LOGIN_UESR = 0x14;
    private static final int CODE_REDUCE =0x15;
    private static final int SEND_CODE_ERROR =0x16 ;
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
                    Toast.makeText(CodeLoginActivity.this, R.string.tips_code_send_success, Toast.LENGTH_SHORT).show();

                    break;
                case CODE_ERROR:
                    Toast.makeText(CodeLoginActivity.this, R.string.tips_code_error, Toast.LENGTH_SHORT).show();
                    break;
                case LOGIN_UESR:
                    Log.e(TAG,"验证成功，登录");
                    showProgressDialog();
                    userLogin();
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
                case SEND_CODE_ERROR:
                    agin_get_sms_verification_code_time = 60;
                    tv_getcode.setText(R.string.text_agin_get);
                    tv_getcode.setClickable(true);
                    Toast.makeText(CodeLoginActivity.this, "send code error", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    private void codeLoginUser() {
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_codelogin);

        initView();
        addListener();
    }

    /**
     * 初始化界面
     */
    private void initView() {
        ed_username = (EditText)findViewById(R.id.ed_username);
        ed_code = (EditText)findViewById(R.id.ed_code);
        img_regist = (ImageView)findViewById(R.id.img_regist);
        img_login =(ImageView)findViewById(R.id.img_login);
        tv_getcode = (TextView)findViewById(R.id.tv_getcode);
        login_pwd = (TextView)findViewById(R.id.login_pwd);
        /*final CountDownUtils countDownUtils = new CountDownUtils(tv_getcode,60_000);
        countDownUtils.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = ed_username.getText().toString().trim();
                countDownUtils.setUserName(username);
                //验证手机号是否正确
                if(!Utils.isChinaPhoneLegal(username)){
                    Toast.makeText(CodeLoginActivity.this,"请输入正确的手机号码",Toast.LENGTH_SHORT).show();
                    return;
                }
                countDownUtils.start();
                sendCode("86",username);
            }
        });*/
    }

    /**
     * 添加监听器
     */
    private void addListener(){
        login_pwd.setOnClickListener(this);
        img_regist.setOnClickListener(this);
        img_login.setOnClickListener(this);
        tv_getcode.setOnClickListener(this);
    }

    /**
     * 添加验证码
     * @param country
     * @param phone
     */
    // 请求验证码，其中country表示国家代码，如“86”；phone表示手机号码，如“13800138000”
    public void sendCode(String country, String phone) {
        // 注册一个事件回调，用于处理发送验证码操作的结果
        SMSSDK.registerEventHandler(new EventHandler() {
            public void afterEvent(int event, int result, Object data) {
                if (result == SMSSDK.RESULT_COMPLETE) {
                    // TODO 处理成功得到验证码的结果
                    // 请注意，此时只是完成了发送验证码的请求，验证码短信还需要几秒钟之后才送达
                   // Toast.makeText(CodeLoginActivity.this, R.string.tips_code_send_success, Toast.LENGTH_SHORT).show();
                    mHandler.sendEmptyMessage(SEND_CODE_SUCCESS);

                } else{
                    // TODO 处理错误的结果
                    //mHandler.sendEmptyMessage(Co);
                    Log.e(TAG,"验证码发送失败");
                    mHandler.sendEmptyMessage(SEND_CODE_ERROR);
                }

            }
        });
        // 触发操作
        SMSSDK.getVerificationCode(country, phone);
    }

    /**
     * 验证验证码
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
                    //userLogin();
                    mHandler.sendEmptyMessage(LOGIN_UESR);
                } else {
                    // TODO 处理错误的结果
                    mHandler.sendEmptyMessage(CODE_ERROR);
                }
            }
        });
        // 触发操作
        SMSSDK.submitVerificationCode(country, phone, code);
    }

    /**
     * 用户登录
     */
    private void userLogin() {
        OkGo.<String>post(Constants.BASEURL+Constants.VERCODEURL)
                .params("userName",username)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        dismissProgressDialog();
                        Log.d(TAG,response.body().toString());
                        GuestResultBean resultBean = GsonUtils.getInstance().GsonToBean(response.body().toString(),GuestResultBean.class);
                        if(resultBean.getStatus() == Constants.REQUEST_SUCCESS){
                            Log.e(TAG,"请求登录成功");
                            setResult(Constants.CODELOGIN);
                            finish();
                        }else{
                            Log.e(TAG,"请求登录失败");
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        dismissProgressDialog();
                        Toast.makeText(CodeLoginActivity.this, R.string.request_error, Toast.LENGTH_SHORT).show();
                    }
                });
    }


    @Override
    public void onClick(View v) {
        int id =v.getId();
        switch (id){
            case R.id.img_login:
                username = ed_username.getText().toString().trim();
                if(TextUtils.isEmpty(username)){
                    Toast.makeText(CodeLoginActivity.this, R.string.tips_intput_username, Toast.LENGTH_SHORT).show();
                    return;
                }
                code = ed_code.getText().toString().trim();
                if (TextUtils.isEmpty(code)) {
                    Toast.makeText(CodeLoginActivity.this, R.string.tips_input_code, Toast.LENGTH_SHORT).show();
                    return;
                }

                submitCode("86",username,code);
                break;
            case R.id.img_regist:
                Intent registIntent = new Intent(CodeLoginActivity.this,RegistActivity.class);

                startActivityForResult(registIntent,Constants.REGIST);
                finish();
                break;
            case R.id.login_pwd:
                finish();
                break;
            case R.id.tv_getcode:
                username = ed_username.getText().toString().trim();
                if(TextUtils.isEmpty(username)||username.length() < 11){
                    Toast.makeText(CodeLoginActivity.this, R.string.tips_input_username, Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!Utils.isNetworkConnected(this)){
                    Toast.makeText(CodeLoginActivity.this, R.string.no_network, Toast.LENGTH_SHORT).show();
                    return;
                }
                mHandler.sendEmptyMessage(CODE_REDUCE);
                tv_getcode.setClickable(false);
                sendCode("86",username);
                break;

        }
    }

    protected void onDestroy() {
        super.onDestroy();
        //用完回调要注销掉，否则可能会出现内存泄露
        SMSSDK.unregisterAllEventHandler();

    };
}
