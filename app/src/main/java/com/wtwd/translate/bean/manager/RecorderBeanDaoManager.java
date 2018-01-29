package com.wtwd.translate.bean.manager;

import android.content.Context;

import com.wtwd.translate.bean.RecorderBean;
import com.wtwd.translate.db.BaseDao;

/**
 * time:2018/1/29
 * Created by w77996
 */

public class RecorderBeanDaoManager extends BaseDao<RecorderBean> {
    public RecorderBeanDaoManager(Context context) {
        super(context);
    }
}
