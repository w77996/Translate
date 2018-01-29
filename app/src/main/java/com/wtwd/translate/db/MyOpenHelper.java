package com.wtwd.translate.db;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;


import com.wtwd.translate.bean.RecorderBean;
import com.wtwd.translate.bean.TranResultBean;
import com.wtwd.translate.bean.dao.DaoMaster;
import com.wtwd.translate.bean.dao.RecorderBeanDao;
import com.wtwd.translate.bean.dao.UserDao;

import org.greenrobot.greendao.database.Database;

/**
 * 用于GreenDao 3.0数据库升级
 * <p>
 * 逻辑:先建立一个临时表，将数据迁移到临时表中，删除原有的数据库，再把数据从临时表中迁移到升级后的表中，
 * 最后删除临时表
 * <p>
 * 注意:新添加int类型数据，会在升级后报异常，因为创建数据库时，integer类型不能为null
 * 解决方法:将int类型修改为string类型
 */
public class MyOpenHelper extends DaoMaster.OpenHelper {

    public MyOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        if (oldVersion < newVersion) {
            //后面一个参数可以写多个参数,原方法(void migrate(Database db, Class<? extends AbstractDao<?, ?>>... daoClasses))
            MigrationHelper.getInstance().migrate(db, UserDao.class, RecorderBeanDao.class);

        }
    }
}
