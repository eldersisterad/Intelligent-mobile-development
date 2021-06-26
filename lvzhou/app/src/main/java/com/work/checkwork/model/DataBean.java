package com.work.checkwork.model;

import android.os.Parcel;
import android.os.Parcelable;


public class DataBean implements Parcelable {

    public int id;
    public int type; // 0:时尚 1:雕塑 2:舞蹈 3:动漫 4:饮食 5:民俗 6:体育
    public String imgUrl; //图片地址 (由于没有服务器仅限于本机图片)
    public String content;//描述
    public int userId; //发布者id;
    public int likeNum;
    public UserBean userBean;
    public boolean isLike;

    public DataBean(int type, String imgUrl, String content, int userId, int likeNum) {
        this.type = type;
        this.imgUrl = imgUrl;
        this.content = content;
        this.userId = userId;
        this.likeNum = likeNum;
    }

    public DataBean() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.type);
        dest.writeString(this.imgUrl);
        dest.writeString(this.content);
        dest.writeInt(this.userId);
        dest.writeInt(this.likeNum);
        dest.writeParcelable(this.userBean, flags);
        dest.writeByte(this.isLike ? (byte) 1 : (byte) 0);
    }

    protected DataBean(Parcel in) {
        this.id = in.readInt();
        this.type = in.readInt();
        this.imgUrl = in.readString();
        this.content = in.readString();
        this.userId = in.readInt();
        this.likeNum = in.readInt();
        this.userBean = in.readParcelable(UserBean.class.getClassLoader());
        this.isLike = in.readByte() != 0;
    }

    public static final Creator<DataBean> CREATOR = new Creator<DataBean>() {
        @Override
        public DataBean createFromParcel(Parcel source) {
            return new DataBean(source);
        }

        @Override
        public DataBean[] newArray(int size) {
            return new DataBean[size];
        }
    };
}