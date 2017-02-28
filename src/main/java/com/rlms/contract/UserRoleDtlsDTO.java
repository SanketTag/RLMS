package com.rlms.contract;

public class UserRoleDtlsDTO {

	private Integer userId;
	private Integer companyId;
	private Integer spocRoleId;
	private Integer companyBranchMapId;
	private String name;
	private String contactNumber;
	private Integer countOfComplaintsAssigned;
	private String currentAddress;
	
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public Integer getCompanyId() {
		return companyId;
	}
	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}
	public Integer getSpocRoleId() {
		return spocRoleId;
	}
	public void setSpocRoleId(Integer spocRoleId) {
		this.spocRoleId = spocRoleId;
	}
	public Integer getCompanyBranchMapId() {
		return companyBranchMapId;
	}
	public void setCompanyBranchMapId(Integer companyBranchMapId) {
		this.companyBranchMapId = companyBranchMapId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getContactNumber() {
		return contactNumber;
	}
	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}
	public Integer getCountOfComplaintsAssigned() {
		return countOfComplaintsAssigned;
	}
	public void setCountOfComplaintsAssigned(Integer countOfComplaintsAssigned) {
		this.countOfComplaintsAssigned = countOfComplaintsAssigned;
	}
	public String getCurrentAddress() {
		return currentAddress;
	}
	public void setCurrentAddress(String currentAddress) {
		this.currentAddress = currentAddress;
	}
	
	
}
