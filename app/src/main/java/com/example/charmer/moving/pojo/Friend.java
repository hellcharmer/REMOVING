package com.example.charmer.moving.pojo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Jingsheng Liang on 2016/9/13.
 */
public class Friend implements Parcelable {

    public Integer friendId;
    public String photoImg;
    public String name;
    public String title;
    public String content;
    public Integer dianzan;

    // alt+insert

    public Friend(){}

    public Friend(Integer friendId, String name, String title, String content) {
        this.friendId = friendId;
        this.name = name;
        this.title = title;
        this.content = content;
    }

    public Integer getFriendId() {
        return friendId;
    }

    public void setFriendId(Integer friendId) {
        this.friendId = friendId;
    }

    public String getPhotoImg() {
        return photoImg;
    }

    public void setPhotoImg(String photoImg) {
        this.photoImg = photoImg;
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

    public Integer getDianzan() {
        return dianzan;
    }

    public void setDianzan(Integer dianzan) {
        this.dianzan = dianzan;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.friendId);
        dest.writeString(this.photoImg);
        dest.writeString(this.name);
        dest.writeString(this.title);
        dest.writeString(this.content);
        dest.writeValue(this.dianzan);
    }

    protected Friend(Parcel in) {
        this.friendId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.photoImg = in.readString();
        this.name = in.readString();
        this.title = in.readString();
        this.content = in.readString();
        this.dianzan = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public static final Parcelable.Creator<Friend> CREATOR = new Parcelable.Creator<Friend>() {
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
