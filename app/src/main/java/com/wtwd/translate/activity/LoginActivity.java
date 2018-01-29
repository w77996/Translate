package com.wtwd.translate.activity;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.wtwd.translate.MainActivity;
import com.wtwd.translate.R;
import com.wtwd.translate.bean.GuestResultBean;
import com.wtwd.translate.bean.ResultBean;
import com.wtwd.translate.utils.Constants;
import com.wtwd.translate.utils.GsonUtils;
import com.wtwd.translate.utils.SharedPreferencesUtils;
import com.wtwd.translate.utils.SpUtils;
import com.wtwd.translate.utils.Utils;
import com.wtwd.translate.utils.keybord.SoftKeyBoardListener;
import com.wtwd.translate.view.LoadingDialogFragment;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

/**
 * time:2018/1/8
 * Created by w77996
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener{

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
        boolean isFirst = SpUtils.getBoolean(LoginActivity.this,Constants.APP_FIRST_START,true);
        if(isFirst == true){

        }else if(isFirst == false){
            Intent intetnt = new Intent(this,MainActivity.class);
            startActivity(intetnt);
            finish();
        }
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
               // Toast.makeText(LoginActivity.this, "键盘显示 高度" + height, Toast.LENGTH_SHORT).show();
                //  textView.setText(String.valueOf(height));
                //mKeybordHight = height;
                SpUtils.putInt(LoginActivity.this,Constants.KEY_HIGHT,height);
                Log.d(TAG,"键盘高度为"+height);
            }

            @Override
            public void keyBoardHide(int height) {
                //Toast.makeText(LoginActivity.this, "键盘隐藏 高度" + height, Toast.LENGTH_SHORT).show();
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
                loginUser();
                showProgressDialog();
                break;
            case R.id.img_regist:
                Intent registIntent = new Intent(LoginActivity.this,RegistActivity.class);
                startActivityForResult(registIntent,Constants.REGIST);
                break;
            case R.id.login_forget_pwd:
                break;
            case R.id.login_code:
                Intent codeLoginIntent = new Intent(LoginActivity.this,CodeLoginActivity.class);
                startActivityForResult(codeLoginIntent,Constants.CODELOGIN);
                break;
        }

    }

    /**
     * 解析用户名和密码
     * 通信服务器
     */
    private void loginUser() {
        String username = mLoginUsernameEdit.getText().toString();
        String pwd = mPwdEdit.getText().toString();
        if(username == null || "".equals(username) || username.length() <11){
            Toast.makeText(LoginActivity.this, R.string.tips_input_sure_number,Toast.LENGTH_SHORT).show();
            return;
        }
        if(pwd == null || "".equals(pwd) ){
            Toast.makeText(LoginActivity.this, R.string.tips_input_pwd,Toast.LENGTH_SHORT).show();
            return;
        }
        if(pwd.length() < 6){
            Toast.makeText(LoginActivity.this, R.string.input_six_pwd,Toast.LENGTH_SHORT).show();
            return;
        }
        OkGo.<String>post(Constants.BASEURL+Constants.LOGINURL)
                .tag(this)
                .params("userName",username)
                .params("password",pwd)
                .retryCount(0)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        dismissProgressDialog();
                        Log.e(TAG,response.body().toString());
                        GuestResultBean resultBean = GsonUtils.getInstance().GsonToBean(response.body().toString(), GuestResultBean.class);
                        if(resultBean.getStatus() == Constants.REQUEST_SUCCESS){

                            Log.e(TAG,"登录成功");
                            boolean isFirstStart = SpUtils.getBoolean(getApplication(), Constants.APP_FIRST_START,true);
                           // if()
                            SpUtils.putInt(LoginActivity.this,Constants.GUEST_ID,resultBean.getGuest().getGuestId());
                            if(isFirstStart){
                                Intent splashIntent = new Intent(LoginActivity.this,SplashLanguageActivity.class);
                                startActivity(splashIntent);
                                Toast.makeText(LoginActivity.this, R.string.login_success,Toast.LENGTH_SHORT).show();
                                finish();
                            }else{
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }else if(resultBean.getStatus() == Constants.REQUEST_FAIL){
                            Log.e(TAG,"请求错误");
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        Log.e(TAG,"请求错误");
                        dismissProgressDialog();
                    }
                });

        //和服务器进行通信，通信返回后进入下一个界面

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Constants.REGIST){
            Log.e(TAG,"注册成功，跳转");

        }
        if(resultCode == Constants.CODELOGIN){
            boolean isFirst = SpUtils.getBoolean(this,Constants.APP_FIRST_START,true);
            if(isFirst){
                Intent splashIntent = new Intent(LoginActivity.this,SplashLanguageActivity.class);
                startActivity(splashIntent);
                finish();
            }else{
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
       // OkGo.getInstance().cancelTag(this);
    }
}
