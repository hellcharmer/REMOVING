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
            public boolean isShowAll;
        public String  state;
        public Integer zixunId;
        public String photoImg;
        public String publisheraccount;
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

            public String getPhotoImg() {
                return photoImg;
            }

            public void setPhotoImg(String photoImg) {
                this.photoImg = photoImg;
            }

            public String getPublisherimg() {
                return publisherimg;
            }

            public void setPublisherimg(String publisherimg) {
                this.publisherimg = publisherimg;
            }

            public String getChenghao() {
                return chenghao;
            }

            public void setChenghao(String chenghao) {
                this.chenghao = chenghao;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getPublisher() {
                return publisher;
            }

            public void setPublisher(String publisher) {
                this.publisher = publisher;
            }

            public String getTimeStamp() {
                return timeStamp;
            }

            public void setTimeStamp(String timeStamp) {
                this.timeStamp = timeStamp;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public Integer getLikes() {
                return likes;
            }

            public void setLikes(Integer likes) {
                this.likes = likes;
            }

            public Integer getCollections() {
                return collections;
            }

            public void setCollections(Integer collections) {
                this.collections = collections;
            }

            public Integer getPingluns() {
                return pingluns;
            }

            public void setPingluns(Integer pingluns) {
                this.pingluns = pingluns;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getState() {
                return state;
            }

            public void setState(String state) {
                this.state = state;
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

            public Comments(Integer zixun_id, Long childDiscussant, String commentTime) {
                this.zixun_id = zixun_id;
                this.childDiscussant = childDiscussant;
                this.commentTime = commentTime;
            }

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
