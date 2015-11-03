package com.example.bean;

import java.util.Date;

public class LeaveWord {
	
	private int leaveWordId;
	//±ª¡Ù—‘’ﬂ
	private int ToUserId;
	//¡Ù—‘’ﬂ
	private int fromUserid;
	private String fromUser_name;
	private Date fromLeaveWordtime;
	private String LeaverWordContent;
	public int getToUserId() {
		return ToUserId;
	}
	public void setToUserId(int toUserId) {
		ToUserId = toUserId;
	}
	public int getFromUserid() {
		return fromUserid;
	}
	public void setFromUserid(int fromUserid) {
		this.fromUserid = fromUserid;
	}
	public String getFromUser_name() {
		return fromUser_name;
	}
	public void setFromUser_name(String fromUser_name) {
		this.fromUser_name = fromUser_name;
	}
	public Date getFromLeaveWordtime() {
		return fromLeaveWordtime;
	}
	public void setFromLeaveWordtime(Date fromLeaveWordtime) {
		this.fromLeaveWordtime = fromLeaveWordtime;
	}
	public String getLeaverWordContent() {
		return LeaverWordContent;
	}
	public void setLeaverWordContent(String leaverWordContent) {
		LeaverWordContent = leaverWordContent;
	}
	public int getLeaveWordId() {
		return leaveWordId;
	}
	public void setLeaveWordId(int leaveWordId) {
		this.leaveWordId = leaveWordId;
	}

}
