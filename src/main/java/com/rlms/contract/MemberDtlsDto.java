package com.rlms.contract;

import java.util.List;

public class MemberDtlsDto {

	private String firstName;
	private String lastName;
	private String companyName;
	private String contactNumber;
	private String address;
	private String emailId;
	private Integer customerId;
	private List<CustomerDtlsDto> listOfCustomerDtls;
	
	
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
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public Integer getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}
	public List<CustomerDtlsDto> getListOfCustomerDtls() {
		return listOfCustomerDtls;
	}
	public void setListOfCustomerDtls(List<CustomerDtlsDto> listOfCustomerDtls) {
		this.listOfCustomerDtls = listOfCustomerDtls;
	}
	
	
}
