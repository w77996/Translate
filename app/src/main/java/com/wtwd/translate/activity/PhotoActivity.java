package com.wtwd.translate.activity;

/**
 * time:2017/12/27
 * Created by w77996
 */

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wtwd.translate.R;
import com.wtwd.translate.utils.Constants;
import com.wtwd.translate.utils.Utils;
import com.wtwd.translate.utils.photos.BitmapUtils;
import com.wtwd.translate.utils.photos.CameraUtil;

import java.io.File;
import java.io.IOException;

/**
 * time:2017/12/27
 * Created by w77996
 */
public class PhotoActivity extends Activity implements SurfaceHolder.Callback, View.OnClickListener {
    private static final String TAG = "PhotoActivity";

    /**
     * 底部中间拍照按钮
     */
    private ImageView img_photo_camera;
    /**
     * 底部右侧设置闪光灯按钮
     */
    private ImageView img_photo_flash_light;
    /**
     * 闪光灯状态
     */
    private TextView text_photo_light_state;
    /**
     * 底部左侧从相册获取图片按钮
     */
    private ImageView img_photo_picture;

    /**
     * 选择拍照取词
     */
    private LinearLayout lin_photo_take_word;
    private TextView text_photo_take_word;

    /**
     * 选择拍照翻译
     */
    private LinearLayout lin_photo_tran;
    private TextView text_photo_tran;


    /**
     * 中间预览摄像头控件
     */
    private SurfaceView surface_photo;
    private SurfaceHolder mSurfaceHolder;
    /**
     * 摄像头
     */
    private Camera mCamera;
    /**
     * 摄像头ID
     * 后置摄像头为0  前置摄像头为1
     */
    private int mCameraId = 0;

    /**
     * 闪光灯开启关闭flag
     */
    private int lightNum = 0;


    /**
     * 选择取词或翻译flag
     */
    private static final int TAKE_WORD = 0x01;
    private static final int TRAN = 0x02;

