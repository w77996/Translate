package com.wtwd.translate.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.wtwd.translate.R;
import com.wtwd.translate.activity.ChatActivity;
import com.wtwd.translate.activity.DevTranslateActivity;
import com.wtwd.translate.activity.DevTranslationActivity;
import com.wtwd.translate.activity.PhotoActivity;
import com.wtwd.translate.activity.TranslateActivity;
import com.wtwd.translate.utils.Constants;

/**
 * time:2017/12/27
 * Created by w77996
 */
public class TranslateFragment extends Fragment implements View.OnClickListener{


    private static final String TAG = "TranslateFragment";
    /**设备翻译**/
    LinearLayout mDevTranButton;
    /**相机翻译**/
    LinearLayout mPhotoTranButton;
    /**双人翻译**/
    LinearLayout mChatTranButton;
    /**相机翻译**/
    LinearLayout mVoiceTranButton;
    ImageView mSearchImageView;
    ImageView mStartImageView;
    public static  TranslateFragment mInstance;
    private Context mContext;
    public static TranslateFragment getInstance() {
        if (mInstance == null) {
            synchronized (TranslateFragment.class) {
                if (mInstance == null) {
                    mInstance = new TranslateFragment();
                }
            }
        }
        return mInstance;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view  =inflater.inflate(R.layout.fragment_translate,container,false);

        initView(view);
        initOnClick();
      
        return view;
    }

    private void initOnClick() {
        mDevTranButton.setOnClickListener(this);
        mPhotoTranButton.setOnClickListener(this);
        mChatTranButton.setOnClickListener(this);
        mVoiceTranButton.setOnClickListener(this);
        mSearchImageView.setOnClickListener(this);
        mStartImageView.setOnClickListener(this);
    }

    private void initView(View view) {

        mDevTranButton = (LinearLayout)view.findViewById(R.id.btn_dev_tran);
        mPhotoTranButton = (LinearLayout) view.findViewById(R.id.btn_photo_tran);
        mChatTranButton= (LinearLayout) view.findViewById(R.id.btn_chat_tran);
        mVoiceTranButton= (LinearLayout) view.findViewById(R.id.btn_voice_tran);
        mStartImageView = (ImageView)view.findViewById(R.id.img_start);
        mSearchImageView= (ImageView) view.findViewById(R.id.img_search);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.btn_dev_tran:
                Log.d(TAG,"btn_dev_tran");
                Intent DevTranIntent = new Intent(mContext, DevTranslationActivity.class);
                startActivity(DevTranIntent);
                break;
            case R.id.btn_photo_tran:
                Log.d(TAG,"btn_photo_tran");
                Intent photoIntent = new Intent(mContext, PhotoActivity.class);
                startActivity(photoIntent);
                break;
            case R.id.btn_chat_tran:
                Log.d(TAG,"btn_chat_tran");
                Intent ChatIntent = new Intent(mContext, ChatActivity.class);
                startActivity(ChatIntent);
                break;
            case R.id.btn_voice_tran:
                Log.d(TAG,"btn_voice_tran");
                Intent VoiceIntent = new Intent(mContext, TranslateActivity.class);
                VoiceIntent.putExtra("intent_type",Constants.INTENT_VOICE);
                startActivity(VoiceIntent);
                break;
            case R.id.img_search:
                Intent TranslateIntent = new Intent(getActivity(), TranslateActivity.class);
                TranslateIntent.putExtra("intent_type", Constants.INTENT_TRANT);
                startActivity(TranslateIntent);
                break;
            case R.id.img_start:
                break;

        }
    }
}
