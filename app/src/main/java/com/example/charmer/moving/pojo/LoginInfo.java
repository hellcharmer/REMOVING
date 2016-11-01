package com.example.charmer.moving.pojo;

public class LoginInfo {
	
	private Integer loginId;
	private String loginName;
	private String loginPsd;
	private String phoneNum;
	private String QRcode;
	public Integer getLoginId() {
		return loginId;
	}
	public void setLoginId(Integer loginId) {
		this.loginId = loginId;
	}
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public String getLoginPsd() {
		return loginPsd;
	}
	public void setLoginPsd(String loginPsd) {
		this.loginPsd = loginPsd;
	}
	public String getPhoneNum() {
		return phoneNum;
	}
	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}
	public String getQRcode() {
		return QRcode;
	}
	public void setQRcode(String qRcode) {
		QRcode = qRcode;
	}
	public LoginInfo(){
		
	}
	public LoginInfo(String phoneNum, String loginPsd){
		super();
		this.phoneNum = phoneNum;
		this.loginPsd = loginPsd;
	}
	public LoginInfo(String loginName, String loginPsd,
			String phoneNum, String qRcode) {
		super();
		this.loginName = loginName;
		this.loginPsd = loginPsd;
		this.phoneNum = phoneNum;
		QRcode = qRcode;
	}
	
	

}
