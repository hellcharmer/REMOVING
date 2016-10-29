package com.example.charmer.moving.pojo;

import com.example.charmer.moving.utils.DateUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by asus on 2016/9/14.
 */
public class ListActivityBean {
    public int status;
    public int page;
    public List<Zixun> zixunlist;
    public List<Comments> commentList;
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
        public String  state;
        public Integer zixunId;
        public String photoImg;
        public String publisherimg;
        public String chenghao;
        public String type;
        public String publisher;
        public String timeStamp;
        public String content;
        public Integer likes;
        public Integer collections;
        public Integer pingluns;

        public String title;

        public Integer getZixunId() {
            return zixunId;
        }

        public void setZixunId(Integer zixunId) {
            this.zixunId = zixunId;
        }
    }
        public static class Comments {
        public Integer zixun_id;
        public String childDiscussantImg;
        public Long childDiscussant;
        public String childDiscussantName;
        public String childComment;
        public Long fatherDiscussant;
        public String  fatherDiscussantName;
        public String fatherComment;
        public String commentTime;

            public Comments(Integer zixun_id, Long childDiscussant, Long fatherDiscussant, Date commentTime, String childComment, String fatherComment) {
                this.zixun_id = zixun_id;
                this.childDiscussant = childDiscussant;
                this.fatherDiscussant = fatherDiscussant;
                this.commentTime = DateUtil.dateToString(commentTime);
                this.childComment = childComment;
                this.fatherComment = fatherComment;
            }
        }
}
