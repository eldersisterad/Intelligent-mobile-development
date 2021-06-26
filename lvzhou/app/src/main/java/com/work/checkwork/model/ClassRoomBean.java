package com.work.checkwork.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;


public class ClassRoomBean implements Parcelable {

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

    public ClassRoomBean() {
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
    }

    protected ClassRoomBean(Parcel in) {
        this.id = in.readInt();
        this.className = in.readString();
        this.classCapacity = in.readString();
        this.classProjection = in.readInt();
        this.classMake = in.readInt();
        this.classAddress = in.readString();
    }

    public static final Creator<ClassRoomBean> CREATOR = new Creator<ClassRoomBean>() {
        @Override
        public ClassRoomBean createFromParcel(Parcel source) {
            return new ClassRoomBean(source);
        }

        @Override
        public ClassRoomBean[] newArray(int size) {
            return new ClassRoomBean[size];
        }
    };
}