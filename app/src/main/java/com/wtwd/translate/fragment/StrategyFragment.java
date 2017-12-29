package com.wtwd.translate.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wtwd.translate.R;

/**
 * time:2017/12/28
 * Created by w77996
 * Github:https://github.com/w77996
 * CSDN:http://blog.csdn.net/w77996?viewmode=contents
 */
public class StrategyFragment extends Fragment {

    public static  StrategyFragment mInstance;
    private Context mContext;
    public static StrategyFragment getInstance() {
        if (mInstance == null) {
            synchronized (StrategyFragment.class) {
                if (mInstance == null) {
                    mInstance = new StrategyFragment();
                }
            }
        }
        return mInstance;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view  =inflater.inflate(R.layout.fragment_strategy,container,false);

       /* initView(view);
        initOnClick();*/

        return view;
    }
}
