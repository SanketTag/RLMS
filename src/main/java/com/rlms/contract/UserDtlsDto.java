package com.rlms.contract;

public class UserDtlsDto {

    private String userName;
	private String firstName;
	private String lastName;
	private String companyName;
	private String contactNumber;
	private String address;
	private String emailId;
	private String branchName;
	private Integer userId;
	private Integer branchCompanyMapId;
	private Integer companyId;
	
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getContactNumber() {
		return contactNumber;
	}
	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public String getBranchName() {
		return branchName;
	}
	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public Integer getBranchCompanyMapId() {
		return branchCompanyMapId;
	}
	public void setBranchCompanyMapId(Integer branchCompanyMapId) {
		this.branchCompanyMapId = branchCompanyMapId;
	}
	public Integer getCompanyId() {
		return companyId;
	}
	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}
	
	
	
}
