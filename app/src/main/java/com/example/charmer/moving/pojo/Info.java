package com.example.charmer.moving.pojo;

import java.io.Serializable;
import java.sql.Timestamp;

public class Info implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer infoId;
	private String infoContent;
	private Timestamp infoDate;
	private boolean infoState;
    private String infoPhotoImg;
    private Integer infoLikeNum;
    public Integer getInfoLikeNum() {
		return infoLikeNum;
	}
	public void setInfoLikeNum(Integer infoLikeNum) {
		this.infoLikeNum = infoLikeNum;
	}
	private User user;
    private Like like;
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Like getLike() {
		return like;
	}
	public void setLike(Like like) {
		this.like = like;
	}
	public Integer getInfoId() {
		return infoId;
	}
	public void setInfoId(Integer infoId) {
		this.infoId = infoId;
	}
	public String getInfoContent() {
		return infoContent;
	}
	public void setInfoContent(String infoContent) {
		this.infoContent = infoContent;
	}
	public Timestamp getInfoDate() {
		return infoDate;
	}
	public void setInfoDate(Timestamp infoDate) {
		this.infoDate = infoDate;
	}
	public boolean isInfoState() {
		return infoState;
	}
	public void setInfoState(boolean infoState) {
		this.infoState = infoState;
	}
	public String getInfoPhotoImg() {
		return infoPhotoImg;
	}
	public void setInfoPhotoImg(String infoPhotoImg) {
		this.infoPhotoImg = infoPhotoImg;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	public Info(){}
	public Info(String infoContent, Timestamp infoDate,
			boolean infoState, String infoPhotoImg,Integer infoLikeNum, User user ,Like like) {
		super();
		this.infoContent = infoContent;
		this.infoDate = infoDate;
		this.infoState = infoState;
		this.infoPhotoImg = infoPhotoImg;
		this.infoLikeNum = infoLikeNum;
		this.user = user;
		this.like = like;
	}
	public Info(Integer infoId, String infoContent, Timestamp infoDate,
			boolean infoState,Integer infoLikeNum, User user) {
		super();
		this.infoId = infoId;
		this.infoContent = infoContent;
		this.infoDate = infoDate;
		this.infoState = infoState;
		this.infoLikeNum = infoLikeNum;
		this.user = user;
	}

	public Info(Integer infoId, String infoContent, Timestamp infoDate,
				boolean infoState, String infoPhotoImg,Integer infoLikeNum, User user) {
		super();
		this.infoId = infoId;
		this.infoContent = infoContent;
		this.infoDate = infoDate;
		this.infoState = infoState;
		this.infoPhotoImg = this.infoPhotoImg;
		this.infoLikeNum = infoLikeNum;
		this.user = user;
	}

	public Info(Integer infoId,Integer infoLikeNum) {
		super();
		this.infoId = infoId;
		this.infoLikeNum = infoLikeNum;
	}




}


