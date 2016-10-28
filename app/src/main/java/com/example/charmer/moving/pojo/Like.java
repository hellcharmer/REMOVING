package com.example.charmer.moving.pojo;

import java.io.Serializable;
import java.sql.Timestamp;

public class Like implements Serializable{

	private static final long serialVersionUID = 1L;
	private Integer likeId;
	private Timestamp likeTime;
	private User user;
	private Info info;
	
	public Like(){}
	public Like(Timestamp likeTime, User user, Info info) {
		super();
		this.likeTime = likeTime;
		this.user = user;
		this.info = info;
	}
	public Integer getLikeId() {
		return likeId;
	}
	public void setLikeId(Integer likeId) {
		this.likeId = likeId;
	}
	public Timestamp getLikeTime() {
		return likeTime;
	}
	public void setLikeTime(Timestamp likeTime) {
		this.likeTime = likeTime;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}

	public Like(Timestamp likeTime, User user) {
		super();
		this.likeTime = likeTime;
		this.user = user;
	}
	
	

}
