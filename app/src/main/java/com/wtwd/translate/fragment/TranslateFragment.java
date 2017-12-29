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
import android.widget.Button;
import android.widget.EditText;

import com.wtwd.translate.R;
import com.wtwd.translate.activity.TranslateActivity;
import com.wtwd.translate.activity.VoiceActivity;

/**
 * time:2017/12/27
 * Created by w77996
 */
public class TranslateFragment extends Fragment implements View.OnClickListener{


    private static final String TAG = "TranslateFragment";

    Button mDevTranButton;
    Button mPhotoTranButton;
    Button mChatTranButton;
    Button mVoiceTranButton;
    EditText mSearchEditText;
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
        mSearchEditText.setOnClickListener(this);
        mSearchEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Log.d(TAG,"has fcus"+hasFocus);
                if(hasFocus){
                    Intent intent = new Intent(getActivity(), TranslateActivity.class);
                    startActivity(intent);
                }

            }
        });
    }

    private void initView(View view) {

        mDevTranButton = (Button)view.findViewById(R.id.btn_dev_tran);
        mPhotoTranButton = (Button) view.findViewById(R.id.btn_photo_tran);
        mChatTranButton= (Button) view.findViewById(R.id.btn_chat_tran);
        mVoiceTranButton= (Button) view.findViewById(R.id.btn_voice_tran);
        mSearchEditText= (EditText) view.findViewById(R.id.ed_search);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.btn_dev_tran:
                Log.d(TAG,"btn_dev_tran");
                break;
            case R.id.btn_photo_tran:
                Log.d(TAG,"btn_photo_tran");
                break;
            case R.id.btn_chat_tran:
                Log.d(TAG,"btn_chat_tran");
                break;
            case R.id.btn_voice_tran:
                Log.d(TAG,"btn_voice_tran");
                Intent intent = new Intent(mContext, VoiceActivity.class);
                startActivity(intent);
                break;

        }
    }
}
