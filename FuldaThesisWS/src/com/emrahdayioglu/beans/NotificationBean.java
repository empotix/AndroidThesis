package com.emrahdayioglu.beans;

import java.util.Date;

public class NotificationBean extends CompBean{
	
	private int userId;
	private int couponId;
	private String message;
	private Date creationDate;
	private int errorId;
	private String errorMessage;
	private int errorPriority;
	
	
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getCouponId() {
		return couponId;
	}
	public void setCouponId(int couponId) {
		this.couponId = couponId;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	public int getErrorId() {
		return errorId;
	}
	public void setErrorId(int errorId) {
		this.errorId = errorId;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	public int getErrorPriority() {
		return errorPriority;
	}
	public void setErrorPriority(int errorPriority) {
		this.errorPriority = errorPriority;
	}
	
	

}
