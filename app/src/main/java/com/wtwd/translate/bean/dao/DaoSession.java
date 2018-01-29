package com.wtwd.translate.bean.dao;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.wtwd.translate.bean.User;
import com.wtwd.translate.bean.RecorderBean;

import com.wtwd.translate.bean.dao.UserDao;
import com.wtwd.translate.bean.dao.RecorderBeanDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig userDaoConfig;
    private final DaoConfig recorderBeanDaoConfig;

    private final UserDao userDao;
    private final RecorderBeanDao recorderBeanDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        userDaoConfig = daoConfigMap.get(UserDao.class).clone();
        userDaoConfig.initIdentityScope(type);

        recorderBeanDaoConfig = daoConfigMap.get(RecorderBeanDao.class).clone();
        recorderBeanDaoConfig.initIdentityScope(type);

        userDao = new UserDao(userDaoConfig, this);
        recorderBeanDao = new RecorderBeanDao(recorderBeanDaoConfig, this);

        registerDao(User.class, userDao);
        registerDao(RecorderBean.class, recorderBeanDao);
    }
    
    public void clear() {
        userDaoConfig.clearIdentityScope();
        recorderBeanDaoConfig.clearIdentityScope();
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public RecorderBeanDao getRecorderBeanDao() {
        return recorderBeanDao;
    }

}
