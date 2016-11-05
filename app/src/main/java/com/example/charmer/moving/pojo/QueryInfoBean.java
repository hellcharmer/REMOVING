package com.example.charmer.moving.pojo;

import java.io.Serializable;
import java.util.List;


public class QueryInfoBean implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Boolean infoState;
	private Integer pageSize = 8; // 每页多少条
	private Integer pageNo; //  当前第几页
	private Integer totalPage;  //   总共多少页
	private Integer totalDate;  //   总共多少条数据
	private List<Info> infoList; //返回infoList
	public QueryInfoBean(Integer totalPage, List<Info> infoList) {
		super();
		this.totalPage = totalPage;
		this.infoList = infoList;
	}
	public Boolean getInfoState() {
		return infoState;
	}
	public void setInfoState(Boolean infoState) {
		this.infoState = infoState;
	}
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	
	public Integer getTotalPage() {
		return totalPage;
	}
	
	public Integer getPageNo() {
		return pageNo;
	}
	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}
	public Integer getTotalDate() {
		return totalDate;
	}
	public void setTotalDate(Integer totalDate) {
        this.totalDate = totalDate;
		
		if(this.getTotalDate()%this.getPageSize()==0){
			this.totalPage = this.getTotalDate()/this.getPageSize();
		}else{
			this.totalPage = this.getTotalDate()/this.getPageSize()+1;
		}
		 
	}
	public QueryInfoBean(){}
	public QueryInfoBean(Boolean infoState, Integer pageSize, Integer pageNo) {
		super();
		this.infoState = infoState;
		this.pageSize = pageSize;
		this.pageNo = pageNo;
	}
	
	public QueryInfoBean(Integer pageSize, Integer pageNo) {
		super();
	
		this.pageSize = pageSize;
		this.pageNo = pageNo;
	}
	public List<Info> getInfoList() {
		return infoList;
	}
	public void setInfoList(List<Info> infoList) {
		this.infoList = infoList;
	}
	

}
