package com.example.charmer.moving.pojo;

import java.io.Serializable;
import java.sql.Timestamp;

public class Remark implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public Integer dynamicRemarkId;
	public Integer infoId;
    public String childDiscussantImg;
    public Integer childDiscussant;
    public String childDiscussantName;
    public String childComment;
    public Integer fatherDiscussant;
    public String  fatherDiscussantName;
    public String fatherComment;
    public Timestamp commentTime;
    
    public Remark(Integer infoId, Integer childDiscussant, Timestamp commentTime) {
        this.infoId = infoId;
        this.childDiscussant = childDiscussant;
        this.commentTime = commentTime;
    }

    public Remark(){}
    public Remark(Integer infoId, Integer childDiscussant, Integer fatherDiscussant, Timestamp commentTime, String childComment, String fatherComment) {
        this.infoId = infoId;
        this.childDiscussant = childDiscussant;
        this.fatherDiscussant = fatherDiscussant;
        this.commentTime = commentTime;
        this.childComment = childComment;
        this.fatherComment = fatherComment;
    }

	public Integer getDynamicRemarkId() {
		return dynamicRemarkId;
	}

	public void setDynamicRemarkId(Integer dynamicRemarkId) {
		this.dynamicRemarkId = dynamicRemarkId;
	}

	public Integer getInfoId() {
		return infoId;
	}

	public Integer setInfoId(Integer infoId) {
		this.infoId = infoId;
		return infoId;
	}

	public String getChildDiscussantImg() {
		return childDiscussantImg;
	}

	public void setChildDiscussantImg(String childDiscussantImg) {
		this.childDiscussantImg = childDiscussantImg;
	}

	public Integer getChildDiscussant() {
		return childDiscussant;
	}

	public void setChildDiscussant(Integer childDiscussant) {
		this.childDiscussant = childDiscussant;
	}

	public String getChildDiscussantName() {
		return childDiscussantName;
	}

	public void setChildDiscussantName(String childDiscussantName) {
		this.childDiscussantName = childDiscussantName;
	}

	public String getChildComment() {
		return childComment;
	}

	public void setChildComment(String childComment) {
		this.childComment = childComment;
	}

	public Integer getFatherDiscussant() {
		return fatherDiscussant;
	}

	public void setFatherDiscussant(Integer fatherDiscussant) {
		this.fatherDiscussant = fatherDiscussant;
	}

	public String getFatherDiscussantName() {
		return fatherDiscussantName;
	}

	public void setFatherDiscussantName(String fatherDiscussantName) {
		this.fatherDiscussantName = fatherDiscussantName;
	}

	public String getFatherComment() {
		return fatherComment;
	}

	public void setFatherComment(String fatherComment) {
		this.fatherComment = fatherComment;
	}

	public Timestamp getCommentTime() {
		return commentTime;
	}

	public void setCommentTime(Timestamp commentTime) {
		this.commentTime = commentTime;
	}

	public Remark(Integer infoId, Integer childDiscussant, String childComment, Integer fatherDiscussant, String fatherComment) {
		this.infoId = infoId;
		this.childDiscussant = childDiscussant;
		this.childComment = childComment;
		this.fatherDiscussant = fatherDiscussant;
		this.fatherComment = fatherComment;
	}
}

