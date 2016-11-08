package com.example.charmer.moving.pojo;

public class Post {
	private  Integer post;
   private Integer postid;
   private String postname;
   private String postimg;
   private Integer getid;
   private Integer state;
   private String getname;
   private Integer agree;
   public Post(){}

	public Post(Integer post, Integer postid, String postname, String postimg, Integer getid, Integer state, String getname, Integer agree) {
		this.post = post;
		this.postid = postid;
		this.postname = postname;
		this.postimg = postimg;
		this.getid = getid;
		this.state = state;
		this.getname = getname;
		this.agree = agree;
	}

	public Integer getPost() {
		return post;
	}

	public void setPost(Integer post) {
		this.post = post;
	}

	public Integer getPostid() {
	return postid;
}
public void setPostid(Integer postid) {
	this.postid = postid;
}
public String getPostname() {
	return postname;
}
public void setPostname(String postname) {
	this.postname = postname;
}
public String getPostimg() {
	return postimg;
}
public void setPostimg(String postimg) {
	this.postimg = postimg;
}
public Integer getGetid() {
	return getid;
}
public void setGetid(Integer getid) {
	this.getid = getid;
}
public Integer getState() {
	return state;
}
public void setState(Integer state) {
	this.state = state;
}
public String getGetname() {
	return getname;
}
public void setGetname(String getname) {
	this.getname = getname;
}
public Integer getAgree() {
	return agree;
}
public void setAgree(Integer agree) {
	this.agree = agree;
}
   
}
