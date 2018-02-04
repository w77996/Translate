package com.wtwd.translate;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wtwd.translate.fragment.EducationFragment;
import com.wtwd.translate.fragment.StrategyFragment;
import com.wtwd.translate.fragment.TranslateFragment;
import com.wtwd.translate.fragment.TravelFragment;
import com.wtwd.translate.fragment.UserFragment;
import com.wtwd.translate.utils.Utils;
import com.wtwd.translate.utils.permissions.PermissionsActivity;
import com.wtwd.translate.utils.permissions.PermissionsChecker;

/**
 *
 */
public class MainActivity extends FragmentActivity implements View.OnClickListener{

    private static final String TAG = "MainActivity";


    /**首页activity中的fragment**/
    FrameLayout mMainFramgLayout;
    FragmentManager mFragmentManager;
    FragmentTransaction mFragmentTransaction;

    /**翻译fragment**/
    TranslateFragment mTranslateFragment;
    /**攻略fragment**/
    StrategyFragment mStrategyFragment;
    /**出行fragment**/
    TravelFragment mTravelFragment;
    /**教育fragment**/
    EducationFragment mEducationFragment;
    /**用户fragment**/
    UserFragment mUserFragment;

    /**底部四个按钮**/
    LinearLayout mNavTranslate;
    LinearLayout mNavStrategy;
    LinearLayout mNavTravel;
    LinearLayout mNavEdu;
    LinearLayout mNavUser;

    ImageView mNavTranslateImage;
    ImageView mNavStrategyImage;
    ImageView mNavTravelImage;
    ImageView mNavEduImage;
    ImageView mNavUserImage;

    TextView mNavTranslateText;
    TextView mNavStrategyText;
    TextView mNavTravelText;
    TextView mNavEduText;
    TextView mNavUserText;

    private int currentId = R.id.nav_tran;



    private static final int PERMISSIONS_REQUEST_CODE = 0111; // 请求码

    private PermissionsChecker mPermissionsChecker; // 权限检测器

