package com.rlms.constants;

public enum UserType {

	END_USER(1,"END_USER"),
	 TECHNICIAN(2,"TECHNICIAN");

	private int id;
	private String type;
	
	UserType(int id, String type){
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
}
