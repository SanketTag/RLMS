package com.rlms.contract;

public class CompanyDtlsDTO {

	private Integer companyId;
	private String companyName;
	private String address;
	private String contactNumber;
	private String emailId;
	private Long panNumber;
	private Long tinNumber;
	private Long vatNumber;
	
	
	
	public Integer getCompanyId() {
		return companyId;
	}
	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getContactNumber() {
		return contactNumber;
	}
	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public Long getPanNumber() {
		return panNumber;
	}
	public void setPanNumber(Long panNumber) {
		this.panNumber = panNumber;
	}
	public Long getTinNumber() {
		return tinNumber;
	}
	public void setTinNumber(Long tinNumber) {
		this.tinNumber = tinNumber;
	}
	public Long getVatNumber() {
		return vatNumber;
	}
	public void setVatNumber(Long vatNumber) {
		this.vatNumber = vatNumber;
	}
	
	
}
