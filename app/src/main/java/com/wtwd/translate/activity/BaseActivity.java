package com.wtwd.translate.activity;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.wtwd.translate.view.LoadingDialogFragment;

/**
 * time:2018/tran_voice1/26
 * Created by w77996
 */

public class BaseActivity extends Activity{
    private DialogFragment dialogFragment;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public DialogFragment showProgressDialog() {
        if (dialogFragment != null) {
            dialogFragment.dismiss();
            dialogFragment = null;
        }
        dialogFragment = LoadingDialogFragment.newInstance();
        try {
            dialogFragment.show(getFragmentManager(), "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dialogFragment;
    }

    public void dismissProgressDialog() {
        if (null != dialogFragment) {
            try {
                dialogFragment.dismissAllowingStateLoss();
            } catch (Exception e) {
                e.printStackTrace();
            }
            dialogFragment = null;
        }
    }
}
