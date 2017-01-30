package com.rlms.constants;

public enum EmailTemplateEnum {

	USER_ROLE_ASSIGNED(13,"USER_ROLE_ASSIGNED");
	

	private Integer templateId;
	private String tempName;
	
	EmailTemplateEnum(Integer templaeteId, String tempName){
		this.templateId = templaeteId;
		this.tempName = tempName;
	}

	public Integer getTemplateId() {
		return templateId;
	}

	public void setTemplateId(Integer templateId) {
		this.templateId = templateId;
	}

	public String getTempName() {
		return tempName;
	}

	public void setTempName(String tempName) {
		this.tempName = tempName;
	}
	
	
}
