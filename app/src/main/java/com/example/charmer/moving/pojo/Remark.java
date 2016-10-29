package com.example.charmer.moving.pojo;

import java.util.Date;

public class Remark {
	
		private String remarkId;
		private String remarkContent;
		private Date  remarkTime;
		private User mCommentator; // 评论者（即谁评论）
	    public User mReceiver; // 接收者（即回复谁）
		public Remark(String remarkId, String remarkContent,
				Date remarkTime, User mCommentator,User mReceiver) {
			this.remarkId = remarkId;
			this.remarkContent = remarkContent;
			this.remarkTime = remarkTime;
			this.mCommentator = mCommentator;
			this.mReceiver = mReceiver;
		}
		//		private Dynamic dynamic;
		public String getRemarkId() {
			return remarkId;
		}
		public void setRemarkId(String remarkId) {
			this.remarkId = remarkId;
		}
		public String getRemarkContent() {
			return remarkContent;
		}
		public void setRemarkContent(String remarkContent) {
			this.remarkContent = remarkContent;
		}
		public Date getRemarkTime() {
			return remarkTime;
		}
		public void setRemarkTime(Date remarkTime) {
			this.remarkTime = remarkTime;
		}


	public User getmCommentator() {
		return mCommentator;
	}

	public void setmCommentator(User mCommentator) {
		this.mCommentator = mCommentator;
	}

	public User getmReceiver() {
		return mReceiver;
	}

	public void setmReceiver(User mReceiver) {
		this.mReceiver = mReceiver;
	}

	public Remark(User mCommentator, String remarkContent, User mReceiver) {
		this.mCommentator = mCommentator;
		this.remarkContent = remarkContent;
		this.mReceiver = mReceiver;
	}
		
		
}
