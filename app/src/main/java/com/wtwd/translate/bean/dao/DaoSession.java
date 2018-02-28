package com.wtwd.translate.bean.dao;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.wtwd.translate.bean.DevRecorderBean;
import com.wtwd.translate.bean.Guest;
import com.wtwd.translate.bean.RecorderBean;
import com.wtwd.translate.bean.User;

import com.wtwd.translate.bean.dao.DevRecorderBeanDao;
import com.wtwd.translate.bean.dao.GuestDao;
import com.wtwd.translate.bean.dao.RecorderBeanDao;
import com.wtwd.translate.bean.dao.UserDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig devRecorderBeanDaoConfig;
    private final DaoConfig guestDaoConfig;
    private final DaoConfig recorderBeanDaoConfig;
    private final DaoConfig userDaoConfig;

    private final DevRecorderBeanDao devRecorderBeanDao;
    private final GuestDao guestDao;
    private final RecorderBeanDao recorderBeanDao;
    private final UserDao userDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        devRecorderBeanDaoConfig = daoConfigMap.get(DevRecorderBeanDao.class).clone();
        devRecorderBeanDaoConfig.initIdentityScope(type);

        guestDaoConfig = daoConfigMap.get(GuestDao.class).clone();
        guestDaoConfig.initIdentityScope(type);

        recorderBeanDaoConfig = daoConfigMap.get(RecorderBeanDao.class).clone();
        recorderBeanDaoConfig.initIdentityScope(type);

        userDaoConfig = daoConfigMap.get(UserDao.class).clone();
        userDaoConfig.initIdentityScope(type);

        devRecorderBeanDao = new DevRecorderBeanDao(devRecorderBeanDaoConfig, this);
        guestDao = new GuestDao(guestDaoConfig, this);
        recorderBeanDao = new RecorderBeanDao(recorderBeanDaoConfig, this);
        userDao = new UserDao(userDaoConfig, this);

        registerDao(DevRecorderBean.class, devRecorderBeanDao);
        registerDao(Guest.class, guestDao);
        registerDao(RecorderBean.class, recorderBeanDao);
        registerDao(User.class, userDao);
    }
    
    public void clear() {
        devRecorderBeanDaoConfig.clearIdentityScope();
        guestDaoConfig.clearIdentityScope();
        recorderBeanDaoConfig.clearIdentityScope();
        userDaoConfig.clearIdentityScope();
    }

    public DevRecorderBeanDao getDevRecorderBeanDao() {
        return devRecorderBeanDao;
    }

    public GuestDao getGuestDao() {
        return guestDao;
    }

    public RecorderBeanDao getRecorderBeanDao() {
        return recorderBeanDao;
    }

    public UserDao getUserDao() {
        return userDao;
    }

}
