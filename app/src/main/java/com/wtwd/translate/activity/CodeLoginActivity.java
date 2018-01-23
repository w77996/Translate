package com.wtwd.translate.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
import com.wtwd.translate.utils.Constants;
import com.wtwd.translate.utils.CountDownUtils;
import com.wtwd.translate.utils.Utils;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

/**
 * time:2018/1/22
 * Created by w77996
 */

public class CodeLoginActivity extends Activity implements View.OnClickListener{


    private static final String TAG = "CodeLoginActivity";

    EditText ed_username;
    EditText ed_code;
    ImageView img_regist;
    ImageView img_login;
    TextView tv_getcode;

    TextView login_pwd;
    String username;
    String code;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_codelogin);

        initView();
    }

    private void initView() {
        ed_username = (EditText)findViewById(R.id.ed_username);
        ed_code = (EditText)findViewById(R.id.ed_code);
        img_regist = (ImageView)findViewById(R.id.img_regist);
        img_login =(ImageView)findViewById(R.id.img_login);
        tv_getcode = (TextView)findViewById(R.id.tv_getcode);
        login_pwd = (TextView)findViewById(R.id.login_pwd);
        final CountDownUtils countDownUtils = new CountDownUtils(tv_getcode,60_000);
        countDownUtils.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = ed_username.getText().toString().trim();
                countDownUtils.setUserName(username);
                if(!Utils.isChinaPhoneLegal(username)){
                    return;
                }
                countDownUtils.start();
                sendCode("86",username);
            }
        });
    }

    private void addListener(){
        login_pwd.setOnClickListener(this);
        img_regist.setOnClickListener(this);
    }

    // 请求验证码，其中country表示国家代码，如“86”；phone表示手机号码，如“13800138000”
    public void sendCode(String country, String phone) {
        // 注册一个事件回调，用于处理发送验证码操作的结果
        SMSSDK.registerEventHandler(new EventHandler() {
            public void afterEvent(int event, int result, Object data) {
                if (result == SMSSDK.RESULT_COMPLETE) {
                    // TODO 处理成功得到验证码的结果
                    // 请注意，此时只是完成了发送验证码的请求，验证码短信还需要几秒钟之后才送达
                    Toast.makeText(CodeLoginActivity.this, R.string.tips_code_send_success, Toast.LENGTH_SHORT).show();
                } else{
                    // TODO 处理错误的结果
                }

            }
        });
        // 触发操作
        SMSSDK.getVerificationCode(country, phone);
    }


    // 提交验证码，其中的code表示验证码，如“1357”
    public void submitCode(String country, String phone, String code) {
        // 注册一个事件回调，用于处理提交验证码操作的结果
        SMSSDK.registerEventHandler(new EventHandler() {
            public void afterEvent(int event, int result, Object data) {
                if (result == SMSSDK.RESULT_COMPLETE) {
                    // TODO 处理验证成功的结果
                    userLogin();
                } else {
                    // TODO 处理错误的结果
                }
            }
        });
        // 触发操作
        SMSSDK.submitVerificationCode(country, phone, code);
    }

    private void userLogin() {
        OkGo.<String>post(Constants.BASEURL+Constants.VERCODEURL)
                .params("username",username)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.d(TAG,response.toString());
                    }
                });
    }


    @Override
    public void onClick(View v) {
        int id =v.getId();
        switch (id){
            case R.id.img_login:
                code = ed_code.getText().toString().trim();
                if (TextUtils.isEmpty(code)) {
                    Toast.makeText(CodeLoginActivity.this, R.string.tips_intput_code, Toast.LENGTH_SHORT).show();
                    return;
                }
                username = ed_username.getText().toString().trim();
                if(TextUtils.isEmpty(username)){
                    Toast.makeText(CodeLoginActivity.this, R.string.tips_intput_username, Toast.LENGTH_SHORT).show();
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

        }
    }

    protected void onDestroy() {
        super.onDestroy();
        //用完回调要注销掉，否则可能会出现内存泄露
        SMSSDK.unregisterAllEventHandler();
    };
}
