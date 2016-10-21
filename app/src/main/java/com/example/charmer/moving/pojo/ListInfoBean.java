package com.example.charmer.moving.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/9/15.
 */
public class ListInfoBean implements Serializable{
    public int status;
    public List<Info> infoList;

    public static class Info implements Serializable{  //必须是static 类型
        public Integer infoId;
        public String photoImg;
        public String infoContent;
        public String infoName;
        public String infoDate;


    }
}
