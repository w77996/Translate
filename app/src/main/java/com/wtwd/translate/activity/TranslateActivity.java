package com.wtwd.translate.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.wtwd.translate.R;
import com.wtwd.translate.utils.Utils;

/**
 * time:2017/12/27
 * Created by w77996
 */
public class TranslateActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }


    private void initView(){
        Utils.setWindowStatusBarColor(this, R.color.colorPrimary);

    }
}
