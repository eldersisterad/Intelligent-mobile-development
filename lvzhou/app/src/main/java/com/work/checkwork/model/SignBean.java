package com.work.checkwork.model;

import android.os.Parcel;
import android.os.Parcelable;


public class SignBean implements Parcelable {

    public int id;
    public int userId;
    public double longitude;
    public double latitude;
    public String address;
    public int isNormal;
    public String signDate;
    public String signTime;

    public SignBean(int userId, double longitude, double latitude, String address, int isNormal, String signDate, String signTime) {
        this.userId = userId;
        this.longitude = longitude;
        this.latitude = latitude;
        this.address = address;
        this.isNormal = isNormal;
        this.signDate = signDate;
        this.signTime = signTime;
    }

    public SignBean() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.userId);
        dest.writeDouble(this.longitude);
        dest.writeDouble(this.latitude);
        dest.writeString(this.address);
        dest.writeInt(this.isNormal);
        dest.writeString(this.signTime);
    }

    protected SignBean(Parcel in) {
        this.id = in.readInt();
        this.userId = in.readInt();
        this.longitude = in.readDouble();
        this.latitude = in.readDouble();
        this.address = in.readString();
        this.isNormal = in.readInt();
        this.signTime = in.readString();
    }

    public static final Creator<SignBean> CREATOR = new Creator<SignBean>() {
        @Override
        public SignBean createFromParcel(Parcel source) {
            return new SignBean(source);
        }

        @Override
        public SignBean[] newArray(int size) {
            return new SignBean[size];
        }
    };
}