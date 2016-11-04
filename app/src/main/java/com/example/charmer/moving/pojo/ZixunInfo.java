package com.example.charmer.moving.pojo;

import java.util.Date;

public class ZixunInfo {
	
	private int zixun_id;
	private String zixun_name;
	private String zixun_type;
	private Date zixun_issuedate;
	private String zixun_text; 
	private String zixun_photo ;
	private String zixun_video;
	private String publisher;
	private String publisher_img;
	private int zixun_likes;
	private int zixun_collections;
	private int zixun_pingluns;
	private boolean state;
	
	
	
	public ZixunInfo(){}
	
	
	public ZixunInfo(int zixunId, String zixunName, String zixunType,
			Date zixunIssuedate, String zixunText, String zixunPhoto,
			String zixunVideo) {
		super();
		zixun_id = zixunId;
		zixun_name = zixunName;
		zixun_type = zixunType;
		zixun_issuedate = zixunIssuedate;
		zixun_text = zixunText;
		zixun_photo = zixunPhoto;
		zixun_video = zixunVideo;
		
	}


	public ZixunInfo(String zixunName, String zixunType, Date zixunIssuedate,
			String zixunText, String zixunPhoto, String zixunVideo,String publisher) {
		super();
		zixun_name = zixunName;
		zixun_type = zixunType;
		zixun_issuedate = zixunIssuedate;
		zixun_text = zixunText;
		zixun_photo = zixunPhoto;
		zixun_video = zixunVideo;
		this.publisher = publisher;
	}


	public int getZixun_likes() {
		return zixun_likes;
	}


	public void setZixun_likes(int zixunLikes) {
		zixun_likes = zixunLikes;
	}


	public int getZixun_id() {
		return zixun_id;
	}
	public void setZixun_id(int zixunId) {
		zixun_id = zixunId;
	}
	public String getZixun_name() {
		return zixun_name;
	}
	public void setZixun_name(String zixunName) {
		zixun_name = zixunName;
	}
	public String getZixun_type() {
		return zixun_type;
	}
	public void setZixun_type(String zixunType) {
		zixun_type = zixunType;
	}
	
	
	public Date getZixun_issuedate() {
		return zixun_issuedate;
	}

	public void setZixun_issuedate(Date zixunIssuedate) {
		zixun_issuedate = zixunIssuedate;
	}

	public String getZixun_text() {
		return zixun_text;
	}
	public void setZixun_text(String zixunText) {
		zixun_text = zixunText;
	}
	public String getZixun_photo() {
		return zixun_photo;
	}
	public void setZixun_photo(String zixunPhoto) {
		zixun_photo = zixunPhoto;
	}
	public String getZixun_video() {
		return zixun_video;
	}
	public void setZixun_video(String zixunVideo) {
		zixun_video = zixunVideo;
	}
	


	public int getZixun_collections() {
		return zixun_collections;
	}


	public void setZixun_collections(int zixunCollections) {
		zixun_collections = zixunCollections;
	}


	public boolean isState() {
		return state;
	}


	public void setState(boolean state) {
		this.state = state;
	}


	public String getPublisher() {
		return publisher;
	}


	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	

	public int getZixun_pingluns() {
		return zixun_pingluns;
	}


	public void setZixun_pingluns(int zixunPingluns) {
		zixun_pingluns = zixunPingluns;
	}

	
	public String getPublisher_img() {
		return publisher_img;
	}


	public void setPublisher_img(String publisherImg) {
		publisher_img = publisherImg;
	}


	@Override
	public String toString() {
		return "ZixunInfo [publisher=" + publisher + ", publisher_img="
				+ publisher_img + "]";
	}




	
}