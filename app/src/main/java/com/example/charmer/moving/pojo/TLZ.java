package com.example.charmer.moving.pojo;

public class TLZ {
 private String tlzid;
 private String tlzname;
 private String tlzusers;
 public TLZ(){}
public TLZ(String tlzid, String tlzname, String tlzusers) {
	super();
	this.tlzid = tlzid;
	this.tlzname = tlzname;
	this.tlzusers = tlzusers;
}
public String getTlzid() {
	return tlzid;
}
public void setTlzid(String tlzid) {
	this.tlzid = tlzid;
}
public String getTlzname() {
	return tlzname;
}
public void setTlzname(String tlzname) {
	this.tlzname = tlzname;
}
public String getTlzusers() {
	return tlzusers;
}
public void setTlzusers(String tlzusers) {
	this.tlzusers = tlzusers;
}
 
}
