package com.example.charmer.moving.pojo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asus on 2016/9/14.
 */
public class ListActivityBean {
    public int status;
    public int page;
    public List<Zixun> zixunlist;
        public ArrayList<Friend> friendlist;


        public static class Friend{
            public Integer friendId;
            public String photoImg;
            public String name;
            public String title;
            public String content;
            public Integer dianzan;

            @Override
            public String toString() {
                return "Friend{" +
                        "friendId=" + friendId +
                        ", photoImg='" + photoImg + '\'' +
                        ", name='" + name + '\'' +
                        ", title='" + title + '\'' +
                        ", content='" + content + '\'' +
                        ", dianzan=" + dianzan +
                        '}';
            }
        }


    public static class Zixun {

        public Integer zixunId;
        public String photoImg;
        public String author;
        public String chenghao;
        public String type;
        public String timeStamp;
        public String content;
        public Integer likes;
        public Integer dianzanshu;
        public String title;

        public Integer getZixunId() {
            return zixunId;
        }

        public void setZixunId(Integer zixunId) {
            this.zixunId = zixunId;
        }
    }
}
