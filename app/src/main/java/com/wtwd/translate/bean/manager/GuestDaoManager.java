package com.wtwd.translate.bean.manager;

import android.content.Context;

import com.wtwd.translate.bean.Guest;
import com.wtwd.translate.db.BaseDao;

/**
 * time:2018/1/29
 * Created by w77996
 */

public class GuestDaoManager extends BaseDao<Guest> {
    public GuestDaoManager(Context context) {
        super(context);
    }
}