    /**
     * 从相册获取图片请求码
     */
    private static final int VALUE_PICK_PICTURE = 0x03;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        initView();
        addListener();

    }

    /**
     * 初始化界面
     */
    private void initView() {
        Utils.setWindowStatusBarColor(this, R.color.main_title_color);

        surface_photo = (SurfaceView) findViewById(R.id.surface_photo);
        img_photo_camera = (ImageView) findViewById(R.id.img_photo_camera);
        img_photo_flash_light = (ImageView) findViewById(R.id.img_photo_flash_light);
        img_photo_picture = (ImageView) findViewById(R.id.img_photo_picture);
        lin_photo_take_word = (LinearLayout) findViewById(R.id.lin_photo_take_word);
        text_photo_take_word = (TextView) findViewById(R.id.text_photo_take_word);
        lin_photo_tran = (LinearLayout) findViewById(R.id.lin_photo_tran);
        text_photo_tran = (TextView) findViewById(R.id.text_photo_tran);
        text_photo_light_state = (TextView) findViewById(R.id.text_photo_light_state);
        text_photo_light_state.setText("关闭");

        mSurfaceHolder = surface_photo.getHolder();
        mSurfaceHolder.addCallback(this);
        selectedPhoto(TAKE_WORD);
    }

    /**
     * 控件点击事件监听设置
     */
    private void addListener() {
        img_photo_camera.setOnClickListener(this);
        img_photo_picture.setOnClickListener(this);
        img_photo_flash_light.setOnClickListener(this);
        lin_photo_tran.setOnClickListener(this);
        lin_photo_take_word.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (this.mCamera == null) {
            this.mCamera = getCamera(mCameraId);
            if (mSurfaceHolder != null) {
                startPreview(this.mCamera, mSurfaceHolder);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseCamera();
    }

    /**
     * 获取相机对象
     */
    private Camera getCamera(int id) {

        Camera mCamera = null;
        try {
            mCamera = Camera.open(id);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mCamera;
    }

    /**
     * 预览相机
     */
    private void startPreview(Camera camera, SurfaceHolder holder) {
        try {
            setupCamera(camera);
            camera.setPreviewDisplay(holder);
            CameraUtil.getInstance().setCameraDisplayOrientation(this, mCameraId, camera);
            camera.startPreview();
//            isview = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置相机预览参数
     */
    private void setupCamera(Camera camera) {
        Camera.Parameters parameters = camera.getParameters();

        if (parameters.getSupportedFocusModes().contains(
                Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        }

        //这里第三个参数为最小尺寸 getPropPreviewSize方法会对从最小尺寸开始升序排列 取出所有支持尺寸的最小尺寸
        Camera.Size previewSize = CameraUtil.getInstance().getPropSizeForHeight(parameters.getSupportedPreviewSizes(), 800);
        parameters.setPreviewSize(previewSize.width, previewSize.height);

        Camera.Size pictrueSize = CameraUtil.getInstance().getPropSizeForHeight(parameters.getSupportedPictureSizes(), 800);
        parameters.setPictureSize(pictrueSize.width, pictrueSize.height);

        camera.setParameters(parameters);
    }


    /**
     * 释放相机资源
     */
    private void releaseCamera() {
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    /**
     * 拍照并保存到SDCard
     */
    private void captrue() {
        mCamera.takePicture(null, null, new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
//                isview = false;
                //将data 转换为位图 或者你也可以直接保存为文件使用 FileOutputStream
                //这里我相信大部分都有其他用处把 比如加个水印 后续再讲解
                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                Bitmap saveBitmap = CameraUtil.getInstance().setTakePicktrueOrientation(mCameraId, bitmap);
//                String img_path = getExternalFilesDir(Environment.DIRECTORY_PICTURES).getPath() +
//                        File.separator + System.currentTimeMillis() + ".jpeg";
                String img_path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getPath() + File.separator + System.currentTimeMillis() + ".jpeg";
                Log.e(TAG, "capture save img path : " + img_path);
                BitmapUtils.saveJPGE_After(PhotoActivity.this, saveBitmap, img_path, 100);

                if (!bitmap.isRecycled()) {
                    bitmap.recycle();
                }

                if (!saveBitmap.isRecycled()) {
                    saveBitmap.recycle();
                }

                gotoDealBitmap(img_path);

            }
        });
    }

    /**
     * 获取到Bitmap路径后，传到下个界面
     */
    private void gotoDealBitmap(String path) {

        Intent intent = new Intent();
        intent.putExtra(Constants.IMG_PATH_KEY, path);
        intent.setClass(PhotoActivity.this, DealImageActivity.class);
        startActivity(intent);
        finish();
    }

    private void selectPicFromLocal() {
//        Uri path = Uri.parse(getExternalFilesDir(Environment.DIRECTORY_DCIM).getPath());

        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, VALUE_PICK_PICTURE);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        startPreview(this.mCamera, holder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        this.mCamera.stopPreview();
        startPreview(this.mCamera, holder);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        releaseCamera();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == VALUE_PICK_PICTURE) {
            if (resultCode == RESULT_OK) {
                Uri uri = data.getData();
                gotoDealBitmap(getPath(uri));
            } else {
                Toast.makeText(this, "获取图片失败", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String getPath(Uri uri) {
        String[] projection = {MediaStore.Video.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.img_photo_flash_light:
                /**闪关灯切换*/
                if (mCameraId == 1) {
                    Log.e(TAG, "单前为前置摄像头");
                    return;
                }
                switchFlushLight();

                break;
            case R.id.img_photo_camera:
                /**拍照*/
                switch (lightNum) {
                    case 0:
                        //关闭
                        CameraUtil.getInstance().turnLightOff(mCamera);
                        break;
                    case 1:
                        CameraUtil.getInstance().turnLightOn(mCamera);
                        break;
                    case 2:
                        //自动
                        CameraUtil.getInstance().turnLightAuto(mCamera);
                        break;
                }
                captrue();
                break;
            case R.id.img_photo_picture:
                /**相册选择*/
                selectPicFromLocal();

                break;

            case R.id.lin_photo_take_word:
                /**选择拍照取词*/
                selectedPhoto(TAKE_WORD);
                break;

            case R.id.lin_photo_tran:
                /**选择拍照翻译*/
                selectedPhoto(TRAN);
                break;
        }

    }


    /**
     * 选择拍照取词或拍照翻译按钮UI变化Method
     */
    private void selectedPhoto(int type) {
        if (TAKE_WORD == type) {
            lin_photo_take_word.setSelected(true);
            text_photo_take_word.setTextColor(ContextCompat.getColor(PhotoActivity.this, R.color.color_white));
            lin_photo_tran.setSelected(false);
            text_photo_tran.setTextColor(ContextCompat.getColor(this, R.color.main_title_color));
        } else if (TRAN == type) {
            lin_photo_take_word.setSelected(false);
            text_photo_take_word.setTextColor(ContextCompat.getColor(PhotoActivity.this, R.color.main_title_color));
            lin_photo_tran.setSelected(true);
            text_photo_tran.setTextColor(ContextCompat.getColor(this, R.color.color_white));
        }
    }

    private void switchFlushLight() {
        lightNum++;
        if (lightNum > 2) {
            lightNum = 0;
        }
        Camera.Parameters parameters = mCamera.getParameters();
        switch (lightNum) {
            case 0:
                //关闭
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);//关闭
                mCamera.setParameters(parameters);
                text_photo_light_state.setText("关闭");
                break;

            case 1:
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);//开启
                mCamera.setParameters(parameters);
                text_photo_light_state.setText("开启");
                break;

            case 2:
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
                mCamera.setParameters(parameters);
                text_photo_light_state.setText("自动");
                break;
        }


    }
}
