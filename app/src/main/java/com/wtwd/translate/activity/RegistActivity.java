package com.wtwd.translate.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.Callback;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.model.Result;
import com.lzy.okgo.request.base.Request;
import com.wtwd.translate.R;
import com.wtwd.translate.bean.ResultBean;
import com.wtwd.translate.utils.Constants;
import com.wtwd.translate.utils.CountDownUtils;
import com.wtwd.translate.utils.GsonUtils;
import com.wtwd.translate.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

/**
 * time:2018/1/22
 * Created by w77996
 */

public class RegistActivity extends Activity implements View.OnClickListener {

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
        countDownUtils = new CountDownUtils(tv_getcode, 60_000);
        countDownUtils.setUsable(false);
        countDownUtils.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "验证码按下");
                username = ed_username.getText().toString().trim();
                countDownUtils.setUserName(username);
                if (!Utils.isChinaPhoneLegal(username)) {
                    Toast.makeText(RegistActivity.this, R.string.tips_input_sure_number, Toast.LENGTH_SHORT).show();
                    return;
                }
                sendCode("86", ed_username.getText().toString());

                countDownUtils.start();
                //tv_getcode.setOnClickListener(v);
            }
        });
    }

    private void addListener() {
        img_regist_btn.setOnClickListener(this);

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
                    Toast.makeText(RegistActivity.this, "请输入验证码", Toast.LENGTH_SHORT).show();
                    return;
                }
                userpwd = ed_pwd.getText().toString().trim();
                if (TextUtils.isEmpty(userpwd)) {
                    Toast.makeText(RegistActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(username)){
                    Toast.makeText(RegistActivity.this, "请输入手机号", Toast.LENGTH_SHORT).show();
                    return;
                }
                submitCode("86", username, code);
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
                    Toast.makeText(RegistActivity.this, R.string.tips_code_send_success, Toast.LENGTH_SHORT).show();

                } else {
                    // TODO 处理错误的结果
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
                    /**判断用户名是否存在**/
                    OkGo.<String>post(Constants.BASEURL + Constants.CHECKUSERURL)
                            .params("username", username)
                            .execute(new StringCallback() {
                                @Override
                                public void onSuccess(Response<String> response) {

                                    String result = GsonUtils.getInstance().GsonString(response);
                                    Log.d(TAG, "请求返回结果为: " + result);


                                    ResultBean resultBean = GsonUtils.getInstance().GsonToBean(response.toString(), ResultBean.class);

                                    if (resultBean.getErrCode() == 2) {
                                        Log.d(TAG, "用户已经注册");
                                        return;
                                    }
                                    if (resultBean.getErrCode() == 4) {
                                        Log.d(TAG, "该用户不存在");
                                        OkGo.<String>post(Constants.BASEURL + Constants.RIGTSTURL)
                                                .params("username", username)
                                                .params("password", userpwd)
                                                .execute(new StringCallback() {

                                                    @Override
                                                    public void onSuccess(Response<String> response) {

                                                        ResultBean resultBean = GsonUtils.getInstance().GsonToBean(response.toString(), ResultBean.class);

                                                         Log.d(TAG,"注册成功"+response.toString());

                                                        setResult(Constants.REGIST);

                                                    }
                                                });
                                    }
                                }
                                @Override
                                public void onError(Response<String> response) {
                                    super.onError(response);
                                }
                            });

                } else {
                    // TODO 处理错误的结果
                    Toast.makeText(RegistActivity.this, "验证码错误", Toast.LENGTH_SHORT).show();
                }
            }
        });
        // 触发操作
        SMSSDK.submitVerificationCode(country, phone, code);
    }

    protected void onDestroy() {
        super.onDestroy();
        //用完回调要注销掉，否则可能会出现内存泄露
        SMSSDK.unregisterAllEventHandler();
    }

    ;
}

