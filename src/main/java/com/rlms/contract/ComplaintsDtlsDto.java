package com.rlms.contract;

import java.util.List;

public class ComplaintsDtlsDto {

	private Integer branchCompanyMapId;
	private Integer liftCustomerMapId;
	private String complaintsTitle;
	private String complaintsRemark;
	private Integer registrationType;
	private List<Integer> statusList;
	
	public Integer getLiftCustomerMapId() {
		return liftCustomerMapId;
	}
	public void setLiftCustomerMapId(Integer liftCustomerMapId) {
		this.liftCustomerMapId = liftCustomerMapId;
	}
	public String getComplaintsTitle() {
		return complaintsTitle;
	}
	public void setComplaintsTitle(String complaintsTitle) {
		this.complaintsTitle = complaintsTitle;
	}
	public String getComplaintsRemark() {
		return complaintsRemark;
	}
	public void setComplaintsRemark(String complaintsRemark) {
		this.complaintsRemark = complaintsRemark;
	}
	public Integer getRegistrationType() {
		return registrationType;
	}
	public void setRegistrationType(Integer registrationType) {
		this.registrationType = registrationType;
	}
	public Integer getBranchCompanyMapId() {
		return branchCompanyMapId;
	}
	public void setBranchCompanyMapId(Integer branchCompanyMapId) {
		this.branchCompanyMapId = branchCompanyMapId;
	}
	public List<Integer> getStatusList() {
		return statusList;
	}
	public void setStatusList(List<Integer> statusList) {
		this.statusList = statusList;
	}
	
	
	
}
