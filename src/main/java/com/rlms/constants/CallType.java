package com.rlms.constants;

public enum CallType {

	 Complaint(0,"Complaint"),
	 Amc_Service_Call(1,"Amc Service Call");

	private int id;
	private String type;
	
	CallType(int id, String type){
		this.id = id;
		this.type = type;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
