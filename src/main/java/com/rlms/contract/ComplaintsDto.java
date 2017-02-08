package com.rlms.contract;

import java.util.Date;

import com.rlms.model.RlmsLiftCustomerMap;

public class ComplaintsDto {

	private String complaintNumber;
	private Date registrationDate;
	private Date serviceStartDate;
	private Date actualServiceEndDate;
	private String liftNumber;
	private String customerName;
	private String liftAddress;
	private String latitude;
	private String longitude;
	private Integer registrationType;
	private String registrationTypeStr;
	private String remark;
	private String status;
	private String title;
	public String getComplaintNumber() {
		return complaintNumber;
	}
	public void setComplaintNumber(String complaintNumber) {
		this.complaintNumber = complaintNumber;
	}
	public Date getRegistrationDate() {
		return registrationDate;
	}
	public void setRegistrationDate(Date registrationDate) {
		this.registrationDate = registrationDate;
	}
	public Date getServiceStartDate() {
		return serviceStartDate;
	}
	public void setServiceStartDate(Date serviceStartDate) {
		this.serviceStartDate = serviceStartDate;
	}
	public Date getActualServiceEndDate() {
		return actualServiceEndDate;
	}
	public void setActualServiceEndDate(Date actualServiceEndDate) {
		this.actualServiceEndDate = actualServiceEndDate;
	}
	public String getLiftNumber() {
		return liftNumber;
	}
	public void setLiftNumber(String liftNumber) {
		this.liftNumber = liftNumber;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getLiftAddress() {
		return liftAddress;
	}
	public void setLiftAddress(String liftAddress) {
		this.liftAddress = liftAddress;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public Integer getRegistrationType() {
		return registrationType;
	}
	public void setRegistrationType(Integer registrationType) {
		this.registrationType = registrationType;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getRegistrationTypeStr() {
		return registrationTypeStr;
	}
	public void setRegistrationTypeStr(String registrationTypeStr) {
		this.registrationTypeStr = registrationTypeStr;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	
}
