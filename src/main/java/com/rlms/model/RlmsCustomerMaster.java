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
@Table(name="rlms_customer_master")
public class RlmsCustomerMaster implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer customerId;
	private String customerName;
	private String address;
	private String vatNumber;
	private String tinNumber;
	private String panNumber;
	private Integer customerType;
	private String emailID;
	private String cntNumber;
	private Integer activeFlag;
	private Date createdDate;
	private Integer createdBy;
	private Date updatedDate;
	private Integer updatedBy;
	
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "customer_id", unique = true, nullable = false)
	public Integer getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}
	
	@Column(name = "customer_name", unique = true, nullable = false)
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	
	@Column(name = "address", unique = true, nullable = false)
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
	@Column(name = "vat_number", unique = true, nullable = false)
	public String getVatNumber() {
		return vatNumber;
	}
	public void setVatNumber(String vatNumber) {
		this.vatNumber = vatNumber;
	}
	
	@Column(name = "tin_number", unique = true, nullable = false)
	public String getTinNumber() {
		return tinNumber;
	}
	public void setTinNumber(String tinNumber) {
		this.tinNumber = tinNumber;
	}
	
	@Column(name = "pan_number", unique = true, nullable = false)
	public String getPanNumber() {
		return panNumber;
	}
	public void setPanNumber(String panNumber) {
		this.panNumber = panNumber;
	}
	
	@Column(name = "customer_type", unique = true, nullable = false)
	public Integer getCustomerType() {
		return customerType;
	}
	public void setCustomerType(Integer customerType) {
		this.customerType = customerType;
	}
	
	@Column(name = "email_id", unique = true, nullable = false)
	public String getEmailID() {
		return emailID;
	}
	public void setEmailID(String emailID) {
		this.emailID = emailID;
	}
	
	@Column(name = "cnt_number", unique = true, nullable = false)
	public String getCntNumber() {
		return cntNumber;
	}
	public void setCntNumber(String cntNumber) {
		this.cntNumber = cntNumber;
	}
	
	@Column(name = "active_flag", unique = true, nullable = false)
	public Integer getActiveFlag() {
		return activeFlag;
	}
	public void setActiveFlag(Integer activeFlag) {
		this.activeFlag = activeFlag;
	}
	
	@Column(name = "created_date", unique = true, nullable = false)
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	
	@Column(name = "created_by", unique = true, nullable = false)
	public Integer getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
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
