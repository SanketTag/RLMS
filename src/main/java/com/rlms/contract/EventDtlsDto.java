package com.rlms.contract;

import java.util.Date;

public class EventDtlsDto {

	private Integer userRoleId;
	private Integer companyId;
	private Integer eventId;
	private String eventType;
	private String eventDescription;
	private Integer liftCustomerMapId;
	private Date generatedDate;
	private String generatedDateStr;
	private Integer generatedBy;
	private Date updatedDate;
	private String updatedDateStr;
	private Integer updatedBy;
	private Integer activeFlag;
	public Integer getUserRoleId() {
		return userRoleId;
	}
	public void setUserRoleId(Integer userRoleId) {
		this.userRoleId = userRoleId;
	}
	public Integer getCompanyId() {
		return companyId;
	}
	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}
	public Integer getEventId() {
		return eventId;
	}
	public void setEventId(Integer eventId) {
		this.eventId = eventId;
	}
	public String getEventType() {
		return eventType;
	}
	public void setEventType(String eventType) {
		this.eventType = eventType;
	}
	public String getEventDescription() {
		return eventDescription;
	}
	public void setEventDescription(String eventDescription) {
		this.eventDescription = eventDescription;
	}
	public Integer getLiftCustomerMapId() {
		return liftCustomerMapId;
	}
	public void setLiftCustomerMapId(Integer liftCustomerMapId) {
		this.liftCustomerMapId = liftCustomerMapId;
	}
	public Date getGeneratedDate() {
		return generatedDate;
	}
	public void setGeneratedDate(Date generatedDate) {
		this.generatedDate = generatedDate;
	}
	public String getGeneratedDateStr() {
		return generatedDateStr;
	}
	public void setGeneratedDateStr(String generatedDateStr) {
		this.generatedDateStr = generatedDateStr;
	}
	public Integer getGeneratedBy() {
		return generatedBy;
	}
	public void setGeneratedBy(Integer generatedBy) {
		this.generatedBy = generatedBy;
	}
	public Date getUpdatedDate() {
		return updatedDate;
	}
	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}
	public String getUpdatedDateStr() {
		return updatedDateStr;
	}
	public void setUpdatedDateStr(String updatedDateStr) {
		this.updatedDateStr = updatedDateStr;
	}
	public Integer getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}
	public Integer getActiveFlag() {
		return activeFlag;
	}
	public void setActiveFlag(Integer activeFlag) {
		this.activeFlag = activeFlag;
	}
	
}
