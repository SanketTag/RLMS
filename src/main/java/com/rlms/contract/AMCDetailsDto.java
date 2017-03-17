package com.rlms.contract;

import java.util.List;

public class AMCDetailsDto {

	private Integer companyId;
	private Integer branchCompanyMapId;
	private List<Integer> liftCustomerMapId;
	private List<Integer> listOfBranchCustomerMapId;
	private List<Integer> listOFStatusIds;
	private String liftNumber;
	private String customerName;
	private String amcStartDate;
	private String amcEndDate;
	private String lackStartDate;
	private String lackEndDate;
	private String status;
	private String dueDate;
	private String amcAmount;
	private String slackStartDate;
	private String slackEndDate;
	private Integer slackperiod;
	private Integer liftCustoMapId;
	
	public Integer getCompanyId() {
		return companyId;
	}
	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}
	public Integer getBranchCompanyMapId() {
		return branchCompanyMapId;
	}
	public void setBranchCompanyMapId(Integer branchCompanyMapId) {
		this.branchCompanyMapId = branchCompanyMapId;
	}
	public List<Integer> getLiftCustomerMapId() {
		return liftCustomerMapId;
	}
	public void setLiftCustomerMapId(List<Integer> liftCustomerMapId) {
		this.liftCustomerMapId = liftCustomerMapId;
	}
	public List<Integer> getListOfBranchCustomerMapId() {
		return listOfBranchCustomerMapId;
	}
	public void setListOfBranchCustomerMapId(List<Integer> listOfBranchCustomerMapId) {
		this.listOfBranchCustomerMapId = listOfBranchCustomerMapId;
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
	public String getAmcStartDate() {
		return amcStartDate;
	}
	public void setAmcStartDate(String amcStartDate) {
		this.amcStartDate = amcStartDate;
	}
	public String getAmcEndDate() {
		return amcEndDate;
	}
	public void setAmcEndDate(String amcEndDate) {
		this.amcEndDate = amcEndDate;
	}
	public String getLackStartDate() {
		return lackStartDate;
	}
	public void setLackStartDate(String lackStartDate) {
		this.lackStartDate = lackStartDate;
	}
	public String getLackEndDate() {
		return lackEndDate;
	}
	public void setLackEndDate(String lackEndDate) {
		this.lackEndDate = lackEndDate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getDueDate() {
		return dueDate;
	}
	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}
	public List<Integer> getListOFStatusIds() {
		return listOFStatusIds;
	}
	public void setListOFStatusIds(List<Integer> listOFStatusIds) {
		this.listOFStatusIds = listOFStatusIds;
	}
	public String getAmcAmount() {
		return amcAmount;
	}
	public void setAmcAmount(String amcAmount) {
		this.amcAmount = amcAmount;
	}
	public String getSlackStartDate() {
		return slackStartDate;
	}
	public void setSlackStartDate(String slackStartDate) {
		this.slackStartDate = slackStartDate;
	}
	public String getSlackEndDate() {
		return slackEndDate;
	}
	public void setSlackEndDate(String slackEndDate) {
		this.slackEndDate = slackEndDate;
	}
	public Integer getSlackperiod() {
		return slackperiod;
	}
	public void setSlackperiod(Integer slackperiod) {
		this.slackperiod = slackperiod;
	}
	public Integer getLiftCustoMapId() {
		return liftCustoMapId;
	}
	public void setLiftCustoMapId(Integer liftCustoMapId) {
		this.liftCustoMapId = liftCustoMapId;
	}
	
	
	
}
