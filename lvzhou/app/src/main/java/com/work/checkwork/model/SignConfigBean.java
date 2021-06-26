package com.work.checkwork.model;

import android.os.Parcel;
import android.os.Parcelable;


public class SignConfigBean implements Parcelable {

    public int userId;
    public String address;
    public double longitude;
    public double latitude;
    public double distance;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.userId);
        dest.writeString(this.address);
        dest.writeDouble(this.longitude);
        dest.writeDouble(this.latitude);
        dest.writeDouble(this.distance);
    }

    public SignConfigBean() {
    }

    protected SignConfigBean(Parcel in) {
        this.userId = in.readInt();
        this.address = in.readString();
        this.longitude = in.readDouble();
        this.latitude = in.readDouble();
        this.distance = in.readDouble();
    }

    public static final Parcelable.Creator<SignConfigBean> CREATOR = new Parcelable.Creator<SignConfigBean>() {
        @Override
        public SignConfigBean createFromParcel(Parcel source) {
            return new SignConfigBean(source);
        }

        @Override
        public SignConfigBean[] newArray(int size) {
            return new SignConfigBean[size];
        }
    };
}