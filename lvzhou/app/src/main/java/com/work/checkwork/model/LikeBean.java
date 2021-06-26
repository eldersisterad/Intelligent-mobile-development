package com.work.checkwork.model;

import android.os.Parcel;
import android.os.Parcelable;


public class LikeBean implements Parcelable {

    public int id;
    public int dataId;//数据id
    public int userId;//用户id

    public LikeBean(int dataId, int userId) {
        this.dataId = dataId;
        this.userId = userId;
    }

    public LikeBean() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.dataId);
        dest.writeInt(this.userId);
    }

    protected LikeBean(Parcel in) {
        this.id = in.readInt();
        this.dataId = in.readInt();
        this.userId = in.readInt();
    }

    public static final Creator<LikeBean> CREATOR = new Creator<LikeBean>() {
        @Override
        public LikeBean createFromParcel(Parcel source) {
            return new LikeBean(source);
        }

        @Override
        public LikeBean[] newArray(int size) {
            return new LikeBean[size];
        }
    };
}