package com.wtwd.translate.activity;

import android.app.Activity;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wtwd.translate.MainActivity;
import com.wtwd.translate.R;
import com.wtwd.translate.utils.Constants;
import com.wtwd.translate.utils.SharedPreferencesUtils;
import com.wtwd.translate.utils.SpUtils;
import com.wtwd.translate.utils.Utils;
import com.wtwd.translate.utils.keybord.SoftKeyBoardListener;

/**
 * time:2018/1/8
 * Created by w77996
 */
public class LoginActivity extends Activity implements View.OnClickListener{

    public static final  String TAG = "LoginActivity";

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

        //软键盘的监听，弹出和隐藏
        SoftKeyBoardListener.setListener(this,new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
            @Override
            public void keyBoardShow(int height) {
                Toast.makeText(LoginActivity.this, "键盘显示 高度" + height, Toast.LENGTH_SHORT).show();
                //  textView.setText(String.valueOf(height));
                //mKeybordHight = height;
                SpUtils.putString(LoginActivity.this,"keybord_hight",height+"");
                Log.d(TAG,"键盘高度为"+height);
            }

            @Override
            public void keyBoardHide(int height) {
                Toast.makeText(LoginActivity.this, "键盘隐藏 高度" + height, Toast.LENGTH_SHORT).show();
                // textView.setText("高度："+String.valueOf(height));
            }
        });

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

                perseUsernameAndPwd();

                break;
            case R.id.img_regist:
                break;
            case R.id.login_forget_pwd:
                break;
            case R.id.login_code:
                break;
        }

    }

    private void perseUsernameAndPwd() {
        String username = mLoginUsernameEdit.getText().toString();
        String pwd = mPwdEdit.getText().toString();
        if(username == null || "".equals(username)){
            Toast.makeText(LoginActivity.this,"请输入用户名",Toast.LENGTH_SHORT).show();
            return;
        }
        if(pwd == null || "".equals(pwd)){
            Toast.makeText(LoginActivity.this,"请输入密码",Toast.LENGTH_SHORT).show();
            return;
        }
        boolean isFirstStart = SpUtils.getBoolean(getApplication(), Constants.APP_FIRST_START,true);
        if(isFirstStart){
            Intent splashIntent = new Intent(this,SplashLableActivity.class);
            startActivity(splashIntent);
        }else{
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }

    }
}
