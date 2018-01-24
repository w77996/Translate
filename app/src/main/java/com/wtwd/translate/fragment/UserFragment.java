package com.wtwd.translate.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.wtwd.translate.R;
import com.wtwd.translate.activity.UserActivity;

/**
 * time:2017/12/28
 * Created by w77996
 */
public class UserFragment extends Fragment implements View.OnClickListener{



    ImageView user_head;
    public static  UserFragment mInstance;
    private Context mContext;
    public static UserFragment getInstance() {
        if (mInstance == null) {
            synchronized (UserFragment.class) {
                if (mInstance == null) {
                    mInstance = new UserFragment();
                }
            }
        }
        return mInstance;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view  =inflater.inflate(R.layout.fragment_user,container,false);

        initView(view);
        addListener();

        return view;
    }

    private void addListener() {
        user_head.setOnClickListener(this);
    }

    private void initView(View view) {
        user_head = (ImageView)view.findViewById(R.id.user_head);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.user_head:
                Intent userIntent = new Intent(getActivity(), UserActivity.class);
                startActivity(userIntent);
                break;
        }
    }
}
