package com.work.checkwork.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.sql.Time;
import java.util.Date;


public class AppealBean implements Parcelable {

    public int userId;
    public int detailId;
    public Time createtime;
    public String reason;

    public AppealBean(int userId, int detailId, String reason) {
        this.userId = userId;
        this.detailId = detailId;
        this.reason = reason;
    }

    public AppealBean() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.userId);
        dest.writeInt(this.detailId);
        dest.writeSerializable(this.createtime);
        dest.writeString(this.reason);
    }

    protected AppealBean(Parcel in) {
        this.userId = in.readInt();
        this.detailId = in.readInt();
        this.createtime = (Time) in.readSerializable();
        this.reason = in.readString();
    }

    public static final Creator<AppealBean> CREATOR = new Creator<AppealBean>() {
        @Override
        public AppealBean createFromParcel(Parcel source) {
            return new AppealBean(source);
        }

        @Override
        public AppealBean[] newArray(int size) {
            return new AppealBean[size];
        }
    };
}