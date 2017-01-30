package com.rlms.constants;

public enum Status {

	PENDING(2, "Pending"),
	ASSIGNED(3,"Assigned"),
	INPROGESS(4,"In Progress"),
	RESOLVED(5,"Resolved");
	
	private Integer statusId;
	private String statusMsg;
	
	private Status(Integer statusId, String statusMsg){
		this.statusId = statusId;
		this.statusMsg = statusMsg;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public String getStatusMsg() {
		return statusMsg;
	}

	public void setStatusMsg(String statusMsg) {
		this.statusMsg = statusMsg;
	}
	
	
}
