package com.rlms.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "rlms_company_master")
public class RlmsCompanyMaster implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer companyId;
	private String companyName;
	private String address;
	private String contactNumber;
	private String emailId;
	private String panNumber;
	private String tinNumber;
	private String vatNumber;
	private Integer activeFlag;
	private Integer status;
	private Date createdDate;
	private Integer cretedBy;
	private Date updatedDate;
	private Integer updatedBy;
	
	public RlmsCompanyMaster() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "company_id", unique = true, nullable = false)
	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	@Column(name = "company_name", unique = true, nullable = false)
	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	@Column(name = "address", unique = true, nullable = false)
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Column(name = "contact_number", unique = true, nullable = false)
	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	@Column(name = "email_id", unique = true, nullable = false)
	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	@Column(name = "pan_number", unique = true, nullable = false)
	public String getPanNumber() {
		return panNumber;
	}

	public void setPanNumber(String panNumber) {
		this.panNumber = panNumber;
	}

	@Column(name = "tin_number", unique = true, nullable = false)
	public String getTinNumber() {
		return tinNumber;
	}

	public void setTinNumber(String tinNumber) {
		this.tinNumber = tinNumber;
	}

	@Column(name = "vat_number", unique = true, nullable = false)
	public String getVatNumber() {
		return vatNumber;
	}

	public void setVatNumber(String vatNumber) {
		this.vatNumber = vatNumber;
	}

	@Column(name = "active_flag", unique = true, nullable = false)
	public Integer getActiveFlag() {
		return activeFlag;
	}

	public void setActiveFlag(Integer activeFlag) {
		this.activeFlag = activeFlag;
	}

	@Column(name = "status", unique = true, nullable = false)
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Column(name = "created_date", unique = true, nullable = false)
	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	@Column(name = "created_by", unique = true, nullable = false)
	public Integer getCretedBy() {
		return cretedBy;
	}

	public void setCretedBy(Integer cretedBy) {
		this.cretedBy = cretedBy;
	}

	@Column(name = "updated_date", unique = true, nullable = false)
	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	@Column(name = "updated_by", unique = true, nullable = false)
	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}
	
	
	
	
}
