package com.wtwd.translate.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.wtwd.translate.MainActivity;
import com.wtwd.translate.R;
import com.wtwd.translate.utils.Constants;
import com.wtwd.translate.utils.SpUtils;
import com.wtwd.translate.utils.Utils;

/**
 * time:2018/1/8
 * Created by w77996
 */
public class LoginActivity extends Activity implements View.OnClickListener{
    /**用户名EditText**/
    EditText mLoginUsernameEdit;
    /**密码EditText**/
    EditText mPwdEdit;

    /**登录按钮**/
    ImageView mLoginImg;
    /**注册按钮**/
    ImageView mRegistImg;

    /**忘记密码**/
    TextView mForgetPwdText;
    /**验证码登录**/
    TextView mCodeLoginText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        addListener();
    }

    /**
     * 初始化界面控件
     */
    private void initView() {
        Utils.setWindowStatusBarColor(this,R.color.main_title_color);
        mLoginUsernameEdit = (EditText)findViewById(R.id.ed_username);
        mPwdEdit = (EditText)findViewById(R.id.ed_pwd);

        mLoginImg = (ImageView)findViewById(R.id.img_login);
        mRegistImg = (ImageView)findViewById(R.id.img_regist);

        mForgetPwdText = (TextView)findViewById(R.id.login_forget_pwd);
        mCodeLoginText = (TextView)findViewById(R.id.login_code);

    }

    /**
     * 添加监听事件
     */
    private void addListener(){
        mLoginUsernameEdit.setOnClickListener(this);
        mPwdEdit.setOnClickListener(this);
        mLoginImg.setOnClickListener(this);
        mRegistImg.setOnClickListener(this);
        mForgetPwdText.setOnClickListener(this);
        mCodeLoginText.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ed_username:
                break;
            case R.id.ed_pwd:
                break;
            case R.id.img_login:
                boolean isFirstStart = SpUtils.getBoolean(getApplication(), Constants.APP_FIRST_START,true);
                if(isFirstStart){
                    Intent splashIntent = new Intent(this,SplashLableActivity.class);
                    startActivity(splashIntent);
                }else{
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.img_regist:
                break;
            case R.id.login_forget_pwd:
                break;
            case R.id.login_code:
                break;
        }

    }
}