    // 所需的全部权限
    static final String[] PERMISSIONS = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPermissionsChecker = new PermissionsChecker(this);
        // 缺少权限时, 进入权限配置页面
        if (mPermissionsChecker.lacksPermissions(PERMISSIONS)) {
            startPermissionsActivity();
        }
        initView();
        initFragment();
    }

    /**
     * 初始化界面控件
     */
    private void initView() {
        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mMainFramgLayout = (FrameLayout) findViewById(R.id.main_fragment);

        mNavTranslate = (LinearLayout)findViewById(R.id.nav_tran);
        mNavStrategy = (LinearLayout)findViewById(R.id.nav_strategy);
        mNavTravel = (LinearLayout)findViewById(R.id.nav_travel);
        mNavEdu = (LinearLayout)findViewById(R.id.nav_edu);
        mNavUser = (LinearLayout)findViewById(R.id.nav_user);

        mNavTranslateImage = (ImageView) findViewById(R.id.nav_tran_img);
        mNavStrategyImage = (ImageView)findViewById(R.id.nav_strategy_img);
        mNavTravelImage = (ImageView)findViewById(R.id.nav_travel_img);
        mNavEduImage = (ImageView)findViewById(R.id.nav_edu_img);
        mNavUserImage = (ImageView)findViewById(R.id.nav_user_img);

        mNavTranslateText = (TextView)findViewById(R.id.nav_tran_text);
        mNavStrategyText = (TextView)findViewById(R.id.nav_strategy_text);
        mNavTravelText = (TextView)findViewById(R.id.nav_travel_text);
        mNavEduText = (TextView)findViewById(R.id.nav_edu_text);
        mNavUserText = (TextView)findViewById(R.id.nav_user_text);
        mNavTranslateImage.setSelected(true);


        //初始化点击事件
        mNavTranslate.setOnClickListener(this);
        mNavStrategy.setOnClickListener(this);
        mNavTravel.setOnClickListener(this);
        mNavEdu.setOnClickListener(this);
        mNavUser.setOnClickListener(this);

        Utils.setWindowStatusBarColor(this,R.color.main_title_color);//设置状态栏颜色

    }

    /**
     * 初次加载显示主fragment
     */
    private void initFragment(){
        mTranslateFragment = TranslateFragment.getInstance();
        if(!mTranslateFragment.isAdded()){
            getSupportFragmentManager().beginTransaction().add(R.id.main_fragment, mTranslateFragment,"MainFragment").commit();
            mNavTranslateImage.setBackgroundResource(R.drawable.translate_btn_select);
            mNavTranslateText.setTextColor(getResources().getColor(R.color.main_title_color));
        }

    }


    /**
     * 点击事件
     * @param v
     */
    @Override
    public void onClick(View v) {
        int id = v.getId();
        changeFragment(id);
    }


    /**
     * 点击菜单栏切换fragment
     * @param id
     */
    private void changeFragment(int id) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        switch (id){

            case R.id.nav_tran:
                hideFragment(transaction);
                changeSelect();
                if(mTranslateFragment == null){
                    mTranslateFragment = TranslateFragment.getInstance();
                    transaction.add(R.id.main_fragment, mTranslateFragment);
                }
                transaction.show(mTranslateFragment);
                transaction.commit();
                mNavTranslateImage.setBackgroundResource(R.drawable.tran_select);
                mNavTranslateText.setTextColor(getResources().getColor(R.color.main_title_color));
                break;
            case R.id.nav_strategy:
                hideFragment(transaction);
                changeSelect();
                if(mStrategyFragment == null){
                    mStrategyFragment = mStrategyFragment.getInstance();
                    transaction.add(R.id.main_fragment, mStrategyFragment);
                }

                transaction.show(mStrategyFragment);
                transaction.commit();
                mNavStrategyImage.setBackgroundResource(R.drawable.strategy_select);
                mNavStrategyText.setTextColor(getResources().getColor(R.color.main_title_color));
                break;
            case R.id.nav_travel:
                hideFragment(transaction);
                changeSelect();
                if(mTravelFragment == null){
                    mTravelFragment = TravelFragment.getInstance();
                    transaction.add(R.id.main_fragment, mTravelFragment);
                }
                transaction.show(mTravelFragment);
                transaction.commit();
                mNavTravelImage.setBackgroundResource(R.drawable.travel_select);
                mNavTravelText.setTextColor(getResources().getColor(R.color.main_title_color));
                break;
            case R.id.nav_edu:
                hideFragment(transaction);
                changeSelect();
                if(mEducationFragment == null){
                    mEducationFragment = EducationFragment.getInstance();
                    transaction.add(R.id.main_fragment, mEducationFragment);
                }
                transaction.show(mEducationFragment);
                transaction.commit();
                mNavEduImage.setBackgroundResource(R.drawable.edu_select);
                mNavEduText.setTextColor(getResources().getColor(R.color.main_title_color));
                break;
            case R.id.nav_user:
                hideFragment(transaction);
                changeSelect();
                if(mUserFragment == null){
                    mUserFragment = UserFragment.getInstance();
                    transaction.add(R.id.main_fragment, mUserFragment);
                }
                transaction.show(mUserFragment);
                transaction.commit();
                mNavUserImage.setBackgroundResource(R.drawable.user_btn_select);
                mNavUserText.setTextColor(getResources().getColor(R.color.main_title_color));
                break;
        }
    }

    /**
     * 隐藏所有fragment
     * @param transaction
     */
    private void hideFragment(FragmentTransaction transaction){
        if(mTranslateFragment !=null){
            transaction.hide(mTranslateFragment);
        }
        if(mStrategyFragment !=null){
            transaction.hide(mStrategyFragment);
        }
        if(mTravelFragment !=null){
            transaction.hide(mTravelFragment);
        }
        if(mEducationFragment !=null){
            transaction.hide(mEducationFragment);
        }
        if(mUserFragment !=null){
            transaction.hide(mUserFragment);
        }

    }

    /**
     * 改变底部按钮和文字的颜色
     */
    private void changeSelect() {
        mNavTranslateImage.setBackgroundResource(R.drawable.tran_normal);
        mNavStrategyImage.setBackgroundResource(R.drawable.strategy_normal);
        mNavTravelImage.setBackgroundResource(R.drawable.travel_normal);
        mNavEduImage.setBackgroundResource(R.drawable.edu_normal);
        mNavUserImage.setBackgroundResource(R.drawable.main_user_normal);

        mNavTranslateText.setTextColor(Color.parseColor("#616161"));
        mNavStrategyText.setTextColor(Color.parseColor("#616161"));
        mNavTravelText.setTextColor(Color.parseColor("#616161"));
        mNavEduText.setTextColor(Color.parseColor("#616161"));
        mNavUserText.setTextColor(Color.parseColor("#616161"));
    }
    private void startPermissionsActivity() {
        PermissionsActivity.startActivityForResult(this, PERMISSIONS_REQUEST_CODE, PERMISSIONS);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 拒绝时, 关闭页面, 缺少主要权限, 无法运行
        if (requestCode == PERMISSIONS_REQUEST_CODE && resultCode == PermissionsActivity.PERMISSIONS_DENIED) {
            finish();
        }
    }
}
