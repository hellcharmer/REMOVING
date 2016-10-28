package com.example.charmer.moving.pojo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Jingsheng Liang on 2016/9/13.
 */
public class Friend implements Parcelable {
    public String friendimg ;
    public Integer friendid;
    public String photoimg;
    public String name;
    public String title;
    public String content;
   public boolean visbale;
    public User user;
    // alt+insert

    public Friend(String friendimg, String name, String title, String content, boolean visbale, Integer friendid) {
        this.friendimg = friendimg;
        this.name = name;
        this.title = title;
        this.content = content;
        this.visbale = visbale;
        this.friendid = friendid;
    }

    public Friend(){}

    public Friend(String title) {
        this.title = title;
    }

    public Friend(Integer friendid, String photoimg, String name, String title, String content, boolean visbale, User user) {
        this.friendid = friendid;
        this.photoimg = photoimg;
        this.name = name;
        this.title = title;
        this.content = content;
        this.visbale = visbale;
        this.user = user;
    }

    public String getFriendimg() {
        return friendimg;
    }

    public void setFriendimg(String friendimg) {
        this.friendimg = friendimg;
    }

    public Integer getFriendid() {
        return friendid;
    }

    public void setFriendid(Integer friendid) {
        this.friendid = friendid;
    }

    public String getPhotoimg() {
        return photoimg;
    }

    public void setPhotoimg(String photoimg) {
        this.photoimg = photoimg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isVisbale() {
        return visbale;
    }

    public void setVisbale(boolean visbale) {
        this.visbale = visbale;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.friendimg);
        dest.writeValue(this.friendid);
        dest.writeString(this.photoimg);
        dest.writeString(this.name);
        dest.writeString(this.title);
        dest.writeString(this.content);
        dest.writeByte(this.visbale ? (byte) 1 : (byte) 0);
        dest.writeParcelable(this.user, flags);
    }

    protected Friend(Parcel in) {
        this.friendimg = in.readString();
        this.friendid = (Integer) in.readValue(Integer.class.getClassLoader());
        this.photoimg = in.readString();
        this.name = in.readString();
        this.title = in.readString();
        this.content = in.readString();
        this.visbale = in.readByte() != 0;
        this.user = in.readParcelable(User.class.getClassLoader());
    }

    public static final Creator<Friend> CREATOR = new Creator<Friend>() {
        @Override
        public Friend createFromParcel(Parcel source) {
            return new Friend(source);
        }

        @Override
        public Friend[] newArray(int size) {
            return new Friend[size];
        }
    };
}
