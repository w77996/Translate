package com.wtwd.translate.bean.manager;

import android.content.Context;

import com.wtwd.translate.bean.User;
import com.wtwd.translate.db.BaseDao;
import com.wtwd.translate.db.DaoUtils;

/**
 * time:2018/1/25
 * Created by w77996
 */

public class UserDaoManager extends BaseDao<User> {
    public UserDaoManager(Context context) {
        super(context);
    }


//    public int deletUser (User user){
//        DaoUtils.getLandUserManager().deleteObject(user);
//    }
}
