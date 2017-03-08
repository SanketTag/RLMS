package com.rlms.contract;

import java.util.List;

import org.json.JSONArray;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;



public class ResponseDto {

	private boolean status;
	private Object response;
	private JsonElement jsonElement;
	private JsonArray jsonArray;
	

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public Object getResponse() {
		return response;
	}

	public void setResponse(Object response) {
		this.response = response;
	}

	public JsonElement getJsonElement() {
		return jsonElement;
	}

	public void setJsonElement(JsonElement jsonElement) {
		this.jsonElement = jsonElement;
	}

	public JsonArray getJsonArray() {
		return jsonArray;
	}

	public void setJsonArray(JsonArray jsonArray) {
		this.jsonArray = jsonArray;
	}

	

	
	
	
	
	
}
