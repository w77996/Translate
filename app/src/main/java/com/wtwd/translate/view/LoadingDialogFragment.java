package com.wtwd.translate.view;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wtwd.translate.R;

/**
 * time:2018/tran_voice1/26
 * Created by w77996
 * Github:https://github.com/w77996
 * CSDN:http://blog.csdn.net/w77996?viewmode=contents
 */

public class LoadingDialogFragment extends android.app.DialogFragment {

    public static LoadingDialogFragment newInstance() {
        return new LoadingDialogFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setStyle(android.app.DialogFragment.STYLE_NO_TITLE, R.style.Transparent);
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialogview, container, false);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setCancelable(true);
    }

    @Override
    public void onCancel(DialogInterface dialog) {

    }

    @Override
    public void onDismiss(DialogInterface dialog) {

    }

}
