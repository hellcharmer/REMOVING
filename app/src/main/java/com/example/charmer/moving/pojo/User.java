package com.example.charmer.moving.pojo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by lenovo on 2016/10/13.
 */
public class User implements Parcelable {
    private Integer userid;
    private String useraccount;
    private String userpsd;
    private String username;
    private String userphone;
    private String usertip;
    private String usercode;
    private String userimg;
    private boolean usersex;
    private String useradd;
    private Integer userpublish;
    private Integer usersucesspublish;
    private Integer userjoin;
    private Integer usersucessjoin;
    private Integer usergood;
    private Integer userbad;
    public User(){}

    public User(String username, String userimg) {
        this.username = username;
        this.userimg = userimg;
    }
    public User(Integer userid){
        this.userid = userid;
    }

    public User(Integer userid,String useraccount) {
        this.userid = userid;
        this.useraccount = useraccount;
    }

    public Integer getUsersucessjoin() {
        return usersucessjoin;
    }

    public void setUsersucessjoin(Integer usersucessjoin) {
        this.usersucessjoin = usersucessjoin;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public String getUseraccount() {
        return useraccount;
    }

    public void setUseraccount(String useraccount) {
        this.useraccount = useraccount;
    }

    public String getUserpsd() {
        return userpsd;
    }

    public void setUserpsd(String userpsd) {
        this.userpsd = userpsd;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserphone() {
        return userphone;
    }

    public void setUserphone(String userphone) {
        this.userphone = userphone;
    }

    public String getUsertip() {
        return usertip;
    }

    public void setUsertip(String usertip) {
        this.usertip = usertip;
    }

    public String getUsercode() {
        return usercode;
    }

    public void setUsercode(String usercode) {
        this.usercode = usercode;
    }

    public String getUserimg() {
        return userimg;
    }

    public void setUserimg(String userimg) {
        this.userimg = userimg;
    }

    public boolean isUsersex() {
        return usersex;
    }

    public void setUsersex(boolean usersex) {
        this.usersex = usersex;
    }

    public String getUseradd() {
        return useradd;
    }

    public void setUseradd(String useradd) {
        this.useradd = useradd;
    }

    public Integer getUserpublish() {
        return userpublish;
    }

    public void setUserpublish(Integer userpublish) {
        this.userpublish = userpublish;
    }

    public Integer getUsersucesspublish() {
        return usersucesspublish;
    }

    public void setUsersucesspublish(Integer usersucesspublish) {
        this.usersucesspublish = usersucesspublish;
    }

    public Integer getUserjoin() {
        return userjoin;
    }

    public void setUserjoin(Integer userjoin) {
        this.userjoin = userjoin;
    }



    public Integer getUsergood() {
        return usergood;
    }

    public void setUsergood(Integer usergood) {
        this.usergood = usergood;
    }

    public Integer getUserbad() {
        return userbad;
    }

    public void setUserbad(Integer userbad) {
        this.userbad = userbad;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.userid);
        dest.writeString(this.useraccount);
        dest.writeString(this.userpsd);
        dest.writeString(this.username);
        dest.writeString(this.userphone);
        dest.writeString(this.usertip);
        dest.writeString(this.usercode);
        dest.writeString(this.userimg);
        dest.writeByte(this.usersex ? (byte) 1 : (byte) 0);
        dest.writeString(this.useradd);
        dest.writeValue(this.userpublish);
        dest.writeValue(this.usersucesspublish);
        dest.writeValue(this.userjoin);
        dest.writeValue(this.usersucessjoin);
        dest.writeValue(this.usergood);
        dest.writeValue(this.userbad);
    }

    protected User(Parcel in) {
        this.userid = (Integer) in.readValue(Integer.class.getClassLoader());
        this.useraccount = in.readString();
        this.userpsd = in.readString();
        this.username = in.readString();
        this.userphone = in.readString();
        this.usertip = in.readString();
        this.usercode = in.readString();
        this.userimg = in.readString();
        this.usersex = in.readByte() != 0;
        this.useradd = in.readString();
        this.userpublish = (Integer) in.readValue(Integer.class.getClassLoader());
        this.usersucesspublish = (Integer) in.readValue(Integer.class.getClassLoader());
        this.userjoin = (Integer) in.readValue(Integer.class.getClassLoader());
        this.usersucessjoin = (Integer) in.readValue(Integer.class.getClassLoader());
        this.usergood = (Integer) in.readValue(Integer.class.getClassLoader());
        this.userbad = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
