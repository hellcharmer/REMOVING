package com.example.charmer.moving.pojo;

/**
 * Created by lenovo on 2016/11/2.
 */

public class Qun {
public Integer qunid;
    public String qunname;
    public String qunuserids;
    public Integer qunmaster;
    public  String qunimg;
    public Qun(){}
    public Qun(Integer qunid, String qunname, String qunuserids, Integer qunmaster, String qunimg) {
        this.qunid = qunid;
        this.qunname = qunname;
        this.qunuserids = qunuserids;
        this.qunmaster = qunmaster;
        this.qunimg = qunimg;
    }

    public Integer getQunid() {
        return qunid;
    }

    public void setQunid(Integer qunid) {
        this.qunid = qunid;
    }

    public String getQunname() {
        return qunname;
    }

    public void setQunname(String qunname) {
        this.qunname = qunname;
    }

    public String getQunuserids() {
        return qunuserids;
    }

    public void setQunuserids(String qunuserids) {
        this.qunuserids = qunuserids;
    }

    public Integer getQunmaster() {
        return qunmaster;
    }

    public void setQunmaster(Integer qunmaster) {
        this.qunmaster = qunmaster;
    }

    public String getQunimg() {
        return qunimg;
    }

    public void setQunimg(String qunimg) {
        this.qunimg = qunimg;
    }
}
