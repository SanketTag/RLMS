package com.rlms.constants;

public enum XMPPServerDetails {
	
	FCM_PROJECT_SENDER_ID(1, "fcm_project_sender_id"),
	FCM_SERVER_KEY(2, "fcm_server_key");
	
	
	private Integer id;
	private String message;
	
	private XMPPServerDetails(int id, String message){
		this.id = id;
		this.message = message;
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	
}
