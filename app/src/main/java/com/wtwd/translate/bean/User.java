package com.wtwd.translate.bean;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

import com.wtwd.translate.db.BaseDao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

/**
 * time:2018/1/25
 * Created by w77996
 */

@Entity
public class User implements Parcelable {
    @Property(nameInDb = "NAME")
    private  String name;

    @Generated(hash = 422536127)
    public User(String name) {
        this.name = name;
    }

    @Generated(hash = 586692638)
    public User() {
    }

    protected User(Parcel in) {
        name = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
    }
}
