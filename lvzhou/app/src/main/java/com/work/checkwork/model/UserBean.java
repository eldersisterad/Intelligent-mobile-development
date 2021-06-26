package com.work.checkwork.model;

import android.os.Parcel;
import android.os.Parcelable;



public class UserBean implements Parcelable {

    public int id;
    public String userName;//用户昵称
    public String userPhone;//用户账号
    public String userPwd;//用户密码
    public String userPic;//用户头像

    public UserBean(String userName, String userPhone, String userPwd, String userPic) {
        this.userName = userName;
        this.userPhone = userPhone;
        this.userPwd = userPwd;
        this.userPic = userPic;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.userName);
        dest.writeString(this.userPhone);
        dest.writeString(this.userPwd);
        dest.writeString(this.userPic);
    }

    public UserBean() {
    }

    protected UserBean(Parcel in) {
        this.id = in.readInt();
        this.userName = in.readString();
        this.userPhone = in.readString();
        this.userPwd = in.readString();
        this.userPic = in.readString();
    }

    public static final Creator<UserBean> CREATOR = new Creator<UserBean>() {
        @Override
        public UserBean createFromParcel(Parcel source) {
            return new UserBean(source);
        }

        @Override
        public UserBean[] newArray(int size) {
            return new UserBean[size];
        }
    };
}