package com.wtwd.translate.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.wtwd.translate.R;
import com.wtwd.translate.utils.permissions.PermissionsActivity;
import com.wtwd.translate.utils.permissions.PermissionsChecker;
import com.wtwd.translate.bean.Recorder;
import com.wtwd.translate.view.AudioRecorderButton;

/**
 * time:2017/12/27
 * Created by w77996
 */
public class VoiceActivity extends Activity {

    private static final String TAG = "VoiceActivity";
    private AudioRecorderButton mAudioRecorderButton;

    private EditText mInputEditText;

    private static final int PERMISSIONS_REQUEST_CODE = 0; // 请求码

    private PermissionsChecker mPermissionsChecker; // 权限检测器

    // 所需的全部权限
    static final String[] PERMISSIONS = new String[]{
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice);
        initView();
        initAction();
    }

    private void initView() {
        mAudioRecorderButton = (AudioRecorderButton) findViewById(R.id.recorder_voice);
        mInputEditText = (EditText)findViewById(R.id.voice_edit);
        mInputEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Log.d(TAG,"hasFocus"+hasFocus);
            }
        });
        mInputEditText.clearFocus();
        mPermissionsChecker = new PermissionsChecker(this);
    }

    @Override protected void onResume() {
        super.onResume();

        // 缺少权限时, 进入权限配置页面
        if (mPermissionsChecker.lacksPermissions(PERMISSIONS)) {
            startPermissionsActivity();
        }
    }
    private void initAction() {
        mAudioRecorderButton.setAudioFinishRecorderListener(new AudioRecorderButton.AudioFinishRecorderListener() {
            @Override
            public void onFinish(float time, String filePath) {
                Recorder recorder = new Recorder((int)time, filePath);
                Log.d(TAG,"onFinish file"+recorder.getFilePath());
            }
        });
    }
    private void startPermissionsActivity() {
        PermissionsActivity.startActivityForResult(this, PERMISSIONS_REQUEST_CODE, PERMISSIONS);
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 拒绝时, 关闭页面, 缺少主要权限, 无法运行
        if (requestCode == PERMISSIONS_REQUEST_CODE && resultCode == PermissionsActivity.PERMISSIONS_DENIED) {
            finish();
        }
    }
}
