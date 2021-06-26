package com.work.checkwork.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class MakeBean implements Parcelable {

    @SerializedName("id")
    public int id;

    @SerializedName("className")
    public String className;

    @SerializedName("classCapacity")
    public String classCapacity;

    @SerializedName("classProjection")
    public int classProjection;

    @SerializedName("classMake")
    public int classMake;

    @SerializedName("classAddress")
    public String classAddress;

    @SerializedName("markTime")
    public long markTime;

    @SerializedName("markNum")
    public int markNum;

    public MakeBean() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.className);
        dest.writeString(this.classCapacity);
        dest.writeInt(this.classProjection);
        dest.writeInt(this.classMake);
        dest.writeString(this.classAddress);
        dest.writeLong(this.markTime);
        dest.writeInt(this.markNum);
    }

    protected MakeBean(Parcel in) {
        this.id = in.readInt();
        this.className = in.readString();
        this.classCapacity = in.readString();
        this.classProjection = in.readInt();
        this.classMake = in.readInt();
        this.classAddress = in.readString();
        this.markTime = in.readLong();
        this.markNum = in.readInt();
    }

    public static final Creator<MakeBean> CREATOR = new Creator<MakeBean>() {
        @Override
        public MakeBean createFromParcel(Parcel source) {
            return new MakeBean(source);
        }

        @Override
        public MakeBean[] newArray(int size) {
            return new MakeBean[size];
        }
    };
}